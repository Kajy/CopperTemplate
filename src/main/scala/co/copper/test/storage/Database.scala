package co.copper.test.storage

import cats.effect.{IO, Resource}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.flywaydb.core.Flyway
import org.typelevel.log4cats.slf4j.Slf4jLogger

import co.copper.test.config.DbConfig

object Database {

  def transactor(dbConfig: DbConfig): Resource[IO, HikariTransactor[IO]] = {
    for {
      ecd <- ExecutionContexts.fixedThreadPool[IO](10)
      xa <- HikariTransactor.newHikariTransactor[IO](
        "org.postgresql.Driver",
        s"${dbConfig.host}/${dbConfig.db}",
        dbConfig.user,
        dbConfig.password,
        ecd
      )
    } yield xa
  }

  def runEvolutionsOnDatabase(transactor: HikariTransactor[IO]): IO[Unit] = {
    val logger = Slf4jLogger.getLogger[IO]

    transactor.configure { dataSource =>
      for {
        _ <- logger.info("Starting SQL migration(s)...")
        _ <- IO.delay {
          val flyWay = Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load()
          flyWay.migrate()
        }
        _ <- logger.info("Database ready")
      } yield ()
    }
  }
}
