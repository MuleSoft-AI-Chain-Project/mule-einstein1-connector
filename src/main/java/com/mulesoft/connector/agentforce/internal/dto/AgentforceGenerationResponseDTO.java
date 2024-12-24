package com.mulesoft.connector.agentforce.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mulesoft.connector.agentforce.api.metadata.ResponseParameters;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentforceGenerationResponseDTO {

  private String id;
  private GenerationDTO generation;
  private Object moreGenerations;
  private Object prompt;
  private ResponseParameters parameters;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GenerationDTO getGeneration() {
    return generation;
  }

  public void setGeneration(GenerationDTO generation) {
    this.generation = generation;
  }

  public Object getMoreGenerations() {
    return moreGenerations;
  }

  public void setMoreGenerations(Object moreGenerations) {
    this.moreGenerations = moreGenerations;
  }

  public Object getPrompt() {
    return prompt;
  }

  public void setPrompt(Object prompt) {
    this.prompt = prompt;
  }

  public ResponseParameters getParameters() {
    return parameters;
  }

  public void setParameters(ResponseParameters parameters) {
    this.parameters = parameters;
  }
}
