package cl.rwangnet.service.rest.route;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Adding a little helper here to ensure the negative json validation scenario
        onException(JsonValidationException.class)
                .handled(true)
                .setBody(exchange -> {
                    Map<String, String> json = new HashMap<>();
                    json.put("greeting",
                            "Hello my friend! Looks like you're trying to send invalid data, huh? Try again and be in compliance with the schema!");
                    return json;
                })
                .marshal().json(JsonLibrary.Jackson);
        ;

        from("direct:getHelloRoute")
                .setBody(exchange -> {
                    Map<String, String> json = new HashMap<>();
                    json.put("greeting", "Hello my friend! Regards from the GET operation of the sample service.");
                    return json;
                })
                .marshal().json(JsonLibrary.Jackson);
        ;

        from("direct:postHelloRoute")
                .to("json-validator:/schemas/post.json")

                // If its valid get elements using json path end store the values in Camel
                // props.
                .setProperty("name", jsonpath("$.name"))
                .setProperty("message", jsonpath("$.message"))

                // Returns a SUCCESS response
                .setBody(exchange -> {
                    Map<String, String> json = new HashMap<>();
                    json.put("greeting", "Hello " + exchange.getProperty("name")
                            + "!. Regards from the POST operation of the sample service. We received your custom message which contains something like this: << "
                            + exchange.getProperty("message") + " >>. so, thank you for Calling!");
                    return json;
                })
                .marshal().json(JsonLibrary.Jackson);

    }

}
