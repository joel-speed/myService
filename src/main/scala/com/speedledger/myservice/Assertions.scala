package com.speedledger.myservice

import cats.syntax.all._
import cats.effect._

object Assertions {
  def assertRight[E, R, F[_]: Effect](failureDescription: Either[E, R])(describe: E => String): F[R] =
    failureDescription match {
      case Left(failure) =>
        Effect[F]
          .pure(
            org.log4s.getLogger.debug(s"Assertion failed. IO action cancelled. Failure reason: ${describe(failure)}"))
          .flatMap(_ => Effect[F].raiseError(new Exception(describe(failure))))
      case Right(value) => Effect[F].pure(value)
    }
}
