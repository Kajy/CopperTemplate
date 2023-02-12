package co.copper.test.models

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema

case class StatusResponse(url: String, headers: Map[String, String])

object StatusResponse {
  implicit val schema: Schema[StatusResponse] = Schema.derived[StatusResponse]
  implicit val codec: Codec[StatusResponse] = deriveCodec[StatusResponse]
}
