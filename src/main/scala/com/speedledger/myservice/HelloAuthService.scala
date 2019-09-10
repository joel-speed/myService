package com.speedledger.myservice

import com.speedledger.myservice.Auth.SpeedLedgerSession
import cats.effect._
import org.http4s._
import org.http4s.dsl.Http4sDsl

case class HelloAuthService() extends Http4sDsl[IO] {
  val service: AuthedService[SpeedLedgerSession, IO] = AuthedService[SpeedLedgerSession, IO] {
    case GET -> Root / "authed" as session => Ok(s"Hello ${session.dataOrgId} !")
  }
}
