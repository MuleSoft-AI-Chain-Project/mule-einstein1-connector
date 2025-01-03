package com.mulesoft.connector.agentforce.internal.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulesoft.connector.agentforce.internal.dto.OAuthResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.AI_PLATFORM_MODELS_CONNECTED_APP;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.AUTHORIZATION;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.CONNECTION_TIMEOUT;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.CONTENT_TYPE_APPLICATION_JSON;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.EINSTEIN_GPT;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.GRANT_TYPE_CLIENT_CREDENTIALS;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.HTTP_METHOD_POST;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.QUERY_PARAM_CLIENT_ID;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.QUERY_PARAM_CLIENT_SECRET;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.QUERY_PARAM_GRANT_TYPE;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.READ_TIMEOUT;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.URI_HTTPS_PREFIX;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.URI_OAUTH_TOKEN;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.X_CLIENT_FEATURE_ID;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.X_SFDC_APP_CONTEXT;


public class RequestHelper {

  private static final Logger log = LoggerFactory.getLogger(RequestHelper.class);

  public static String getOAuthURL(String salesforceOrg) {
    return URI_HTTPS_PREFIX + salesforceOrg + URI_OAUTH_TOKEN;
  }

  public static String getOAuthParams(String clientId, String clientSecret) {
    return QUERY_PARAM_GRANT_TYPE + "=" + GRANT_TYPE_CLIENT_CREDENTIALS
        + "&" + QUERY_PARAM_CLIENT_ID + "=" + clientId
        + "&" + QUERY_PARAM_CLIENT_SECRET + "=" + clientSecret;
  }


