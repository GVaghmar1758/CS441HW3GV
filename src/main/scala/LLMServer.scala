import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object LLMServer {
  val logger = LoggerFactory.getLogger(this.getClass)
  val config = ConfigFactory.load()
  val model = if (config.hasPath("llama.model")) config.getString("llama.model") else "default-model"
  // Define HTTP routes
  val route =
    path("generate") {
      post {
        entity(as[String]) { query =>
          if (query.isEmpty) {
            logger.warn("Received an empty query")
            complete(StatusCodes.BadRequest, "Query cannot be empty")
          } else if (query == "trigger-error") {
            logger.error("Simulated server error triggered")
            complete(StatusCodes.InternalServerError, "An internal server error occurred")
          } else {
            logger.info(s"Received query: $query")
            val response = s"Response to: '$query' using model '$model'"
            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, response))
          }
        }
      }
    } ~
      pathPrefix(Segment) { _ =>
        complete(StatusCodes.NotFound, "The requested route does not exist")
      }

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("LLMServer")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    // Load configuration
    val config = ConfigFactory.load()

    // Get configuration values with fallbacks
    val host = if (config.hasPath("llama.host")) config.getString("llama.host") else "localhost"
    val model = if (config.hasPath("llama.model")) config.getString("llama.model") else "default-model"
    val timeout = if (config.hasPath("llama.request-timeout-seconds")) config.getInt("llama.request-timeout-seconds") else 30

    logger.info(s"Starting server with model: $model at $host")

    // Start the server
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
