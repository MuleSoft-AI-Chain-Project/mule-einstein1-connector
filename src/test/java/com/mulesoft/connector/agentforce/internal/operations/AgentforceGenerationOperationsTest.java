package com.mulesoft.connector.agentforce.internal.operations;

import com.mulesoft.connector.agentforce.internal.connection.AgentforceConnection;
import com.mulesoft.connector.agentforce.internal.helpers.PayloadHelper;
import com.mulesoft.connector.agentforce.internal.helpers.chatmemory.ChatMemoryHelper;
import com.mulesoft.connector.agentforce.internal.models.ParamsModelDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.exception.ModuleException;

import java.io.IOException;

import static com.mulesoft.connector.agentforce.internal.error.AgentforceErrorType.CHAT_FAILURE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgentforceGenerationOperationsTest {

  @InjectMocks
  private AgentforceGenerationOperations agentforceGenerationOperations;

  @Mock
  private PayloadHelper payloadHelperMock;

  @Mock
  private ChatMemoryHelper chatMemoryHelperMock;

  @Mock
  private AgentforceConnection connectionMock;

  @Mock
  private ParamsModelDetails paramDetailsMock;

  @BeforeEach
  void setUp() {
    agentforceGenerationOperations.setPayloadHelper(payloadHelperMock);
    agentforceGenerationOperations.setChatMemoryHelper(chatMemoryHelperMock);
  }

  @Test
  public void testDefinePromptTemplateFailure() throws IOException, ConnectionException {
    String template = "Template";
    String instructions = "Instructions";
    String dataset = "Dataset";
    when(payloadHelperMock.executeGenerateText(anyString(), any(), any()))
        .thenThrow(new RuntimeException("Test exception"));

    ModuleException exception = assertThrows(ModuleException.class,
                                             () -> agentforceGenerationOperations.definePromptTemplate(template, instructions,
                                                                                                       dataset, connectionMock,
                                                                                                       paramDetailsMock));

    assertEquals("Error while generating prompt from template Template, instructions Instructions, dataset Dataset",
                 exception.getMessage());
    assertEquals(CHAT_FAILURE, exception.getType());
  }

  @Test
  public void testGenerateTextFailure() throws IOException, ConnectionException {
    String prompt = "Test Prompt";

    when(payloadHelperMock.executeGenerateText(anyString(), any(), any()))
        .thenThrow(new RuntimeException("Test exception"));

    ModuleException exception =
        assertThrows(ModuleException.class,
                     () -> agentforceGenerationOperations.generateText(prompt, connectionMock, paramDetailsMock));

    assertEquals(
                 "Error while generating text for prompt Test Prompt",
                 exception.getMessage());
    assertEquals(CHAT_FAILURE, exception.getType());
  }

  @Test
  public void testGenerateTextMemoryFailure() throws IOException, ConnectionException {
    String prompt = "Test";
    String memoryPath = "src/resources/testdb";
    String memoryName = "vt";
    Integer keepLastMessages = 10;

    when(chatMemoryHelperMock.chatWithMemory(anyString(), anyString(), anyString(), anyInt(), any(), any(), any()))
        .thenThrow(new RuntimeException("Test exception"));

    ModuleException exception = assertThrows(ModuleException.class, () -> agentforceGenerationOperations
        .generateTextMemory(prompt, memoryPath, memoryName, keepLastMessages, connectionMock, paramDetailsMock));

    assertEquals(
                 "Error while generating text from memory path src/resources/testdb, memory name vt, for prompt Test",
                 exception.getMessage());
    assertEquals(CHAT_FAILURE, exception.getType());
  }

  @Test
  public void testGenerateChatFailure() throws IOException, ConnectionException {
    String messages = "Test Messages";

    when(payloadHelperMock.executeGenerateChat(anyString(), any(), any()))
        .thenThrow(new RuntimeException("Test exception"));

    ModuleException exception =
        assertThrows(ModuleException.class,
                     () -> agentforceGenerationOperations.generateChat(messages, connectionMock, paramDetailsMock));

    assertEquals(
                 "Error while generating the chat from messages Test Messages",
                 exception.getMessage());
    assertEquals(CHAT_FAILURE, exception.getType());
  }
}

