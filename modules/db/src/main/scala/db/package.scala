import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.{ClassLoaderResourceAccessor, CompositeResourceAccessor, FileSystemResourceAccessor}
import liquibase.servicelocator.LiquibaseService
import org.squeryl.adapters.{H2Adapter, PostgreSqlAdapter}
import org.squeryl.internals.DatabaseAdapter


package object db {
  private val configuration = ConfigFactory.load()
  private val config = new HikariConfig()

  private val url: String = configuration.getString("db.default.url")
  private val user: String = configuration.getString("db.default.user")
  private val password: String = configuration.getString("db.default.password")
  config.setJdbcUrl(url)
  config.setUsername(user)
  config.setPassword(password)

  val hikariDataSource = new HikariDataSource(config)

  object SquerylConfig
  {
    lazy val dbDefaultAdapter: DatabaseAdapter = configuration.getString("db.default.driver") match
    {
      case "org.h2.Driver" => new H2Adapter
      case "org.postgresql.Driver" => new PostgreSqlAdapter
      case _ => sys.error("Database driver must be either org.h2.Driver or org.postgresql.Driver")
    }


    lazy val logSql: Boolean = configuration.getBoolean("log-sql")
  }

  object Liquidbase {
    def mkLiquibase: Liquibase = {
      val fileAccessor = new FileSystemResourceAccessor()
      val classLoader  = classOf[LiquibaseService].getClassLoader
      val classLoaderAccessor = new ClassLoaderResourceAccessor(classLoader)
      val fileOpener = new CompositeResourceAccessor(fileAccessor, classLoaderAccessor)
      val dbConnection: JdbcConnection = new JdbcConnection(hikariDataSource.getConnection)
      val liquidbase = new Liquibase(configuration.getString("changelog"), fileOpener, dbConnection)
      liquidbase
    }
  }
}
