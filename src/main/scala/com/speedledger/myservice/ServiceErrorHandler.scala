package com.speedledger.myservice

import cats.effect._
import org.http4s._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._

object ServiceErrorHandler {

  private val logger = org.log4s.getLogger
  def service(): ServiceErrorHandler[IO] = req => {
    case mf: MessageFailure =>
      val message =
        s"""Message failure handling request: ${req.method} ${req.pathInfo} from ${req.remoteAddr
          .getOrElse("<unknown>")}"""
      logger.debug(message)
      mf.toHttpResponse(req.httpVersion)
    case t if !t.isInstanceOf[VirtualMachineError] =>
      val message =
        s"""Error servicing request: ${req.method} ${req.pathInfo} from ${req.remoteAddr.getOrElse("<unknown>")}"""
      logger.debug(t)(message)
      IO.pure(
        Response(
          Status.InternalServerError,
          req.httpVersion,
          Headers.of(
            Connection("close".ci),
            `Content-Length`.zero
          )))
    case error =>
      val message = s"""Runtime error was thrown while processing a request. Throwable was: $error"""
      logger.error(message)
      IO.pure(
        Response(
          Status.InternalServerError,
          req.httpVersion,
          Headers.of(
            Connection("close".ci),
            `Content-Length`.zero
          )))
  }

}
