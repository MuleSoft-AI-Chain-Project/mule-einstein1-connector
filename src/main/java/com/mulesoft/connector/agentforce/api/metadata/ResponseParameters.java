package com.mulesoft.connector.agentforce.api.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mulesoft.connector.agentforce.api.metadata.token.TokenUsage;

import java.beans.ConstructorProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseParameters {

  private final TokenUsage tokenUsage;
  private final String model;
  private final String systemFingerprint;
  private final String object;

  @ConstructorProperties({"usage", "model", "systemFingerprint", "object"})
  public ResponseParameters(TokenUsage tokenUsage, String model, String systemFingerprint, String object) {
    this.tokenUsage = tokenUsage;
    this.model = model;
    this.systemFingerprint = systemFingerprint;
    this.object = object;
  }

  public TokenUsage getTokenUsage() {
    return tokenUsage;
  }

  public String getModel() {
    return model;
  }

  public String getSystemFingerprint() {
    return systemFingerprint;
  }

  public String getObject() {
    return object;
  }
}
