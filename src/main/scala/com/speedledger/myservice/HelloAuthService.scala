package com.speedledger.myservice

import cats.effect._
import org.http4s._
import org.http4s.dsl.Http4sDsl

case class HelloAuthService() extends Http4sDsl[IO] {
  val service: AuthedService[Unit, IO] = AuthedService[Unit, IO] {
    case GET -> Root / "authed" as _ => Ok("Hello  !")
  }


}
