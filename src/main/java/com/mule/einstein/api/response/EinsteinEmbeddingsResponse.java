package com.mule.einstein.api.response;

import java.util.List;

public class EinsteinEmbeddingsResponse {

  private final List<EinsteinEmbedding> embeddings;

  public EinsteinEmbeddingsResponse(List<EinsteinEmbedding> embeddings) {
    this.embeddings = embeddings;
  }

  public List<EinsteinEmbedding> getEmbeddings() {
    return embeddings;
  }
}
