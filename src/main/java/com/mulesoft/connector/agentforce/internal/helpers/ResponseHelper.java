package com.mulesoft.connector.agentforce.internal.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulesoft.connector.agentforce.api.metadata.AgentforceResponseAttributes;
import com.mulesoft.connector.agentforce.api.metadata.ResponseParameters;
import com.mulesoft.connector.agentforce.internal.dto.AgentforceChatFromMessagesResponseDTO;
import com.mulesoft.connector.agentforce.internal.dto.AgentforceEmbeddingResponseDTO;
import com.mulesoft.connector.agentforce.internal.dto.AgentforceGenerationResponseDTO;
import org.json.JSONObject;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.extension.api.runtime.operation.Result;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.io.IOUtils.toInputStream;

public class ResponseHelper {

  public static Result<InputStream, Void> createAgentforceDefaultResponse(String response) {

    return Result.<InputStream, Void>builder()
        .output(toInputStream(response, StandardCharsets.UTF_8))
        .mediaType(MediaType.APPLICATION_JSON)
        .build();
  }

  public static Result<InputStream, AgentforceResponseAttributes> createAgentforceFormattedResponse(String response)
      throws JsonProcessingException {

    AgentforceGenerationResponseDTO responseDTO = new ObjectMapper().readValue(response, AgentforceGenerationResponseDTO.class);

    String generatedText =
        responseDTO.getGeneration() != null ? responseDTO.getGeneration().getGeneratedText() : "";

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("response", generatedText);

    return Result.<InputStream, AgentforceResponseAttributes>builder()
        .output(toInputStream(jsonObject.toString(), StandardCharsets.UTF_8))
        .attributes(mapResponseAttributes(responseDTO))
        .attributesMediaType(MediaType.APPLICATION_JSON)
        .mediaType(MediaType.APPLICATION_JSON)
        .build();
  }

  public static Result<InputStream, ResponseParameters> createAgentforceChatFromMessagesResponse(String response)
      throws JsonProcessingException {

    AgentforceChatFromMessagesResponseDTO responseDTO =
        new ObjectMapper().readValue(response, AgentforceChatFromMessagesResponseDTO.class);

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("generations", responseDTO.getGenerationDetails().getGenerations());

    return Result.<InputStream, ResponseParameters>builder()
        .output(toInputStream(jsonObject.toString(), StandardCharsets.UTF_8))
        .attributes(responseDTO.getGenerationDetails().getParameters())
        .attributesMediaType(MediaType.APPLICATION_JSON)
        .mediaType(MediaType.APPLICATION_JSON)
        .build();
  }

  public static Result<InputStream, ResponseParameters> createAgentforceEmbeddingResponse(String response)
      throws JsonProcessingException {

    ObjectMapper objectMapper = new ObjectMapper();

    AgentforceEmbeddingResponseDTO responseDTO = objectMapper.readValue(response, AgentforceEmbeddingResponseDTO.class);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("embeddings", responseDTO.getEmbeddings());

    return Result.<InputStream, ResponseParameters>builder()
        .output(toInputStream(jsonObject.toString(), StandardCharsets.UTF_8))
        .attributes(mapEmbeddingResponseAttributes(responseDTO))
        .attributesMediaType(MediaType.APPLICATION_JSON)
        .mediaType(MediaType.APPLICATION_JSON)
        .build();
  }

  private static AgentforceResponseAttributes mapResponseAttributes(AgentforceGenerationResponseDTO responseDTO) {

    return new AgentforceResponseAttributes(
                                            responseDTO.getId(),
                                            responseDTO.getGeneration() != null ? responseDTO.getGeneration().getId() : null,
                                            responseDTO.getGeneration() != null ? responseDTO.getGeneration().getContentQuality()
                                                : null,
                                            responseDTO.getGeneration() != null ? responseDTO.getGeneration().getParameters()
                                                : null,
                                            responseDTO.getParameters());
  }

  private static ResponseParameters mapEmbeddingResponseAttributes(AgentforceEmbeddingResponseDTO responseDTO) {

    return new ResponseParameters(
                                  responseDTO.getParameters() != null ? responseDTO.getParameters().getTokenUsage() : null,
                                  responseDTO.getParameters() != null ? responseDTO.getParameters().getModel() : null,
                                  null,
                                  responseDTO.getParameters() != null ? responseDTO.getParameters().getObject() : null);
  }
}
