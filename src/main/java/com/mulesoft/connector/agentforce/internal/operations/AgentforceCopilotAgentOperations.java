package com.mulesoft.connector.agentforce.internal.operations;

import com.mulesoft.connector.agentforce.internal.connection.AgentforceConnection;
import com.mulesoft.connector.agentforce.internal.error.provider.ChatErrorTypeProvider;
import com.mulesoft.connector.agentforce.internal.helpers.PayloadHelper;
import com.mulesoft.connector.agentforce.internal.models.CopilotAgentDetails;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static java.lang.String.format;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

public class AgentforceCopilotAgentOperations {

  private static final Logger log = LoggerFactory.getLogger(AgentforceCopilotAgentOperations.class);

  PayloadHelper payloadHelper = new PayloadHelper();

  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("copilot-start-session")
  @Throws(ChatErrorTypeProvider.class)
  public String startSession(@Connection AgentforceConnection connection,
                             @ParameterGroup(name = "Additional properties") CopilotAgentDetails copilotAgentDetails)
      throws IOException {

    System.out.println("copilotAgentDetails = " + copilotAgentDetails);
    Map<String, String> botNameIdMap = payloadHelper.getAgentMetadata(connection);
    System.out.println(botNameIdMap);
    //return payloadHelper.createSession("0XxdL00000041WzSAI", connection);
    return payloadHelper.startSession(botNameIdMap.get(copilotAgentDetails.getAgent()), connection);

  }

  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("copilot-continue-session")
  @Throws(ChatErrorTypeProvider.class)
  public String continueSession(@Content(primary = true) String body, @Content String sessionId,
                                @Connection AgentforceConnection connection)
      throws IOException {


    return payloadHelper.continueSession(body, sessionId, connection);

  }

  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("copilot-end-session")
  @Throws(ChatErrorTypeProvider.class)
  public String endSession(@Content String sessionId, @Connection AgentforceConnection connection)
      throws IOException {

    return payloadHelper.endSession(sessionId, connection);

  }
}
