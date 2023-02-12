package co.copper.test

import cats.effect.{IO, IOApp}
import cats.effect.syntax.resource._
import org.http4s.blaze.client.BlazeClientBuilder

import co.copper.test.config.ConfigLoader
import co.copper.test.routes.HttpServer
import co.copper.test.services.StatusService
import co.copper.test.storage.Database

object Main extends IOApp.Simple {

  override def run: IO[Unit] = {
    {
      for {
        config <- new ConfigLoader[IO].load()

        httpClient <- BlazeClientBuilder[IO].resource

        pgTransactor <- Database.transactor(config.db)
        _ <- Database.runEvolutionsOnDatabase(pgTransactor).toResource

        statusService = new StatusService(httpClient)
        server = new HttpServer(statusService)
        _ <- server.start()
      } yield ()
    }.useForever
  }
}
