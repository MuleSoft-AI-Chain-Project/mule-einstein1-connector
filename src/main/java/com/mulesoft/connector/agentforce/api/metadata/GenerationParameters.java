package com.mulesoft.connector.agentforce.api.metadata;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.beans.ConstructorProperties;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenerationParameters {

  private final String finishReason;
  private final Object refusal;
  private final int index;
  private final Object logprobs;

  @ConstructorProperties({"finshReason", "refusal", "index", "logprobs"})
  public GenerationParameters(String finishReason, Object refusal, int index, Object logprobs) {
    this.finishReason = finishReason;
    this.refusal = refusal;
    this.index = index;
    this.logprobs = logprobs;
  }

  public String getFinishReason() {
    return finishReason;
  }

  public Object getRefusal() {
    return refusal;
  }

  public int getIndex() {
    return index;
  }

  public Object getLogprobs() {
    return logprobs;
  }
}