  public static String executeREST(String accessToken, String payload, String urlString) throws IOException {

    HttpURLConnection httpConnection = creteURLConnection(urlString);
    populateConnectionObject(httpConnection, accessToken);
    try (OutputStream os = httpConnection.getOutputStream()) {
      byte[] input = payload.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }
    log.info("Executing rest {} ", urlString);
    System.out.println("Executing rest " + urlString);
    int responseCode = httpConnection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      if (httpConnection.getInputStream() == null) {
        return "Error: No response received from Agentforce";
      }
      return readResponse(httpConnection.getInputStream());
    } else {
      String errorMessage = readErrorStream(httpConnection.getErrorStream());
      log.debug("Error response code: {}, message: {}", responseCode, errorMessage);
      return String.format("Error: %d", responseCode);
    }
  }

  public static String executeStartSession(String accessToken, String payload, String xorgId, String urlString)
      throws IOException {

    HttpURLConnection httpConnection = creteURLConnection(urlString);
    populateConnectionObject2(httpConnection, accessToken, xorgId);
    try (OutputStream os = httpConnection.getOutputStream()) {
      byte[] input = payload.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }
    log.info("Executing rest {} ", urlString);
    System.out.println("Executing rest " + urlString);
    int responseCode = httpConnection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      if (httpConnection.getInputStream() == null) {
        return "Error: No response received from Agentforce";
      }
      return readResponse(httpConnection.getInputStream());
    } else {
      String errorMessage = readErrorStream(httpConnection.getErrorStream());
      log.debug("Error response code: {}, message: {}", responseCode, errorMessage);
      System.out.println("Error response code: responseCode = " + responseCode + ", errorMessage = " + errorMessage);
      return String.format("Error: %d", responseCode);
    }
  }

  public static String getAgentList(String accessToken, String urlString) throws IOException {
    HttpURLConnection httpConnection = creteURLConnection2(urlString);
    populateConnectionObject3(httpConnection, accessToken);
    log.info("Executing rest {} ", urlString);
    int responseCode = httpConnection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      if (httpConnection.getInputStream() == null) {
        return "Error: No response received from Agentforce";
      }
      return readResponse(httpConnection.getInputStream());
    } else {
      String errorMessage = readErrorStream(httpConnection.getErrorStream());
      log.debug("Error response code: {}, message: {}", responseCode, errorMessage);
      System.out.println("Error response code: responseCode = " + responseCode + ", errorMessage = " + errorMessage);
      return String.format("Error: %d", responseCode);
    }
  }

  public static String executeContinueSession(String accessToken, String payload, String xorgId, String urlString)
      throws IOException {
    HttpURLConnection httpConnection = creteURLConnection(urlString);
    populateConnectionObject2(httpConnection, accessToken, xorgId);
    try (OutputStream os = httpConnection.getOutputStream()) {
      byte[] input = payload.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }
    log.info("Executing rest {} ", urlString);
    int responseCode = httpConnection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      if (httpConnection.getInputStream() == null) {
        return "Error: No response received from Agentforce";
      }
      return readResponse(httpConnection.getInputStream());
    } else {
      String errorMessage = readErrorStream(httpConnection.getErrorStream());
      log.debug("Error response code: {}, message: {}", responseCode, errorMessage);
      System.out.println("Error response code: responseCode = " + responseCode + ", errorMessage = " + errorMessage);
      return String.format("Error: %d", responseCode);
    }
  }

  public static String executeEndSession(String accessToken, String xorgId, String urlString)
      throws IOException {
    HttpURLConnection httpConnection = creteURLConnectionForDelete(urlString);
    populateConnectionObjectForEndSession(httpConnection, accessToken, xorgId);
    log.info("Executing rest {} ", urlString);
    int responseCode = httpConnection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      if (httpConnection.getInputStream() == null) {
        return "Error: No response received from Agentforce";
      }
      return readResponse(httpConnection.getInputStream());
    } else {
      String errorMessage = readErrorStream(httpConnection.getErrorStream());
      log.debug("Error response code: {}, message: {}", responseCode, errorMessage);
      System.out.println("Error response code: responseCode = " + responseCode + ", errorMessage = " + errorMessage);
      return String.format("Error: %d", responseCode);
    }
  }

  public static OAuthResponseDTO getOAuthResponseDTO(String salesforceOrg, String clientId, String clientSecret)
      throws IOException {

    log.debug("Preparing request for connection for salesforce org:{}", salesforceOrg);

    String urlString = RequestHelper.getOAuthURL(salesforceOrg);
    String urlParameters = RequestHelper.getOAuthParams(clientId, clientSecret);
    HttpURLConnection httpConnection = creteURLConnection(urlString);

    try (OutputStream os = httpConnection.getOutputStream()) {
      byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }
    log.info("Executing rest {} ", urlString);
    int responseCode = httpConnection.getResponseCode();
    log.debug("Response code for connection request:{}", responseCode);
    if (responseCode == HttpURLConnection.HTTP_OK) {
      if (httpConnection.getInputStream() == null) {
        return null;
      }
      String response = readResponse(httpConnection.getInputStream());
      return new ObjectMapper().readValue(response, OAuthResponseDTO.class);
    }
    return null;
  }

  private static String readResponse(InputStream inputStream) throws IOException {
    try (java.io.BufferedReader br = new java.io.BufferedReader(
                                                                new java.io.InputStreamReader(inputStream,
                                                                                              StandardCharsets.UTF_8))) {
      StringBuilder response = new StringBuilder();
      String responseLine;
      while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
      }
      return response.toString();
    }
  }

  private static HttpURLConnection creteURLConnection(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod(HTTP_METHOD_POST);
    conn.setConnectTimeout(CONNECTION_TIMEOUT);
    conn.setReadTimeout(READ_TIMEOUT);
    conn.setDoOutput(true);
    return conn;
  }

  private static HttpURLConnection creteURLConnection2(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setConnectTimeout(CONNECTION_TIMEOUT);
    conn.setReadTimeout(READ_TIMEOUT);
    conn.setDoOutput(true);
    return conn;
  }

  private static HttpURLConnection creteURLConnectionForDelete(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("DELETE");
    conn.setConnectTimeout(CONNECTION_TIMEOUT);
    conn.setReadTimeout(READ_TIMEOUT);
    conn.setDoOutput(true);
    return conn;
  }

  private static void populateConnectionObject(HttpURLConnection conn, String accessToken) throws IOException {
    conn.setRequestProperty(AUTHORIZATION, "Bearer " + accessToken);
    conn.setRequestProperty(X_SFDC_APP_CONTEXT, EINSTEIN_GPT);
    conn.setRequestProperty(X_CLIENT_FEATURE_ID, AI_PLATFORM_MODELS_CONNECTED_APP);
    conn.setRequestProperty(ConstantUtil.CONTENT_TYPE_STRING, CONTENT_TYPE_APPLICATION_JSON);
  }


  private static void populateConnectionObject2(HttpURLConnection conn, String accessToken, String xorgId)
      throws IOException {
    conn.setRequestProperty(AUTHORIZATION, "Bearer " + accessToken);
    conn.setRequestProperty("X-Org-Id", xorgId);
    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
    conn.setRequestProperty("Accept", "application/json");
  }

  private static void populateConnectionObjectForEndSession(HttpURLConnection conn, String accessToken, String xorgId)
      throws IOException {
    conn.setRequestProperty(AUTHORIZATION, "Bearer " + accessToken);
    conn.setRequestProperty("X-Org-Id", xorgId);
    conn.setRequestProperty("Accept", "application/json");
    conn.setRequestProperty("X-Session-End-Reason", "UserRequest");
  }

  private static void populateConnectionObject3(HttpURLConnection conn, String accessToken) throws IOException {
    conn.setRequestProperty(AUTHORIZATION, "Bearer " + accessToken);
    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
  }

  private static String readErrorStream(InputStream errorStream) {
    if (errorStream == null) {
      return "No error details available.";
    }
    try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8))) {
      StringBuilder errorResponse = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        errorResponse.append(line.trim());
      }
      return errorResponse.toString();
    } catch (IOException e) {
      log.debug("Error reading error stream", e);
      return "Unable to get response from Agentforce. Could not read reading error details as well.";
    }
  }
}
