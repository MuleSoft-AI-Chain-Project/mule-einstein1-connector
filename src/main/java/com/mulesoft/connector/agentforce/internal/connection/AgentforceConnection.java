package com.mulesoft.connector.agentforce.internal.connection;

import com.mulesoft.connector.agentforce.internal.dto.OAuthResponseDTO;

/**
 * This class represents a connection to the external system.
 */
public class AgentforceConnection {

  private final String salesforceOrg;
  private final String clientId;
  private final String clientSecret;
  private final OAuthResponseDTO oAuthResponseDTO;

  public AgentforceConnection(String salesforceOrg, String clientId, String clientSecret, OAuthResponseDTO oAuthResponseDTO) {
    this.salesforceOrg = salesforceOrg;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.oAuthResponseDTO = oAuthResponseDTO;
  }

  public String getSalesforceOrg() {
    return salesforceOrg;
  }

  public String getClientId() {
    return clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public OAuthResponseDTO getoAuthResponseDTO() {
    return oAuthResponseDTO;
  }

  public void invalidate() {
    // Add logic to invalidate the connection if necessary
  }
}
