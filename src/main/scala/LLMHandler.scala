import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

import java.util.Base64

class LLMHandler extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent] {

  override def handleRequest(
                              input: APIGatewayProxyRequestEvent,
                              context: Context
                            ): APIGatewayProxyResponseEvent = {

    // Decode Base64 body if it's encoded
    val body = if (input.getIsBase64Encoded) {
      new String(Base64.getDecoder.decode(input.getBody))
    } else {
      input.getBody
    }

    // Simulate processing the request (Replace this with your actual model logic)
    val processedResponse = s"Processed: $body"

    // Prepare the response
    val response = new APIGatewayProxyResponseEvent()
    response.setStatusCode(200)
    response.setBody(processedResponse)
    response
  }
}
