package com.mule.einstein.api.response;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.util.List;

public class EinsteinEmbedding {

  private final int index;
  private final List<BigDecimal> embedding;

  @ConstructorProperties({"index", "embedding"})
  public EinsteinEmbedding(int index, List<BigDecimal> embedding) {
    this.index = index;
    this.embedding = embedding;
  }

  public int getIndex() {
    return index;
  }

  public List<BigDecimal> getEmbedding() {
    return embedding;
  }
}
