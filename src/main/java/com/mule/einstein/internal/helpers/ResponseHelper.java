package com.mule.einstein.internal.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mule.einstein.api.metadata.EinsteinResponseAttributes;
import com.mule.einstein.api.metadata.ResponseParameters;
import com.mule.einstein.api.response.EinsteinEmbeddingsResponse;
import com.mule.einstein.internal.dto.EinsteinEmbeddingResponseDTO;
import com.mule.einstein.internal.dto.EinsteinGenerationResponseDTO;
import org.json.JSONObject;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.extension.api.runtime.operation.Result;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.io.IOUtils.toInputStream;

public class ResponseHelper {

  public static Result<InputStream, Void> createEinsteinDefaultResponse(String response) {

    return Result.<InputStream, Void>builder()
        .output(toInputStream(response, StandardCharsets.UTF_8))
        .mediaType(MediaType.APPLICATION_JSON)
        .build();
  }

  public static Result<InputStream, EinsteinResponseAttributes> createEinsteinFormattedResponse(String response)
      throws JsonProcessingException {

    EinsteinGenerationResponseDTO responseDTO = new ObjectMapper().readValue(response, EinsteinGenerationResponseDTO.class);

    String generatedText =
        responseDTO.getGeneration() != null ? responseDTO.getGeneration().getGeneratedText() : "";

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("response", generatedText);

    return Result.<InputStream, EinsteinResponseAttributes>builder()
        .output(toInputStream(jsonObject.toString(), StandardCharsets.UTF_8))
        .attributes(mapResponseAttributes(responseDTO))
        .attributesMediaType(MediaType.APPLICATION_JSON)
        .mediaType(MediaType.APPLICATION_JSON)
        .build();
  }

  public static Result<EinsteinEmbeddingsResponse, ResponseParameters> createEinsteinEmbeddedResponse(String response)
      throws JsonProcessingException {

    EinsteinEmbeddingResponseDTO responseDTO = new ObjectMapper().readValue(response, EinsteinEmbeddingResponseDTO.class);

    return Result.<EinsteinEmbeddingsResponse, ResponseParameters>builder()
        .output(new EinsteinEmbeddingsResponse(responseDTO.getEmbeddings()))
        .attributes(mapEmbeddingResponseAttributes(responseDTO))
        .attributesMediaType(MediaType.APPLICATION_JSON)
        .mediaType(MediaType.APPLICATION_JSON)
        .build();
  }

  private static EinsteinResponseAttributes mapResponseAttributes(EinsteinGenerationResponseDTO responseDTO) {

    return new EinsteinResponseAttributes(
                                          responseDTO.getId(),
                                          responseDTO.getGeneration() != null ? responseDTO.getGeneration().getId() : null,
                                          responseDTO.getGeneration() != null ? responseDTO.getGeneration().getContentQuality()
                                              : null,
                                          responseDTO.getGeneration() != null ? responseDTO.getGeneration().getParameters()
                                              : null,
                                          responseDTO.getParameters());
  }

  private static ResponseParameters mapEmbeddingResponseAttributes(EinsteinEmbeddingResponseDTO responseDTO) {

    return new ResponseParameters(
                                  responseDTO.getParameters() != null ? responseDTO.getParameters().getTokenUsage() : null,
                                  responseDTO.getParameters() != null ? responseDTO.getParameters().getModel() : null,
                                  null,
                                  responseDTO.getParameters() != null ? responseDTO.getParameters().getObject() : null);
  }
}
