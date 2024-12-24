package com.mulesoft.connector.agentforce.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.beans.ConstructorProperties;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthResponseDTO {

  private final String accessToken;
  private final String apiInstanceUrl;

  @ConstructorProperties({"accessToken", "apiInstanceUrl"})
  public OAuthResponseDTO(String accessToken, String apiInstanceUrl) {
    this.accessToken = accessToken;
    this.apiInstanceUrl = apiInstanceUrl;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getApiInstanceUrl() {
    return apiInstanceUrl;
  }

}
