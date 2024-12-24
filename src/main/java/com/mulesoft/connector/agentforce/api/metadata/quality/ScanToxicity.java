package com.mulesoft.connector.agentforce.api.metadata.quality;

import java.util.List;

public class ScanToxicity {

  private boolean isDetected;
  private List<Categories> categories;

  public void setIsDetected(boolean isDetected) {
    this.isDetected = isDetected;
  }

  public void setCategories(List<Categories> categories) {
    this.categories = categories;
  }

  public boolean getIsDetected() {
    return isDetected;
  }

  public List<Categories> getCategories() {
    return categories;
  }

}
