package com.speedledger.myservice


import org.http4s.dsl.Http4sDsl
import cats.effect._
import org.http4s._
import io.circe._
import org.http4s.circe._
import io.circe.syntax._
import io.circe.literal._
import io.circe.generic.semiauto._
import cats.data.ValidatedNel
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.util.CaseInsensitiveString
import org.json4s.native.JsonMethods
import cats._
import cats.effect._
import cats.implicits._
import cats.data._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server._

import scala.tools.nsc.doc.html.page.JSONArray


case class User(token: String)
case class HelloWorldService(db: Database) extends Http4sDsl[IO] {

  implicit val todoDecoder: Decoder[Todo] = deriveDecoder
  implicit val todoEncoder: Encoder[Todo] = deriveEncoder

  object TokenQueryParamMatcher extends QueryParamDecoderMatcher[String]("token")
  object PayloadQueryParamMatcher extends QueryParamDecoderMatcher[String]("text")

  def tupleToJson(T:(String,String)):Json = {
    json"""{
          "title":${T._1},
          "value":${T._2},
          "short":true
          }"""
  }



  val service: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ GET -> Root / "helloworld" =>

      Ok("hej hej")

    case req @ GET -> Root / "hellodbswb" :? PayloadQueryParamMatcher(payload) =>


        db.getCustomerBySwbId(payload.toInt).flatMap {

          case Some(todo) => {

            val jsonItemArray = Map("Project"->"Awsome","Environment"->"production").map(tupleToJson).asJson

            val jsonFieldsArray: Json = Json.fromValues(List(
             Json.fromFields(List(("fields", jsonItemArray)))))

            val json = Json.obj("attachments" -> jsonFieldsArray)

            Ok(json)

          }

          case _ => NotFound()
        }


    case req @ GET -> Root / "hellodbemail" :? PayloadQueryParamMatcher(payload) :? TokenQueryParamMatcher(slackToken) =>

      db.getCustomerByEmail(payload).flatMap {
        case Some(todo) =>{ Ok(todo.asJson)}
        case _          => NotFound()
      }
  }
}
