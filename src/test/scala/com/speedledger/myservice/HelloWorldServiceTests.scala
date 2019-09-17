//package com.speedledger.myservice
//
//import cats.effect.IO
//import org.http4s._
//import org.http4s.{Request}
//import org.http4s.syntax.kleisli.http4sKleisliResponseSyntax
//import org.http4s.client.{Client => HttpClient}
//import org.scalatest._
//
//class HelloWorldServiceTests extends FreeSpec with MustMatchers {
//
//  val fakeDb = new Database {
//    override def getTodo: IO[Option[Todo]] = IO(Some(Todo(1, "foobar")))
//    override def getCustomerBySwbId(id:Int): IO[Option[Todo]] = IO(Some(Todo(1, "foobar")))
//    override def getCustomerByEmail(email:String): IO[Option[Todo]] = IO(Some(Todo(1, "foobar")))
//    override def getCustomerByDataOrgId(dataOrgId:Int): IO[Option[Todo]] = IO(Some(Todo(1, "foobar")))
//    override def getCustomerByOrgId(orgId:Int): IO[Option[Todo]] = IO(Some(Todo(1, "foobar")))
//  }
//  val client = HttpClient.fromHttpApp(HelloWorldService(fakeDb).service.orNotFound)
//  "http service" - {
//    "hello world" - {
//      "replies with 200 status code" in {
//        val status = client.fetch(Request[IO](Method.GET, uri"/helloworld"))(rsp => IO(rsp.status.code)).unsafeRunSync
//        status mustBe 200
//      }
//
//      "contains 'world'" in {
//        client.expect[String]("/helloworld").unsafeRunSync() must include("world")
//      }
//    }
//
//    "hello db" - {
//      "contains todo title" in {
//        client.expect[String]("/hellodb").unsafeRunSync() must include("foobar")
//      }
//    }
//  }
//}
