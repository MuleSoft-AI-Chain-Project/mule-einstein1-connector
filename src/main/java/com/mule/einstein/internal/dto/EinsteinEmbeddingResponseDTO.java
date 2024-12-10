package com.mule.einstein.internal.dto;

import com.mule.einstein.api.metadata.ResponseParameters;
import com.mule.einstein.api.response.EinsteinEmbedding;

import java.util.List;

public class EinsteinEmbeddingResponseDTO {

  private List<EinsteinEmbedding> embeddings;
  private ResponseParameters parameters;

  public List<EinsteinEmbedding> getEmbeddings() {
    return embeddings;
  }

  public void setEmbeddings(List<EinsteinEmbedding> embeddings) {
    this.embeddings = embeddings;
  }

  public ResponseParameters getParameters() {
    return parameters;
  }

  public void setParameters(ResponseParameters parameters) {
    this.parameters = parameters;
  }
}
