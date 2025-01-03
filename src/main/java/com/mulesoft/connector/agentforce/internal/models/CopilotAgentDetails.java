package com.mulesoft.connector.agentforce.internal.models;

import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyPart;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class CopilotAgentDetails {

  @Parameter
  @MetadataKeyPart(order = 1)
  @DisplayName("Agent List")
  @Optional(defaultValue = "TestAgent")
  private String agent;

  public String getAgent() {
    return agent;
  }

  public void setAgent(String agent) {
    this.agent = agent;
  }

  @Override
  public String toString() {
    return "CopilotAgentDetails{" +
        "agent='" + agent + '\'' +
        '}';
  }
}
