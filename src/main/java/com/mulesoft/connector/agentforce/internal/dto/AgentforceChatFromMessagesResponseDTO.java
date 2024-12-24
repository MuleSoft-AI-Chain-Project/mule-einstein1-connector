package com.mulesoft.connector.agentforce.internal.dto;

public class AgentforceChatFromMessagesResponseDTO {

  private String id;
  private GenerationDetailsDTO generationDetails;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GenerationDetailsDTO getGenerationDetails() {
    return generationDetails;
  }

  public void setGenerationDetails(GenerationDetailsDTO generationDetails) {
    this.generationDetails = generationDetails;
  }
}
