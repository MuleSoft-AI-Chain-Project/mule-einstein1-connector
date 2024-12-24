package com.mulesoft.connector.agentforce.internal.error.provider;

import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.mulesoft.connector.agentforce.internal.error.AgentforceErrorType.CHAT_FAILURE;
import static java.util.Collections.unmodifiableSet;

public class ChatErrorTypeProvider implements ErrorTypeProvider {

  @SuppressWarnings("rawtypes")
  @Override
  public Set<ErrorTypeDefinition> getErrorTypes() {
    return unmodifiableSet(new HashSet<>(Collections.singletonList(CHAT_FAILURE)));
  }
}
