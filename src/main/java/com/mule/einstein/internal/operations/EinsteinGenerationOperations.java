package com.mule.einstein.internal.operations;

import com.mule.einstein.api.metadata.EinsteinResponseAttributes;
import com.mule.einstein.api.metadata.ResponseParameters;
import com.mule.einstein.internal.connection.EinsteinConnection;
import com.mule.einstein.internal.error.provider.ChatErrorTypeProvider;
import com.mule.einstein.internal.helpers.PayloadHelper;
import com.mule.einstein.internal.helpers.PromptTemplateHelper;
import com.mule.einstein.internal.helpers.ResponseHelper;
import com.mule.einstein.internal.helpers.chatmemory.ChatMemoryHelper;
import com.mule.einstein.internal.models.ParamsModelDetails;
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

import static com.mule.einstein.internal.error.EinsteinErrorType.CHAT_FAILURE;
import static java.lang.String.format;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class EinsteinGenerationOperations {

  private static final Logger log = LoggerFactory.getLogger(EinsteinGenerationOperations.class);
  PayloadHelper payloadHelper = new PayloadHelper();
  ChatMemoryHelper chatMemoryHelper = new ChatMemoryHelper();

  public void setPayloadHelper(PayloadHelper payloadHelper) {
    this.payloadHelper = payloadHelper;
  }

  public void setChatMemoryHelper(ChatMemoryHelper chatMemoryHelper) {
    this.chatMemoryHelper = chatMemoryHelper;
  }

  /**
   * Helps defining an AI Agent with a prompt template
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("AGENT-define-prompt-template")
  @Throws(ChatErrorTypeProvider.class)
  public Result<InputStream, EinsteinResponseAttributes> definePromptTemplate(@Content(primary = true) String template,
                                                                              @Content String instructions,
                                                                              @Content String dataset,
                                                                              @Connection EinsteinConnection connection,
                                                                              @ParameterGroup(
                                                                                  name = "Additional properties") ParamsModelDetails paramDetails) {
    log.info("Executing agent defined prompt template operation.");
    try {
      String finalPromptTemplate = PromptTemplateHelper.definePromptTemplate(template, instructions, dataset);
      String response = payloadHelper.executeGenerateText(finalPromptTemplate, connection, paramDetails);

      return ResponseHelper.createEinsteinFormattedResponse(response);
    } catch (Exception e) {

      log.error(format("Exception occurred while executing agent defined prompt template operation %s", e.getMessage()), e);
      throw new ModuleException("Error while generating prompt from template " + template + ", instructions "
          + instructions + ", dataset " + dataset, CHAT_FAILURE, e);
    }
  }

  /**
   * Generate a response based on the prompt provided.
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("CHAT-answer-prompt")
  @Throws(ChatErrorTypeProvider.class)
  public Result<InputStream, EinsteinResponseAttributes> generateText(@Content String prompt,
                                                                      @Connection EinsteinConnection connection,
                                                                      @ParameterGroup(
                                                                          name = "Additional properties") ParamsModelDetails paramDetails) {
    log.info("Executing chat answer prompt operation.");
    try {
      String response = payloadHelper.executeGenerateText(prompt, connection, paramDetails);

      return ResponseHelper.createEinsteinFormattedResponse(response);
    } catch (Exception e) {

      log.error(format("Exception occurred while executing chat answer prompt operation %s", e.getMessage()), e);
      throw new ModuleException("Error while generating text for prompt " + prompt, CHAT_FAILURE, e);
    }
  }

  /**
   * Generate a response based on the prompt using chat memory.
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("CHAT-answer-prompt-with-memory")
  @Throws(ChatErrorTypeProvider.class)
  public Result<InputStream, EinsteinResponseAttributes> generateTextMemory(@Content(primary = true) String prompt,
                                                                            String memoryPath,
                                                                            String memoryName,
                                                                            Integer keepLastMessages,
                                                                            @Connection EinsteinConnection connection,
                                                                            @ParameterGroup(
                                                                                name = "Additional properties") ParamsModelDetails paramDetails) {
    log.info("Executing chat answer prompt with memory operation.");
    try {

      String response = chatMemoryHelper.chatWithMemory(prompt, memoryPath, memoryName, keepLastMessages,
                                                        connection, paramDetails, payloadHelper);

      return ResponseHelper.createEinsteinFormattedResponse(response);
    } catch (Exception e) {
      log.error(format("Exception occurred while executing chat answer prompt with memory operation %s", e.getMessage()), e);
      throw new ModuleException("Error while generating text from memory path " + memoryPath + ", memory name "
          + memoryName + ", for prompt " + prompt, CHAT_FAILURE, e);
    }
  }

  /**
   * Generate a response based on a list of messages representing a chat conversation.
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("CHAT-generate-from-messages")
  @Throws(ChatErrorTypeProvider.class)
  public Result<InputStream, ResponseParameters> generateChat(@Content String messages, @Connection EinsteinConnection connection,
                                                              @ParameterGroup(
                                                                  name = "Additional properties") ParamsModelDetails paramDetails) {
    log.info("Executing chat generate from message operation.");
    try {

      String response = payloadHelper.executeGenerateChat(messages, connection, paramDetails);

      return ResponseHelper.createEinsteinChatFromMessagesResponse(response);
    } catch (Exception e) {

      log.error(format("Exception occurred while executing chat generate from message operation %s", e.getMessage()), e);
      throw new ModuleException("Error while generating the chat from messages " + messages, CHAT_FAILURE, e);
    }
  }

}
