package com.mulesoft.connector.agentforce.internal.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulesoft.connector.agentforce.internal.connection.AgentforceConnection;
import com.mulesoft.connector.agentforce.internal.dto.AgentforceEmbeddingResponseDTO;
import com.mulesoft.connector.agentforce.internal.dto.OAuthResponseDTO;
import com.mulesoft.connector.agentforce.internal.models.ParamsEmbeddingDocumentDetails;
import com.mulesoft.connector.agentforce.internal.models.ParamsEmbeddingModelDetails;
import com.mulesoft.connector.agentforce.internal.models.ParamsModelDetails;
import com.mulesoft.connector.agentforce.internal.models.RAGParamsModelDetails;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.URI_MODELS_API;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.URI_MODELS_API_CHAT_GENERATIONS;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.URI_MODELS_API_EMBEDDINGS;
import static com.mulesoft.connector.agentforce.internal.helpers.ConstantUtil.URI_MODELS_API_GENERATIONS;
import static com.mulesoft.connector.agentforce.internal.helpers.RequestHelper.executeREST;

public class PayloadHelper {

  private static final Logger log = LoggerFactory.getLogger(PayloadHelper.class);

  public String executeGenerateText(String prompt, AgentforceConnection connection, ParamsModelDetails paramDetails)
      throws IOException {
    String payload = constructJsonPayload(prompt, paramDetails.getLocale(), paramDetails.getProbability());
    OAuthResponseDTO accessTokeDTO = connection.getoAuthResponseDTO();
    return executeAgentforceRequest(accessTokeDTO, payload, paramDetails.getModelApiName(), URI_MODELS_API_GENERATIONS);
  }

  public String executeGenerateChat(String messages, AgentforceConnection connection, ParamsModelDetails paramDetails)
      throws IOException {
    String payload = constrcutJsonMessages(messages, paramDetails);
    OAuthResponseDTO accessTokeDTO = connection.getoAuthResponseDTO();
    return executeAgentforceRequest(accessTokeDTO, payload, paramDetails.getModelApiName(), URI_MODELS_API_CHAT_GENERATIONS);
  }

  public String executeGenerateEmbedding(String text, AgentforceConnection connection,
                                         ParamsEmbeddingModelDetails paramDetails)
      throws IOException {
    String payload = constructEmbeddingJSON(text);
    OAuthResponseDTO accessTokeDTO = connection.getoAuthResponseDTO();
    return executeAgentforceRequest(accessTokeDTO, payload, paramDetails.getModelApiName(), URI_MODELS_API_EMBEDDINGS);
  }

  public JSONArray embeddingFromFile(String filePath, AgentforceConnection connection,
                                     ParamsEmbeddingDocumentDetails embeddingDocumentDetails)
      throws IOException, SAXException, TikaException {

    List<String> corpus =
        createCorpusList(filePath, embeddingDocumentDetails.getFileType(), embeddingDocumentDetails.getOptionType());
    OAuthResponseDTO accessTokeDTO = connection.getoAuthResponseDTO();
    return new JSONArray(
                         getCorpusEmbeddings(embeddingDocumentDetails.getModelApiName(), corpus, accessTokeDTO));
  }

  public String executeRAG(String text, AgentforceConnection connection, RAGParamsModelDetails paramDetails) throws IOException {
    String payload = constructJsonPayload(text, paramDetails.getLocale(), paramDetails.getProbability());
    OAuthResponseDTO accessTokeDTO = connection.getoAuthResponseDTO();
    return executeAgentforceRequest(accessTokeDTO, payload, paramDetails.getModelApiName(), URI_MODELS_API_GENERATIONS);
  }

