/*
package com.speedledger.myservice

import cats.effect.IO
import com.speedledger.myservice.Auth.SpeedLedgerSession
import org.http4s.dsl.Http4sDsl
import org.http4s.syntax.kleisli.http4sKleisliResponseSyntax
import org.http4s.{Request, _}
import org.scalatest.{FreeSpec, MustMatchers}

class AuthTest extends FreeSpec with MustMatchers with Http4sDsl[IO] {
  val service = AuthedService[SpeedLedgerSession, IO] {
    case GET -> Root as session => Ok(s"Hello ${session.dataOrgId} ${session.authLevel} ${session.status} !")
  }

  val authedService = Auth.middleware(service)

  "auth" - {
    "forbidden if no headers are present" in {
      val req = Request[IO](uri = uri"/")
      val res = authedService.orNotFound(req).unsafeRunSync
      res.status mustBe Forbidden
    }
    "forbidden if headers cannot be parsed" in {
      val req = Request[IO](
        uri = uri"/",
        headers = Headers.of(Header(Auth.authHeaderName.toString, "foobar"))
      )
      val res = authedService.orNotFound(req).unsafeRunSync
      res.status mustBe Forbidden
    }
    "retrieves dataOrgId from headers" in {
      val req = Request[IO](
        uri = uri"/",
        headers = Headers.of(Header(Auth.authHeaderName.toString, "1234:BOKFEF:WRITE:VALID:SL"))
      )
      val res = authedService.orNotFound(req).unsafeRunSync
      res.status mustBe Ok
      val body: String = res.as[String].unsafeRunSync
      body mustBe "Hello 1234 WRITE ValidAuthStatus !"
    }
  }

}
*/
