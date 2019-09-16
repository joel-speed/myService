package com.speedledger.myservice

import cats.data.{Kleisli, OptionT}
import cats.effect.{ContextShift, IO}
import com.speedledger.myservice.Types.{ErrorResponse, TokenConfig}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedService, Request}


import scala.concurrent.ExecutionContext.Implicits.global


case class Auth (token:String) extends Http4sDsl[IO] with Codecs {
  implicit val cs: ContextShift[IO] = IO.contextShift(global)


  val authSession: Kleisli[IO, Request[IO], Either[String, Unit]] = Kleisli({ req =>
    IO {
      req.params.get("token") match {
        case Some(t) if t.equals(token)=>Right()
        case Some(_) => Left("Token invalid")
        case None =>Left("No token")
      }
    }
  })


  val onFailure: AuthedService[String, IO] = Kleisli(req => OptionT.liftF(Forbidden(ErrorResponse(req.authInfo))))

  val middleware = AuthMiddleware(authSession, onFailure)


}