  public String executeTools(String originalPrompt, String prompt, String filePath, AgentforceConnection connection,
                             ParamsModelDetails paramDetails)
      throws IOException {
    String payload = constructJsonPayload(prompt, paramDetails.getLocale(), paramDetails.getProbability());
    OAuthResponseDTO accessTokeDTO = connection.getoAuthResponseDTO();
    String payloadOptional = constructJsonPayload(originalPrompt, paramDetails.getLocale(), paramDetails.getProbability());

    String intermediateAnswer =
        executeAgentforceRequest(accessTokeDTO, payload, paramDetails.getModelApiName(), URI_MODELS_API_GENERATIONS);

    String response =
        executeAgentforceRequest(accessTokeDTO, payloadOptional, paramDetails.getModelApiName(), URI_MODELS_API_GENERATIONS);
    List<String> findURL = extractUrls(intermediateAnswer);
    if (findURL != null) {
      JSONObject jsonObject = new JSONObject(intermediateAnswer);
      String generatedText = jsonObject.getJSONObject("generation").getString("generatedText");

      String ePayload = buildPayload(generatedText);

      response = getAttributes(findURL.get(0), filePath, extractPayload(ePayload));
      String finalPayload = constructJsonPayload("data: " + response + ", question: " + originalPrompt, paramDetails.getLocale(),
                                                 paramDetails.getProbability());
      response =
          executeAgentforceRequest(accessTokeDTO, finalPayload, paramDetails.getModelApiName(), URI_MODELS_API_GENERATIONS);

    }
    return response;
  }

  public JSONArray embeddingFileQuery(String prompt, String filePath, AgentforceConnection connection, String modelName,
                                      String fileType, String optionType)
      throws IOException, SAXException, TikaException {
    List<String> corpus = createCorpusList(filePath, fileType, optionType);
    String body = constructEmbeddingJSON(prompt);
    OAuthResponseDTO accessTokeDTO = connection.getoAuthResponseDTO();
    List<Double> embeddingList = getQueryEmbedding(accessTokeDTO, body, modelName);

    List<List<Double>> corpusEmbeddingList = getCorpusEmbeddings(modelName, corpus, accessTokeDTO);

    // Compare embeddings and rank results
    List<Double> similarityScores = new ArrayList<>();
    corpusEmbeddingList
        .forEach(corpusEmbedding -> similarityScores.add(
                                                         calculateCosineSimilarity(embeddingList, corpusEmbedding)));
    // Rank and print results
    List<String> results = rankAndPrintResults(corpus, similarityScores);

    // Convert results list to a JSONArray
    return new JSONArray(results);
  }

  private List<List<Double>> getCorpusEmbeddings(String modelName, List<String> corpus, OAuthResponseDTO accessTokeDTO)
      throws IOException {

    String embeddingResponse;
    String corpusBody;
    // Generate embeddings for the corpus
    List<List<Double>> corpusEmbeddings = new ArrayList<>();

    for (String text : corpus) {

      corpusBody = constructEmbeddingJSON(text);

      if (text != null && !text.isEmpty()) {
        embeddingResponse =
            executeAgentforceRequest(accessTokeDTO, constructEmbeddingJSON(corpusBody), modelName, URI_MODELS_API_EMBEDDINGS);

        AgentforceEmbeddingResponseDTO embeddingResponseDTO =
            new ObjectMapper().readValue(embeddingResponse, AgentforceEmbeddingResponseDTO.class);

        corpusEmbeddings.add(embeddingResponseDTO.getEmbeddings().get(0).getEmbedding());
      }
    }
    return corpusEmbeddings;
  }

  private List<Double> getQueryEmbedding(OAuthResponseDTO accessTokeDTO, String body, String modelName)
      throws IOException {

    String embeddingResponse = executeAgentforceRequest(accessTokeDTO, body, modelName, URI_MODELS_API_EMBEDDINGS);

    AgentforceEmbeddingResponseDTO embeddingResponseDTO =
        new ObjectMapper().readValue(embeddingResponse, AgentforceEmbeddingResponseDTO.class);

    return embeddingResponseDTO.getEmbeddings().get(0).getEmbedding();
  }

  private List<String> createCorpusList(String filePath, String fileType, String splitOption)
      throws IOException, SAXException, TikaException {
    List<String> corpus;
    if (splitOption.equals("FULL")) {
      corpus = Arrays.asList(splitFullDocument(filePath, fileType));
    } else {
      corpus = Arrays.asList(splitByType(filePath, fileType, splitOption));
    }
    return corpus;
  }

  private double calculateCosineSimilarity(List<Double> embeddingList, List<Double> corpusEmbedding) {

    double dotProduct = 0.0;
    double normA = 0.0;
    double normB = 0.0;
    for (int i = 0; i < embeddingList.size(); i++) {
      double a = embeddingList.get(i);
      double b = corpusEmbedding.get(i);
      dotProduct += a * b;
      normA += Math.pow(a, 2);
      normB += Math.pow(b, 2);
    }
    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
  }

