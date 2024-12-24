package com.mulesoft.connector.agentforce.internal.error.provider;

import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.HashSet;
import java.util.Set;

import static com.mulesoft.connector.agentforce.internal.error.AgentforceErrorType.EMBEDDING_OPERATIONS_FAILURE;
import static com.mulesoft.connector.agentforce.internal.error.AgentforceErrorType.RAG_FAILURE;
import static com.mulesoft.connector.agentforce.internal.error.AgentforceErrorType.TOOLS_OPERATION_FAILURE;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

public class EmbeddingErrorTypeProvider implements ErrorTypeProvider {

  @SuppressWarnings("rawtypes")
  @Override
  public Set<ErrorTypeDefinition> getErrorTypes() {
    return unmodifiableSet(new HashSet<>(asList(EMBEDDING_OPERATIONS_FAILURE, RAG_FAILURE,
                                                TOOLS_OPERATION_FAILURE)));
  }
}
