package co.copper.test.config

import cats.syntax.apply._
import cats.syntax.flatMap._

import cats.effect.Resource
import cats.effect.kernel.Async
import com.typesafe.config.ConfigFactory

import lt.dvim.ciris.Hocon._

class ConfigLoader[F[_]: Async] {
  def load(): Resource[F, AppConfig] = {
    val config = hoconAt(ConfigFactory.load())(_)

    (
      for {
        dbConfig <-
          (
            config("copper.test.db")("host").as[String],
            config("copper.test.db")("db").as[String],
            config("copper.test.db")("username").as[String],
            config("copper.test.db")("password").as[String]
          ).mapN(DbConfig.apply)
      } yield AppConfig(dbConfig)
      ).resource
  }
}
