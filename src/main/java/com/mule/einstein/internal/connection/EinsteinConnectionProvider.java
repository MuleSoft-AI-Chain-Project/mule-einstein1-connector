package com.mule.einstein.internal.connection;

import com.mule.einstein.internal.dto.OAuthResponseDTO;
import com.mule.einstein.internal.helpers.RequestHelper;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EinsteinConnectionProvider implements ConnectionProvider<EinsteinConnection> {

  private final Logger log = LoggerFactory.getLogger(EinsteinConnectionProvider.class);

  @Parameter
  @Placement(order = 1, tab = Placement.CONNECTION_TAB)
  @DisplayName("Salesforce Org URL")
  @Example("mydomain.my.salesforce.com")
  private String salesforceOrg;

  @Parameter
  @Placement(order = 2, tab = Placement.CONNECTION_TAB)
  @DisplayName("Client ID")
  private String clientId;

  @Password
  @Parameter
  @Placement(order = 3, tab = Placement.CONNECTION_TAB)
  @DisplayName("Client Secret")
  private String clientSecret;

  @Override
  public EinsteinConnection connect() throws ConnectionException {
    log.debug("Executing connect method call");
    try {
      OAuthResponseDTO oAuthResponseDTO = RequestHelper.getOAuthResponseDTO(salesforceOrg, clientId, clientSecret);
      if (oAuthResponseDTO != null) {
        return new EinsteinConnection(salesforceOrg, clientId, clientSecret, oAuthResponseDTO);
      } else {
        throw new ConnectionException("Failed to connect to Salesforce: HTTP ");
      }
    } catch (IOException e) {
      log.debug("Failed to connect to Salesforce: ", e);
      throw new ConnectionException("Failed to connect to Salesforce");
    }
  }

  @Override
  public void disconnect(EinsteinConnection connection) {
    log.debug("Executing disconnect method call");
    try {
      connection.invalidate();
    } catch (Exception e) {
      log.debug("Error while disconnecting [{}]: {}", connection.getClientId(), e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(EinsteinConnection einsteinConnection) {
    log.debug("Executing validate method call");
    try {
      OAuthResponseDTO oAuthResponseDTO =
          RequestHelper.getOAuthResponseDTO(einsteinConnection.getSalesforceOrg(), einsteinConnection.getClientId(),
                                            einsteinConnection.getClientSecret());
      if (oAuthResponseDTO != null) {
        return ConnectionValidationResult.success();
      } else {
        return ConnectionValidationResult.failure("Failed to validate connection: HTTP ", null);
      }
    } catch (IOException e) {
      log.debug("Failed to connect to Salesforce: ", e);
      return ConnectionValidationResult.failure("Failed to validate connection", e);
    }
  }
}
