package co.copper.test.routes

import sttp.model.StatusCode.Ok
import sttp.tapir._
import sttp.tapir.json.circe._

import co.copper.test.models.StatusResponse
import co.copper.test.services.StatusService

object StatusRoute {

  def statusRoute(service: StatusService) = {
    endpoint
      .in("api" / "status")
      .name("Application status")
      .description("Check if server is running")
      .get
      .out(statusCode(Ok))
      .out(jsonBody[StatusResponse])
      .serverLogicSuccess(_ => service.returnOk)
  }
}
