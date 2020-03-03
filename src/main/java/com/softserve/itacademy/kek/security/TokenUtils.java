package com.softserve.itacademy.kek.security;

import java.util.Map;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for working with tokens
 */
public class TokenUtils {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    /**
     * Get the JWT claims as a JSON string.
     *
     * @param claims the JWT ID token claims
     * @return the claims as JSON
     */
    public static String claimsAsJson(Map<String, Claim> claims) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();


        claims.forEach((key, value) -> {
            if (value.asMap() != null) {
                node.putPOJO(key, value.asMap());
            } else if (value.asList(String.class) != null) {
                JsonNode jsonNode = objectMapper.valueToTree(value.asList(String.class));
                node.set(key, jsonNode);
            } else if (value.asBoolean() != null) {
                node.put(key, value.asBoolean());
            } else if (value.asInt() != null) {
                node.put(key, value.asInt());
            } else if (value.as(String.class) != null) {
                node.put(key, value.as(String.class));
            } else if (value.isNull()) {
                node.putNull(key);
            }
        });

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (Exception e) {
            logger.error("Error processing json from profile", e);
            throw new IllegalArgumentException(e);
        }
    }
}
