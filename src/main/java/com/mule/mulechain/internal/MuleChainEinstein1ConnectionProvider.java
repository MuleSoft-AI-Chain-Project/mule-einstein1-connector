package com.mule.mulechain.internal;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MuleChainEinstein1ConnectionProvider implements PoolingConnectionProvider<MuleChainEinstein1Connection> {

  private final Logger LOGGER = LoggerFactory.getLogger(MuleChainEinstein1ConnectionProvider.class);

  @Parameter
  private String clientId;

  @Parameter
  private String clientSecret;

  @Parameter
  private String salesforceOrg;

  @Override
  public MuleChainEinstein1Connection connect() throws ConnectionException {
    try {
      String urlStr = "https://" + salesforceOrg + "/services/oauth2/token";
      String urlParameters = "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
      byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

      URL url = new URL(urlStr);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.getOutputStream().write(postData);

      int responseCode = conn.getResponseCode();
      if (responseCode == 200) {
        return new MuleChainEinstein1Connection(salesforceOrg, clientId, clientSecret);
      } else {
        throw new ConnectionException("Failed to connect to Salesforce: HTTP " + responseCode);
      }
    } catch (IOException e) {
      throw new ConnectionException("Failed to connect to Salesforce", e);
    }
  }

  @Override
  public void disconnect(MuleChainEinstein1Connection connection) {
    try {
      connection.invalidate();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting [" + connection.getClientId() + "]: " + e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(MuleChainEinstein1Connection connection) {
    try {
      String urlStr = "https://" + connection.getSalesforceOrg() + "/services/oauth2/token";
      String urlParameters = "grant_type=client_credentials&client_id=" + connection.getClientId() + "&client_secret=" + connection.getClientSecret();
      byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

      URL url = new URL(urlStr);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.getOutputStream().write(postData);

      int responseCode = conn.getResponseCode();
      if (responseCode == 200) {
        return ConnectionValidationResult.success();
      } else {
        return ConnectionValidationResult.failure("Failed to validate connection: HTTP " + responseCode, null);
      }
    } catch (IOException e) {
      return ConnectionValidationResult.failure("Failed to validate connection", e);
    }
  }
}
