package co.copper.test.config

case class DbConfig(host: String, db: String, user: String, password: String)
case class AppConfig(db: DbConfig)

