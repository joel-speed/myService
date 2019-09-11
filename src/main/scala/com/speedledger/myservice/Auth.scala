package com.speedledger.myservice

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import com.speedledger.myservice.Types.{ErrorResponse, TokenConfig}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.util.CaseInsensitiveString
import org.http4s.{AuthedService, Request}

object Auth extends Http4sDsl[IO] with Codecs {
  val authHeaderName = CaseInsensitiveString("SpeedLedger-Session-Auth")


  /*val authSession: Kleisli[IO, Request[IO], Either[String, SpeedLedgerSession]] = Kleisli({ req =>
    IO(for {
      header <- req.headers.get(authHeaderName).toRight("Found no speedledger session header")
      session <- sessionFromHeaderValue(header.value).toRight("Could not parse speedledger session header")
    } yield session)
  })*/

  val authSession: Kleisli[IO, Request[IO], Either[String, Unit]] = Kleisli({ req =>
    IO(for {

      param <- req.params.get("token").toRight("Found no Token")
    } yield if(param=="kaffe")Right else Left ("invalid"))
  })


  val onFailure: AuthedService[String, IO] = Kleisli(req => OptionT.liftF(Forbidden(ErrorResponse(req.authInfo))))

  val middleware = AuthMiddleware(authSession, onFailure)

  private def sessionFromHeaderValue(string: String): Option[SpeedLedgerSession] = {
    string.split(":") match {
      case Array(orgId, prodPart, AuthLevel(level), AuthStatus(status), site) =>
        Some(SpeedLedgerSession(orgId.toLong, prodPart, level, status, site))
      case _ => None
    }
  }



  final case class SpeedLedgerSession(dataOrgId: Long,
    prodPart: String,
    authLevel: AuthLevel,
    status: AuthStatus,
    site: String) {
    /*
      Check if the current session matches against a data org and auth level,
      returning 'result' if the operation is allowed, otherwise nothing
    */
    def validateFor[A](dataOrgId: Option[Long], minimumAuthLevel: AuthLevel = ReadAuth) (result: A): Option[A] = {
      if (this.authLevel >= minimumAuthLevel && dataOrgId.forall(_ == this.dataOrgId))
        Some(result)
      else
        None
    }
  }

  sealed abstract class AuthLevel(val value: String, protected val level: Int) extends Ordered[AuthLevel] {
    override def compare(that: AuthLevel) = this.level compareTo that.level

    override def toString = value
  }

  object AuthLevel {
    def unapply(text: String): Option[AuthLevel] = text match {
      case "NONE"  => Some(NoneAuth)
      case "READ"  => Some(ReadAuth)
      case "WRITE" => Some(WriteAuth)
      case "ADMIN" => Some(AdminAuth)
      case _       => None
    }
  }

  case object NoneAuth extends AuthLevel("NONE", 0)

  case object ReadAuth extends AuthLevel("READ", 1)

  case object WriteAuth extends AuthLevel("WRITE", 2)

  case object AdminAuth extends AuthLevel("ADMIN", 3)


  sealed trait AuthStatus

  object AuthStatus {
    def unapply(text: String): Option[AuthStatus] = text match {
      case "VALID"   => Some(ValidAuthStatus)
      case "EXPIRED" => Some(ExpiredAuthStatus)
      case _         => None
    }
  }

  case object ValidAuthStatus extends AuthStatus

  case object ExpiredAuthStatus extends AuthStatus
}
