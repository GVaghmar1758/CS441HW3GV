import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object LLMClient {
  val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("LLMClient")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val config = ConfigFactory.load()
    val query = "How do cats express love?"
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = "http://localhost:8080/generate",
      entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, query)
    )

    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture.onComplete {
      case Success(res) =>
        res.entity.dataBytes.runForeach { chunk =>
          logger.info(s"Response: ${chunk.utf8String}")
        }
      case Failure(ex) =>
        logger.error(s"Failed to fetch response: ${ex.getMessage}")
    }

    // Terminate after delay
    Thread.sleep(2000)
    system.terminate()
  }
}
