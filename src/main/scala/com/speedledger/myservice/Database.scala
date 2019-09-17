package com.speedledger.myservice

import cats.effect.IO
import doobie.util.transactor.Transactor
import doobie.implicits._


case class Todo(id:Long,title:String,email:String,Org_Num:String)



trait Database {
  def getTodo: IO[Option[Todo]]
  def getCustomerBySwbId(id:Int): IO[Option[Todo]]
  def getCustomerByEmail(email:String): IO[Option[Todo]]
  def getCustomerByDataOrgId(dataOrgId:Int): IO[Option[Todo]]
  def getCustomerByOrgId(orgId:Int): IO[Option[Unit]]

}

case class PgDatabase(db: Transactor[IO]) extends Database {



  def getTodo: IO[Option[Todo]] =
    sql"SELECT id, title, created_at FROM todo ".query[Todo].option.transact(db)

  def getCustomerBySwbId(id:Int): IO[Option[Todo]] =
    sql"SELECT * FROM todo WHERE id = $id".query[Todo].option.transact(db)

  def getCustomerByEmail(email:String): IO[Option[Todo]] =
    sql"SELECT * FROM todo WHERE id = $email".query[Todo].option.transact(db)

  def getCustomerByDataOrgId(dataOrgId:Int): IO[Option[Todo]] =
    sql"SELECT * FROM todo WHERE id = $dataOrgId".query[Todo].option.transact(db)

  def getCustomerByOrgId(orgId:Int): IO[Option[Unit]] =
    sql"SELECT * FROM todo WHERE id = $orgId".query.option.transact(db)

}
