package com.speedledger.myservice

object Types {
  case class HttpConfig(port: Int)

  case class LoggingConfig(applicationName: String, version: String)

  case class DbConfig(connection: String, user: String, password: String)

  case class TokenConfig(password: String)

  case class Config(httpServer: HttpConfig, db: DbConfig, logging: LoggingConfig,token: TokenConfig)
  case class ErrorResponse(statusMessage: String)
}
