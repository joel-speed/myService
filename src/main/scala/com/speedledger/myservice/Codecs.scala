package com.speedledger.myservice

import com.speedledger.myservice.Types.ErrorResponse
import cats.effect._
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder, circe}

trait Codecs {
  implicit def circeEntityEncoder[A: Encoder]: EntityEncoder[IO, A] = circe.jsonEncoderOf
  implicit def circeEntityDecoder[A: Decoder]: EntityDecoder[IO, A] = circe.jsonOf

  implicit val errorResponseEncoder: Encoder[ErrorResponse] = deriveEncoder
}