  private List<String> rankAndPrintResults(List<String> corpus, List<Double> similarityScores) {

    if (!similarityScores.isEmpty() && !corpus.isEmpty()) {

      List<Integer> indices = IntStream
          .range(0, corpus.size())
          .boxed()
          .sorted((i, j) -> Double.compare(similarityScores.get(j),
                                           similarityScores.get(i)))
          .collect(Collectors.toList());

      return indices.stream()
          .map(index -> similarityScores.get(index) + " - " + corpus.get(index))
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  private String executeAgentforceRequest(OAuthResponseDTO accessTokenDTO, String payload, String modelName,
                                          String resource)
      throws IOException {

    String urlString = accessTokenDTO.getApiInstanceUrl() + URI_MODELS_API + modelName + resource;
    log.debug("Agentforce Request URL: {}", urlString);

    return executeREST(accessTokenDTO.getAccessToken(), payload, urlString);
  }

  private String getContentFromUrl(String urlString) throws IOException, SAXException, TikaException {
    BodyContentHandler handler = new BodyContentHandler(-1);
    Metadata metadata = new Metadata();
    InputStream inputstream = new URL(urlString).openStream();
    ParseContext pcontext = new ParseContext();

    Parser parser = new AutoDetectParser();
    parser.parse(inputstream, handler, metadata, pcontext);
    return handler.toString();
  }

  private String getContentFromFile(String filePath) throws IOException, SAXException, TikaException {
    BodyContentHandler handler = new BodyContentHandler(-1);
    Metadata metadata = new Metadata();
    FileInputStream inputstream = new FileInputStream(filePath);
    ParseContext pcontext = new ParseContext();

    AutoDetectParser parser = new AutoDetectParser();
    parser.parse(inputstream, handler, metadata, pcontext);
    return handler.toString();
  }

  private String getContentFromTxtFile(String filePath) throws IOException, SAXException, TikaException {
    BodyContentHandler handler = new BodyContentHandler(-1);
    Metadata metadata = new Metadata();
    FileInputStream inputstream = new FileInputStream(filePath);
    ParseContext pcontext = new ParseContext();

    TXTParser parser = new TXTParser();
    parser.parse(inputstream, handler, metadata, pcontext);
    return handler.toString();
  }

  private String getFileTypeContextFromFile(String filePath, String fileType)
      throws IOException, SAXException, TikaException {

    switch (fileType) {
      case "URL":
        return getContentFromUrl(filePath);
      case "PDF":
        return getContentFromFile(filePath);
      default:
        return getContentFromTxtFile(filePath);
    }
  }

  private String splitFullDocument(String filePath, String fileType) throws IOException, SAXException, TikaException {
    return getFileTypeContextFromFile(filePath, fileType);
  }

  private String[] splitByType(String filePath, String fileType, String splitOption)
      throws IOException, SAXException, TikaException {
    String content = getFileTypeContextFromFile(filePath, fileType);
    return splitContent(content, splitOption);
  }

  private String[] splitContent(String text, String option) {
    switch (option) {
      case "PARAGRAPH":
        return splitByParagraphs(text);
      case "SENTENCES":
        return splitBySentences(text);
      default:
        throw new IllegalArgumentException("Unknown split option: " + option);
    }
  }

  private String[] splitByParagraphs(String text) {
    // Assuming paragraphs are separated by two or more newlines

    return removeEmptyStrings(text.split("\\r?\\n\\r?\\n"));
  }

  private String[] splitBySentences(String text) {
    // Split by sentences (simple implementation using period followed by space)
    return removeEmptyStrings(text.split("(?<!Mr|Mrs|Ms|Dr|Sr|Jr|Prof)\\.\\s+"));
  }

  public String[] removeEmptyStrings(String[] array) {
    // Convert array to list
    List<String> list = new ArrayList<>(Arrays.asList(array));

    // Remove empty strings from the list
    list.removeIf(String::isEmpty);

    // Convert list back to array
    return list.toArray(new String[0]);
  }

  private List<String> extractUrls(String input) {
    // Define the URL pattern
    String urlPattern = "(https?://[\\w\\-\\.]+(?:\\.[\\w\\-]+)+(?:[\\w\\-.,@?^=%&:/~+#]*[\\w\\-@?^=%&/~+#])?)";

    // Compile the pattern
    Pattern pattern = Pattern.compile(urlPattern);

    // Create a matcher from the input string
    Matcher matcher = pattern.matcher(input);

    // Find and collect all matches
    List<String> urls = new ArrayList<>();
    while (matcher.find()) {
      // Group 1 contains the URL without trailing whitespace
      urls.add(matcher.group(1));
    }

    // Return null if no URLs are found
    return urls.isEmpty() ? null : urls;
  }

  private String constructJsonPayload(String prompt, String locale, Number probability) {
    JSONObject localization = new JSONObject();
    localization.put("defaultLocale", locale);

    JSONArray inputLocales = new JSONArray();
    JSONObject inputLocale = new JSONObject();
    inputLocale.put("locale", locale);
    inputLocale.put("probability", probability);
    inputLocales.put(inputLocale);
    localization.put("inputLocales", inputLocales);

    JSONArray expectedLocales = new JSONArray();
    expectedLocales.put(locale);
    localization.put("expectedLocales", expectedLocales);

    JSONObject jsonPayload = new JSONObject();
    jsonPayload.put("prompt", prompt);
    jsonPayload.put("localization", localization);
    jsonPayload.put("tags", new JSONObject());

    return jsonPayload.toString();
  }

  private String constrcutJsonMessages(String message, ParamsModelDetails paramsModelDetails) {
    JSONArray messages = new JSONArray(message);

    JSONObject locale = new JSONObject();
    locale.put("locale", paramsModelDetails.getLocale());
    locale.put("probability", paramsModelDetails.getProbability());

    JSONArray inputLocales = new JSONArray();
    inputLocales.put(locale);

    JSONArray expectedLocales = new JSONArray();
    expectedLocales.put(paramsModelDetails.getLocale());

    JSONObject localization = new JSONObject();
    localization.put("defaultLocale", paramsModelDetails.getLocale());
    localization.put("inputLocales", inputLocales);
    localization.put("expectedLocales", expectedLocales);

    JSONObject tags = new JSONObject();

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("messages", messages);
    jsonObject.put("localization", localization);
    jsonObject.put("tags", tags);

    return jsonObject.toString();
  }

  private String constructEmbeddingJSON(String text) {
    JSONArray input = new JSONArray();
    input.put(text);

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("input", input);

    return jsonObject.toString();
  }

  private String getAttributes(String url, String filePath, String payload) throws IOException {

    try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {

      JSONTokener tokener = new JSONTokener(inputStream);
      JSONArray rootArray = new JSONArray(tokener);
      String responseString = "";

      for (int i = 0; i < rootArray.length(); i++) {

        JSONObject node = rootArray.getJSONObject(i);

        if (node.getString("url").trim().equals(url)) {
          String method = node.getString("method");
          String headers = node.getString("headers");

          URL urlObj = new URL(url);
          HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

          conn.setRequestMethod(method);
          if (headers != null && !headers.isEmpty()) {
            conn.setRequestProperty("Authorization", headers);
          }
          conn.setRequestProperty(ConstantUtil.CONTENT_TYPE_STRING, "application/json; charset=UTF-8");
          conn.setDoOutput(true);

          if (method.equals("POST")) {
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
              wr.writeBytes(payload);
              wr.flush();
            }
          }
          int responseCode = conn.getResponseCode();

          BufferedReader in;
          if (responseCode >= 200 && responseCode < 300) {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          } else {
            in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
          }

          String inputLine;
          StringBuilder response = new StringBuilder();

          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();

          responseString = response.toString();
          break;
        }
      }
      return responseString;
    }
  }

  private String extractPayload(String payload) {

    Pattern pattern = Pattern.compile("\\{.*\\}");
    Matcher matcher = pattern.matcher(payload);
    String response;
    if (matcher.find()) {
      response = matcher.group();
    } else {
      response = "Payload not found!";
    }
    return response;
  }

  private String buildPayload(String payload) {

    String findPayload = extractPayload(payload);
    if (findPayload.equals("Payload not found!")) {
      return extractPayload(payload);
    }
    return findPayload;
  }
}
