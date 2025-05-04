package cl.rwangnet.service.rest.route;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
/**
 * @author: rwangnet
 * @apiNote: Route which implements the REST configuration items.
 */
public class RestRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration()
				.contextPath("/api/v1")
				.bindingMode(RestBindingMode.off)
				.scheme("http")
				.dataFormatProperty("prettyPrint", "true");

		rest("/samples/simple-rest")

				.get("/healthcheck").id("_healthcheckEndpoint")
				.produces(MediaType.APPLICATION_JSON)
				.to("direct:healthCheckRoute")

				.get("/greetings").id("getGreeting")
				.produces(MediaType.APPLICATION_JSON)
				.to("direct:getHelloRoute")

				.post("/greetings").id("postGreeting")
				.produces(MediaType.APPLICATION_JSON)
				.to("direct:postHelloRoute");

		from("direct:healthCheckRoute").routeId("healthCheckId")
				.setBody(exchange -> {
					Map<String, String> json = new HashMap<>();
					json.put("status", "OK");
					return json;
				})
				.marshal().json(JsonLibrary.Jackson);
	}

}
