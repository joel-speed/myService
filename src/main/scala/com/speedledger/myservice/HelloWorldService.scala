package com.speedledger.myservice


import org.http4s.dsl.Http4sDsl
import cats.effect._
import org.http4s._
import io.circe._
import org.http4s.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._
import cats.data.ValidatedNel
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.util.CaseInsensitiveString
import org.json4s.native.JsonMethods



case class HelloWorldService(db: Database) extends Http4sDsl[IO] {

  implicit val todoDecoder: Decoder[Todo] = deriveDecoder
  implicit val todoEncoder: Encoder[Todo] = deriveEncoder

  object TokenQueryParamMatcher extends QueryParamDecoderMatcher[String]("token")
  object PayloadQueryParamMatcher extends QueryParamDecoderMatcher[String]("text")





  val service: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ GET -> Root / "helloworld" =>
      //val postmanToken= req.headers.get(CaseInsensitiveString("Postman-Token"))
      val json = Json.obj("hello" -> Json.fromString("world"))

      print(req.headers)

      Ok("Hej")

    case req @ GET -> Root / "hellodbswb" :? PayloadQueryParamMatcher(payload) :? TokenQueryParamMatcher(slackToken) =>
      println(slackToken)
      print(req.headers)

      db.getCustomerBySwbId(payload.toInt).flatMap {
        case Some(todo) =>{ Ok(todo.asJson)}
        case _          => NotFound()
      }

    case req @ GET -> Root / "hellodbemail" :? PayloadQueryParamMatcher(payload) :? TokenQueryParamMatcher(slackToken) =>

      db.getCustomerByEmail(payload).flatMap {
        case Some(todo) =>{ Ok(todo.asJson)}
        case _          => NotFound()
      }
  }
}
