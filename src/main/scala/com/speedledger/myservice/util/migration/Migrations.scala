package com.speedledger.myservice.util.migration

import cats.effect.IO
import org.flywaydb.core.Flyway

object Migrations extends Migrations

trait Migrations {

  def databaseMigrations(connectionString: String, username: String, password: String): IO[Int] =
    IO({
      val flyway = new Flyway()
      flyway.setDataSource(connectionString, username, password)
      flyway.migrate
    })
}
