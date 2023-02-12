package co.copper.test.routes

import cats.effect.IO
import cats.effect.kernel.{Async, Resource}
import org.http4s.server.{Router, Server}
import org.http4s.server.middleware.CORS
import org.http4s.blaze.server.BlazeServerBuilder
import sttp.tapir.server.http4s.Http4sServerInterpreter

import co.copper.test.services.StatusService

class HttpServer(statusService: StatusService) {

  def start(): Resource[IO, Server] = {
    val corsPolicy = CORS
      .policy
      .withAllowOriginAll
      .withAllowCredentials(false)

    val statusRoute = StatusRoute.statusRoute(statusService)

    val routes = Http4sServerInterpreter[IO]().toRoutes(statusRoute)

    val services = corsPolicy(routes)

    val httpApp = Router("/" -> services).orNotFound

    BlazeServerBuilder[IO]
      .bindHttp(9700, "0.0.0.0")
      .withHttpApp(httpApp)
      .resource
  }
}
