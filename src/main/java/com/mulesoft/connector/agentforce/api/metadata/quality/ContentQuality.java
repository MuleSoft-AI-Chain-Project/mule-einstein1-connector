package com.mulesoft.connector.agentforce.api.metadata.quality;

public class ContentQuality {

  private ScanToxicity scanToxicity;

  public void setScanToxicity(ScanToxicity scanToxicity) {
    this.scanToxicity = scanToxicity;
  }

  public ScanToxicity getScanToxicity() {
    return scanToxicity;
  }
}
