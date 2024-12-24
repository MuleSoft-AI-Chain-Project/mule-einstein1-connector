package com.mulesoft.connector.agentforce.internal.operations;

import com.mulesoft.connector.agentforce.api.metadata.AgentforceResponseAttributes;
import com.mulesoft.connector.agentforce.api.metadata.ResponseParameters;
import com.mulesoft.connector.agentforce.internal.connection.AgentforceConnection;
import com.mulesoft.connector.agentforce.internal.error.provider.EmbeddingErrorTypeProvider;
import com.mulesoft.connector.agentforce.internal.helpers.PayloadHelper;
import com.mulesoft.connector.agentforce.internal.helpers.ResponseHelper;
import com.mulesoft.connector.agentforce.internal.models.ParamsEmbeddingDocumentDetails;
import com.mulesoft.connector.agentforce.internal.models.ParamsEmbeddingModelDetails;
import com.mulesoft.connector.agentforce.internal.models.ParamsModelDetails;
import com.mulesoft.connector.agentforce.internal.models.RAGParamsModelDetails;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static com.mulesoft.connector.agentforce.internal.error.AgentforceErrorType.EMBEDDING_OPERATIONS_FAILURE;
import static com.mulesoft.connector.agentforce.internal.error.AgentforceErrorType.RAG_FAILURE;
import static com.mulesoft.connector.agentforce.internal.error.AgentforceErrorType.TOOLS_OPERATION_FAILURE;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.MODELAPI_OPENAI_ADA_002;
import static java.lang.String.format;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class AgentforceEmbeddingOperations {

  private static final Logger log = LoggerFactory.getLogger(AgentforceEmbeddingOperations.class);
  PayloadHelper payloadHelper = new PayloadHelper();

  /**
   * Create an embedding vector representing the input text.
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("EMBEDDING-generate-from-text")
  @Throws(EmbeddingErrorTypeProvider.class)
  public Result<InputStream, ResponseParameters> generateEmbeddingFromText(@Content String text,
                                                                           @Connection AgentforceConnection connection,
                                                                           @ParameterGroup(
                                                                               name = "Additional properties") ParamsEmbeddingModelDetails paramDetails) {
    try {
      String response = payloadHelper.executeGenerateEmbedding(text, connection, paramDetails);

      return ResponseHelper.createAgentforceEmbeddingResponse(response);
    } catch (Exception e) {
      throw new ModuleException("Error while executing embedding generate from text operation",
                                EMBEDDING_OPERATIONS_FAILURE, e);
    }
  }

  /**
   * Create an embedding vector representing the input file .
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("EMBEDDING-generate-from-file")
  @Throws(EmbeddingErrorTypeProvider.class)
  public Result<InputStream, Void> generateEmbeddingFromFile(String filePath, @Connection AgentforceConnection connection,
                                                             @ParameterGroup(
                                                                 name = "Additional properties") ParamsEmbeddingDocumentDetails paramDetails) {
    try {
      JSONArray response = payloadHelper.embeddingFromFile(filePath, connection, paramDetails);

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("result", response);

      return ResponseHelper.createAgentforceDefaultResponse(jsonObject.toString());
    } catch (Exception e) {
      throw new ModuleException("Error while executing embedding generate from file operation",
                                EMBEDDING_OPERATIONS_FAILURE, e);
    }
  }

  /**
   * Generate a response based on a file embedding.
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("EMBEDDING-adhoc-file-query")
  @Throws(EmbeddingErrorTypeProvider.class)
  public Result<InputStream, Void> queryEmbeddingOnFiles(@Content String prompt, String filePath,
                                                         @Connection AgentforceConnection connection,
                                                         @ParameterGroup(
                                                             name = "Additional properties") ParamsEmbeddingDocumentDetails paramDetails) {
    log.info("Executing embedding adhoc file query operation.");
    try {
      JSONArray response = payloadHelper.embeddingFileQuery(prompt, filePath, connection, paramDetails.getModelApiName(),
                                                            paramDetails.getFileType(), paramDetails.getOptionType());

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("result", response);
      return ResponseHelper.createAgentforceDefaultResponse(jsonObject.toString());
    } catch (Exception e) {

      log.error(format("Exception occurred while executing embedding adhoc file query operation %s", e.getMessage()), e);
      throw new ModuleException("Error while generating the chat from filePath " + filePath + ", for prompt " + prompt,
                                EMBEDDING_OPERATIONS_FAILURE, e);
    }
  }

  /**
   * Generate a response based on a plain text prompt and file from embedding and LLM.
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("RAG-adhoc-load-document")
  @Throws(EmbeddingErrorTypeProvider.class)
  public Result<InputStream, AgentforceResponseAttributes> ragOnFiles(@Content String prompt, String filePath,
                                                                      @Connection AgentforceConnection connection,
                                                                      @ParameterGroup(
                                                                          name = "Additional properties") RAGParamsModelDetails paramDetails) {
    log.info("Executing rag adhoc load document.");
    try {

      String content = payloadHelper.embeddingFileQuery(prompt, filePath, connection, paramDetails.getEmbeddingName(),
                                                        paramDetails.getFileType(), paramDetails.getOptionType())
          .toString();
      String response = payloadHelper.executeRAG("data: " + content + ", question: " + prompt, connection,
                                                 paramDetails);

      return ResponseHelper.createAgentforceFormattedResponse(response);
    } catch (Exception e) {

      log.error(format("Exception occurred while executing rag adhoc load document operation %s", e.getMessage()), e);
      throw new ModuleException("Error while doing rag adhoc load document from filePath " + filePath + ", for prompt "
          + prompt, RAG_FAILURE, e);
    }
  }

  /**
   * Generate a response based on a plain text prompt and tools config.
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("Tools-use-ai-service")
  @Throws(EmbeddingErrorTypeProvider.class)
  public Result<InputStream, AgentforceResponseAttributes> executeTools(@Content String prompt, String toolsConfig,
                                                                        @Connection AgentforceConnection connection,
                                                                        @ParameterGroup(
                                                                            name = "Additional properties") ParamsModelDetails paramDetails) {
    log.info("Executing AI service tools operation.");
    try {

      String content =
          payloadHelper.embeddingFileQuery(prompt, toolsConfig, connection, MODELAPI_OPENAI_ADA_002, "text", "FULL")
              .toString();
      String response = payloadHelper.executeTools(prompt, "data: " + content + ", question: " + prompt,
                                                   toolsConfig, connection, paramDetails);

      return ResponseHelper.createAgentforceFormattedResponse(response);
    } catch (Exception e) {

      log.error(format("Exception occurred while executing AI service tools operation %s", e.getMessage()), e);
      throw new ModuleException("Error while executing AI service tools with provided config " + toolsConfig + ", for prompt "
          + prompt, TOOLS_OPERATION_FAILURE, e);
    }
  }
}
