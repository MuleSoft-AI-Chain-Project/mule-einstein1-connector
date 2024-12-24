package com.mulesoft.connector.agentforce.api.metadata;

import com.mulesoft.connector.agentforce.api.metadata.quality.ContentQuality;

public class AgentforceResponseAttributes {

  private final String responseId;
  private final String generationId;
  private final ContentQuality contentQuality;
  private final GenerationParameters generationParameters;
  private final ResponseParameters responseParameters;

  public AgentforceResponseAttributes(String responseId, String generationId, ContentQuality contentQuality,
                                      GenerationParameters generationParameters, ResponseParameters responseParameters) {
    this.responseId = responseId;
    this.generationId = generationId;
    this.contentQuality = contentQuality;
    this.generationParameters = generationParameters;
    this.responseParameters = responseParameters;
  }

  public String getResponseId() {
    return responseId;
  }

  public String getGenerationId() {
    return generationId;
  }

  public ContentQuality getContentQuality() {
    return contentQuality;
  }

  public GenerationParameters getGenerationParameters() {
    return generationParameters;
  }

  public ResponseParameters getResponseParameters() {
    return responseParameters;
  }
}
