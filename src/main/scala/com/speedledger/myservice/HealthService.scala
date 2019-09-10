package com.speedledger.myservice

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

case class HealthService() extends Http4sDsl[IO] {
  val service: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "ping" => Ok("OK")
  }
}
