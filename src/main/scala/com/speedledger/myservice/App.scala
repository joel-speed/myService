package com.speedledger.myservice

import com.speedledger.myservice.Types._
import com.speedledger.jsonlogging.formatting.JsonLoggingSettings
import com.speedledger.myservice.util.migration.Migrations
import cats.effect._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.client.blaze.BlazeClientBuilder
import doobie.free.connection.ConnectionIO
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import doobie.implicits._
import cats.implicits._
import org.http4s.server.Router
import org.log4s._
import pureconfig.generic.auto._

object App extends IOApp {
  def run(args: List[String]): IO[ExitCode] = AppServer.serve
}

object AppServer extends Migrations {
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

  private val logger = getLogger

  def makeDb(config: DbConfig): Database = {
    val db: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver",
      config.connection,
      config.user,
      config.password
    )
    val setSchema: ConnectionIO[Unit] = sql"set SEARCH_PATH = 'myservice'".update.run.void
    val dbWithSchema: Transactor[IO] = Transactor.before.modify(db, _ *> setSchema)

    PgDatabase(dbWithSchema)
  }

  def databaseMigrations(config: DbConfig): IO[Unit] = {
    databaseMigrations(config.connection, config.user, config.password).map {
      case 0 => logger.info("No database migrations executed")
      case n => logger.info(s"$n database migrations executed")
    }
  }

  def assertRight[E, R](failureDescription: Either[E, R])(describe: E => String): IO[R] =
    Assertions.assertRight[E, R, IO](failureDescription)(describe)

  def applyLoggingBeanSettings(loggingConfig: LoggingConfig): IO[Unit] = {
    IO.pure {
      JsonLoggingSettings.applicationName = loggingConfig.applicationName
      JsonLoggingSettings.applicationVersion = loggingConfig.version
    }
  }

  private def applyToken(tokenConfig: TokenConfig): IO[Unit] = {
    IO.pure {
      println(tokenConfig.password)
    }
  }

  def loadConfig(): IO[Config] =
    assertRight(pureconfig.loadConfig[Config])(_.toList.map(_.toString).mkString(", "))


  def serve(): IO[ExitCode] = {
    for {
      config <- loadConfig()
      _ <- IO(logger.info(s"starting server on ${config.httpServer.port}"))
      _ <- databaseMigrations(config.db)
      _ <- applyLoggingBeanSettings(config.logging)
      _<- applyToken(config.token)



      client = BlazeClientBuilder[IO](global).resource
      db = makeDb(config.db)
      services = Router(
        //"/" -> Auth.middleware(HelloAuthService().service),
        "/" -> HelloWorldService(db).service,
        "/" -> HealthService().service
      ).orNotFound
      server <- BlazeServerBuilder[IO]
        .bindHttp(config.httpServer.port, "0.0.0.0")
        .withHttpApp(services)
        .withServiceErrorHandler(ServiceErrorHandler.service())
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    } yield server
  }
}
