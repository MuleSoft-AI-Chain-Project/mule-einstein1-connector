package com.mule.einstein.internal.helpers;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.mule.einstein.internal.helpers.ConstantUtil.*;



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

  public static String executeREST(String accessToken, String payload, String urlString) {

    try {
      HttpURLConnection httpConnection = creteURLConnection(urlString);
      populateConnectionObject(httpConnection, accessToken);

      try (OutputStream os = httpConnection.getOutputStream()) {
        byte[] input = payload.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
      }

      int responseCode = httpConnection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        if (httpConnection.getInputStream() == null) {
          return "Error: No response received from Einstein";
        }
        return readResponse(httpConnection.getInputStream());
      } else {
        String errorMessage = readErrorStream(httpConnection.getErrorStream());
        log.error("Error response code: {}, message: {}", responseCode, errorMessage);
        return String.format("Error: %d", responseCode);
      }
    } catch (Exception e) {
      log.error("Exception during REST request execution ", e);
      return "Exception occurred: " + e.getMessage();
    }
  }

  public static String getAccessToken(String org, String consumerKey, String consumerSecret) {

    String urlString = RequestHelper.getOAuthURL(org);
    String params = RequestHelper.getOAuthParams(consumerKey, consumerSecret);

    try {
      HttpURLConnection httpConnection = creteURLConnection(urlString);
      httpConnection.setRequestProperty(ConstantUtil.CONTENT_TYPE_STRING, CONTENT_TYPE_X_WWW_FORM_URLENCODED);

      try (OutputStream os = httpConnection.getOutputStream()) {
        byte[] input = params.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
      }

      int responseCode = httpConnection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        String response = readResponse(httpConnection.getInputStream());
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getString(ACCESS_TOKEN);
      } else {
        String errorMessage = readErrorStream(httpConnection.getErrorStream());
        log.error("Error response code: {}, message: {}", responseCode, errorMessage);
        return String.format("Error: %d", responseCode);
      }
    } catch (Exception e) {
      log.error("Exception while getting access token ", e);
      return "Exception occurred: " + e.getMessage();
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

  private static void populateConnectionObject(HttpURLConnection conn, String accessToken) throws IOException {
    conn.setRequestProperty(AUTHORIZATION, "Bearer " + accessToken);
    conn.setRequestProperty(REQUEST_PROPERTY_X_SFDC_APP_CONTEXT, APP_CONTEXT_EINSTEIN_GPT);
    conn.setRequestProperty(REQUEST_PROPERTY_X_CLIENT_FEATURE_ID, FEATURE_AI_PLATFORM_MODELS_CONNECTED_APP);
    conn.setRequestProperty(ConstantUtil.CONTENT_TYPE_STRING, CONTENT_TYPE_APPLICATION_JSON);
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
      log.error("Error reading error stream", e);
      return "Unable to get response from Einstein. Could not read reading error details as well.";
    }
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
}
