package com.mulesoft.connector.agentforce.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mulesoft.connector.agentforce.api.metadata.GenerationParameters;
import com.mulesoft.connector.agentforce.api.metadata.quality.ContentQuality;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationDTO {

  private String id;
  private String generatedText;
  private ContentQuality contentQuality;
  private GenerationParameters parameters;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getGeneratedText() {
    return generatedText;
  }

  public void setGeneratedText(String generatedText) {
    this.generatedText = generatedText;
  }

  public ContentQuality getContentQuality() {
    return contentQuality;
  }

  public void setContentQuality(ContentQuality contentQuality) {
    this.contentQuality = contentQuality;
  }

  public GenerationParameters getParameters() {
    return parameters;
  }

  public void setParameters(GenerationParameters parameters) {
    this.parameters = parameters;
  }
}
