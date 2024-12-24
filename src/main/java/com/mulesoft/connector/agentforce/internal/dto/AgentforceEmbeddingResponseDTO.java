package com.mulesoft.connector.agentforce.internal.dto;

import com.mulesoft.connector.agentforce.api.metadata.ResponseParameters;

import java.util.List;

public class AgentforceEmbeddingResponseDTO {

  private List<AgentforceEmbeddingDTO> embeddings;
  private ResponseParameters parameters;

  public List<AgentforceEmbeddingDTO> getEmbeddings() {
    return embeddings;
  }

  public void setEmbeddings(List<AgentforceEmbeddingDTO> embeddings) {
    this.embeddings = embeddings;
  }

  public ResponseParameters getParameters() {
    return parameters;
  }

  public void setParameters(ResponseParameters parameters) {
    this.parameters = parameters;
  }
}
