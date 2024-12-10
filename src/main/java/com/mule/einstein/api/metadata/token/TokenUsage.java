package com.mule.einstein.api.metadata.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.beans.ConstructorProperties;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenUsage {

  private final int inputCount;
  private final int outputCount;
  private final int totalCount;
  private final PromptTokensDetails promptTokensDetails;
  private final CompletionTokensDetails completionTokensDetails;

  @ConstructorProperties({"prompt_tokens", "completion_tokens", "total_tokens", "promptTokensDetails", "completionTokensDetails"})
  public TokenUsage(int inputCount, int outputCount, int totalCount, PromptTokensDetails promptTokensDetails,
                    CompletionTokensDetails completionTokensDetails) {
    this.inputCount = inputCount;
    this.outputCount = outputCount;
    this.totalCount = totalCount;
    this.promptTokensDetails = promptTokensDetails;
    this.completionTokensDetails = completionTokensDetails;
  }

  public int getInputCount() {
    return inputCount;
  }

  public int getOutputCount() {
    return outputCount;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public PromptTokensDetails getPromptTokenDetails() {
    return promptTokensDetails;
  }

  public CompletionTokensDetails getCompletionTokenDetails() {
    return completionTokensDetails;
  }
}
