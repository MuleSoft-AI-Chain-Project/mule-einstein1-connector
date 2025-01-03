package com.mulesoft.connector.agentforce.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentMetadataResponse {

  @JsonProperty("totalSize")
  private int totalSize;

  @JsonProperty("done")
  private boolean done;

  @JsonProperty("records")
  private List<BotRecord> records;

  public int getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(int totalSize) {
    this.totalSize = totalSize;
  }

  public boolean isDone() {
    return done;
  }

  public void setDone(boolean done) {
    this.done = done;
  }

  public List<BotRecord> getRecords() {
    return records;
  }

  public void setRecords(List<BotRecord> records) {
    this.records = records;
  }
}
