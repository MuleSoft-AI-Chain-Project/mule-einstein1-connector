package com.mulesoft.connector.agentforce.internal.helpers;

public class ConstantUtil {

  public static final String URI_HTTPS_PREFIX = "https://";
  public static final String URI_MODELS_API = "/einstein/platform/v1/models/";
  public static final String URI_OAUTH_TOKEN = "/services/oauth2/token";
  public static final String URI_MODELS_API_EMBEDDINGS = "/embeddings";
  public static final String URI_MODELS_API_GENERATIONS = "/generations";
  public static final String URI_MODELS_API_CHAT_GENERATIONS = "/chat-generations";

  public static final String HTTP_METHOD_POST = "POST";

  public static final String CONTENT_TYPE_STRING = "Content-Type";
  public static final String CONTENT_TYPE_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
  public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json;charset=utf-8";

  public static final String QUERY_PARAM_GRANT_TYPE = "grant_type";
  public static final String QUERY_PARAM_CLIENT_ID = "client_id";
  public static final String QUERY_PARAM_CLIENT_SECRET = "client_secret";

  public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
  public static final String ACCESS_TOKEN = "access_token";
  public static final String AUTHORIZATION = "Authorization";

  public static final String X_SFDC_APP_CONTEXT = "x-sfdc-app-context";
  public static final String X_CLIENT_FEATURE_ID = "x-client-feature-id";
  public static final String AI_PLATFORM_MODELS_CONNECTED_APP = "ai-platform-models-connected-app";
  public static final String EINSTEIN_GPT = "EinsteinGPT";

  public static final Integer CONNECTION_TIMEOUT = 15000;
  public static final Integer READ_TIMEOUT = 20000;

  public static final String MODELAPI_OPENAI_ADA_002 = "sfdc_ai__DefaultOpenAITextEmbeddingAda_002";
  public static final String MODELAPI_OPENAI_GPT_3_5_TURBO = "sfdc_ai__DefaultOpenAIGPT35Turbo";

}
