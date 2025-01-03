package com.mulesoft.connector.agentforce.internal;

import com.mulesoft.connector.agentforce.internal.connection.AgentforceConnectionProvider;
import com.mulesoft.connector.agentforce.internal.operations.AgentforceCopilotAgentOperations;
import com.mulesoft.connector.agentforce.internal.operations.AgentforceGenerationOperations;
import com.mulesoft.connector.agentforce.internal.operations.AgentforceEmbeddingOperations;
import com.mulesoft.connector.agentforce.internal.error.AgentforceErrorType;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;
import org.mule.runtime.extension.api.annotation.license.RequiresEnterpriseLicense;
import org.mule.sdk.api.annotation.JavaVersionSupport;

import static org.mule.sdk.api.meta.JavaVersion.JAVA_11;
import static org.mule.sdk.api.meta.JavaVersion.JAVA_17;
import static org.mule.sdk.api.meta.JavaVersion.JAVA_8;

/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "ms-agentforce")
@Extension(name = "Anypoint Agentforce")
@ErrorTypes(AgentforceErrorType.class)
@Operations({AgentforceEmbeddingOperations.class, AgentforceGenerationOperations.class, AgentforceCopilotAgentOperations.class})
@ConnectionProviders(AgentforceConnectionProvider.class)
@RequiresEnterpriseLicense(allowEvaluationLicense = true)
@JavaVersionSupport({JAVA_8, JAVA_11, JAVA_17})
public class AgentforceConnector {

}
