import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class LLMServerSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {
  "LLMServer" should {

    "respond with OK and a valid response for a POST request" in {
      val query = "What is Scala?"
      Post("/generate", HttpEntity(ContentTypes.`text/plain(UTF-8)`, query)) ~> LLMServer.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] should include("Response to")
      }
    }

    "respond with 400 Bad Request for an empty query" in {
      val query = ""
      Post("/generate", HttpEntity(ContentTypes.`text/plain(UTF-8)`, query)) ~> LLMServer.route ~> check {
        status shouldBe StatusCodes.BadRequest
        responseAs[String] should include("Query cannot be empty")
      }
    }

    "handle large queries gracefully" in {
      val largeQuery = "a" * 10000
      Post("/generate", HttpEntity(ContentTypes.`text/plain(UTF-8)`, largeQuery)) ~> LLMServer.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] should include("Response to")
      }
    }

    "respond with 404 for unsupported routes" in {
      Get("/unsupported-route") ~> LLMServer.route ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "handle server errors gracefully" in {
      val query = "trigger-error"
      Post("/generate", HttpEntity(ContentTypes.`text/plain(UTF-8)`, query)) ~> LLMServer.route ~> check {
        status shouldBe StatusCodes.InternalServerError
        responseAs[String] should include("An internal server error occurred")
      }
    }
  }
}
