import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Accept, `Content-Type`}
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import scala.io.StdIn
import scala.concurrent.duration._

object LLMClient {
  val logger = LoggerFactory.getLogger(this.getClass)

  implicit val system: ActorSystem = ActorSystem("LLMClient")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // Query history to store previous queries and responses
  var queryHistory: List[(String, String)] = List()

  // Function to send a POST request to the server and get the response
  def sendRequest(query: String): Future[String] = {
    val config = ConfigFactory.load()
    val serverHost = config.getString("server.host")
    val serverPort = config.getInt("server.port")

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://$serverHost:$serverPort/generate",
      entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, query),
      headers = List(
        Accept(MediaRanges.`*/*`),  // Accept header with a wildcard media range (accepts any content type)
        `Content-Type`(ContentTypes.`text/plain(UTF-8)`)  // Content-Type header
      )
    )

    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture.flatMap { response =>
      if (response.status.isSuccess()) {
        response.entity.dataBytes.runFold("")(_ + _.utf8String).map { body =>
          // Save query and response to history
          queryHistory = queryHistory :+ (query, body)
          body
        }
      } else {
        Future.failed(new Exception(s"Request failed with status ${response.status}"))
      }
    }
  }

  // Display the query history to the user
  def displayQueryHistory(): Unit = {
    if (queryHistory.isEmpty) {
      println("No query history available.")
    } else {
      println("Query History:")
      queryHistory.zipWithIndex.foreach { case ((query, response), index) =>
        println(s"${index + 1}. Query: $query\n   Response: $response\n")
      }
    }
  }

  // Function to handle the client interaction with the server
  def runClient(): Unit = {
    println("Welcome to the LLM Client!")
    println("You can type 'exit' to quit, or 'history' to view previous queries.")

    var running = true
    while (running) {
      println("\nEnter your query:")
      val query = StdIn.readLine()

      query.toLowerCase match {
        case "exit" =>
          println("Exiting the client...")
          running = false

        case "history" =>
          displayQueryHistory()

        case _ =>
          // Send the query to the server and display the response
          val responseFuture = sendRequest(query)

          // Handle response or failure
          responseFuture.onComplete {
            case Success(response) =>
              println(s"Response: $response")
              println(s"Time: ${java.time.LocalDateTime.now()}")
            case Failure(exception) =>
              println(s"Error occurred: ${exception.getMessage}")
          }

          // Timeout handling: To avoid blocking indefinitely, we add a timeout
          val timeoutFuture = akka.pattern.after(5.seconds, system.scheduler)(Future.failed(new Exception("Request timeout")))

          // Use `race` to handle timeout or actual response first
          val result = Future.firstCompletedOf(Seq(responseFuture, timeoutFuture))

          result.onComplete {
            case Success(response) =>
              println(s"Received response: $response")
            case Failure(ex) =>
              println(s"Error or Timeout: ${ex.getMessage}")
          }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    runClient()
  }
}
