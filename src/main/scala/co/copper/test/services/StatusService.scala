package co.copper.test.services

import cats.effect.IO
import org.http4s.Method.GET
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.client.Client
import org.http4s.{Request, Uri}

import co.copper.test.models.StatusResponse

class StatusService(httpClient: Client[IO]) {

  def returnOk: IO[StatusResponse] = {
    val req = Request[IO](GET).withUri(Uri.unsafeFromString("https://postman-echo.com/get"))

    httpClient.expect[StatusResponse](req)
  }
}
