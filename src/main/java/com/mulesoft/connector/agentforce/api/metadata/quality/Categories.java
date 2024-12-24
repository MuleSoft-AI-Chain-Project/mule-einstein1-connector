package com.mulesoft.connector.agentforce.api.metadata.quality;

public class Categories {

  private String categoryName;
  private String score;

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public void setScore(String score) {
    this.score = score;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public String getScore() {
    return score;
  }
}
