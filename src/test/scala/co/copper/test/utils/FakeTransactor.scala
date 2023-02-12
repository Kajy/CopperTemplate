package co.copper.test.utils

import cats.effect.IO
import doobie.Transactor

trait FakeTransactor {

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/test",
    "postgres",
    "1234"
  )
}
