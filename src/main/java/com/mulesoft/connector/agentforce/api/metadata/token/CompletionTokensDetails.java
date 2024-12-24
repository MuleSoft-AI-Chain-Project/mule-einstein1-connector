package com.mulesoft.connector.agentforce.api.metadata.token;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.beans.ConstructorProperties;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompletionTokensDetails {

  private final int reasoningTokens;
  private final int audioTokens;
  private final int acceptedPredictionTokens;
  private final int rejectedPredictionTokens;

  @ConstructorProperties({"reasoningTokens", "audioTokens", "acceptedPredictionTokens", "rejectedPredictionTokens"})
  public CompletionTokensDetails(int reasoningTokens, int audioTokens, int acceptedPredictionTokens,
                                 int rejectedPredictionTokens) {
    this.reasoningTokens = reasoningTokens;
    this.audioTokens = audioTokens;
    this.acceptedPredictionTokens = acceptedPredictionTokens;
    this.rejectedPredictionTokens = rejectedPredictionTokens;
  }

  public int getReasoningTokens() {
    return reasoningTokens;
  }

  public int getAudioTokens() {
    return audioTokens;
  }

  public int getAcceptedPredictionTokens() {
    return acceptedPredictionTokens;
  }

  public int getRejectedPredictionTokens() {
    return rejectedPredictionTokens;
  }
}
