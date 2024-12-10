package com.mule.einstein.internal.models;

import com.mule.einstein.internal.models.provider.EmbeddingModelApiNameProvider;
import com.mule.einstein.internal.models.provider.ModelApiNameProvider;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.values.OfValues;

import com.mule.einstein.internal.helpers.documents.DocumentFileType;
import com.mule.einstein.internal.helpers.documents.DocumentSplitOptions;

import static com.mule.einstein.internal.helpers.ConstantUtil.MODELAPI_OPENAI_ADA_002;
import static com.mule.einstein.internal.helpers.ConstantUtil.MODELAPI_OPENAI_GPT_3_5_TURBO;

public class RAGParamsModelDetails {

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @OfValues(EmbeddingModelApiNameProvider.class)
  @Optional(defaultValue = MODELAPI_OPENAI_ADA_002)
  @DisplayName("Embedding Name")
  private String embeddingName;

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

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @OfValues(ModelApiNameProvider.class)
  @Optional(defaultValue = MODELAPI_OPENAI_GPT_3_5_TURBO)
  @DisplayName("Model API Name")
  private String modelApiName;

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional(defaultValue = "0.8")
  @DisplayName("Probability")
  private Number probability;

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional(defaultValue = "en_US")
  @DisplayName("Locale")
  private String locale;

  public String getEmbeddingName() {
    return embeddingName;
  }

  public String getFileType() {
    return fileType;
  }

  public String getOptionType() {
    return optionType;
  }

  public String getModelApiName() {
    return modelApiName;
  }

  public Number getProbability() {
    return probability;
  }

  public String getLocale() {
    return locale;
  }
}
