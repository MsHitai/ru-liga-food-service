package ru.liga;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {@Server(url = "http://127.0.0.1:8080")},
        info = @Info(title = "Delivery Service API", description = "This lists all the Delivery Service API Calls. " +
                "The Calls are OAuth2 secured, so please use your client ID and Secret to test them out.",
                version = "v1.0"))
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(clientCredentials = @OAuthFlow(tokenUrl = "${openapi.oAuthFlow.tokenUrl}", scopes = {
                @OAuthScope(name = "openid", description = "openid scope"), @OAuthScope(name = "message.read")
        })))
@SpringBootApplication
public class DeliveryService {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryService.class, args);
    }
}
