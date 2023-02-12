package co.copper.test.storage

import cats.effect.MonadCancelThrow
import doobie.syntax.string._
import doobie.syntax.connectionio._
import doobie.Transactor

import co.copper.test.models.Test

class TestRepository[F[_]: MonadCancelThrow](transactor: Transactor[F]) {

  def getById(id: Long): F[List[Test]] = {
    TestRepository.getByIdQuery(id).to[List].transact(transactor)
  }
}

object TestRepository {
  def getByIdQuery(id: Long) = {
    sql"SELECT id, val FROM test WHERE id = $id".query[Test]
  }
}
