package com.mulesoft.connector.agentforce.internal.helpers.chatmemory;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ChatMemory {

  private final DB db;
  private final Map<Long, String> chatMap;

  public ChatMemory(String dbFile, String memoryName) {
    // Open or create the DB file
    db = DBMaker.fileDB(new File(dbFile))
        .transactionEnable()
        .fileLockDisable()
        .make();

    // Create or retrieve the chat map
    chatMap = db.hashMap(memoryName)
        .keySerializer(Serializer.LONG)
        .valueSerializer(Serializer.STRING)
        .createOrOpen();
  }

  public void addMessage(long messageId, String messageContent) {
    chatMap.put(messageId, messageContent);
    db.commit(); // Save changes
  }

  public int getMessageCount() {
    return chatMap.size();
  }

  public List<String> getAllMessagesByMessageIdAsc() {
    // Retrieve all messageIds and sort them in ascending order
    List<Long> messageIds = new ArrayList<>(chatMap.keySet());
    messageIds.sort(Comparator.naturalOrder());

    // Retrieve messages in ascending order of messageId
    List<String> messages = new ArrayList<>();
    for (long messageId : messageIds) {
      messages.add(chatMap.get(messageId));
    }
    return messages;
  }
}
