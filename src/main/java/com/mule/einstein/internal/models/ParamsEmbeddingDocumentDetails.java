package com.mule.einstein.internal.models;

import com.mule.einstein.internal.helpers.documents.DocumentFileType;
import com.mule.einstein.internal.helpers.documents.DocumentSplitOptions;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.values.OfValues;

public class ParamsEmbeddingDocumentDetails extends ParamsEmbeddingModelDetails {

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @OfValues(DocumentFileType.class)
  @Optional(defaultValue = "PDF")
  @DisplayName("File Type")
  private String fileType;

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @OfValues(DocumentSplitOptions.class)
  @Optional(defaultValue = "FULL")
  @DisplayName("Option Type")
  private String optionType;

  public String getFileType() {
    return fileType;
  }

  public String getOptionType() {
    return optionType;
  }
}
