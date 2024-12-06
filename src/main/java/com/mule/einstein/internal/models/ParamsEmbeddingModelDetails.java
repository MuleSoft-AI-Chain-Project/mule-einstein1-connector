package com.mule.einstein.internal.models;

import com.mule.einstein.internal.models.provider.EmbeddingModelApiNameProvider;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.values.OfValues;

import static com.mule.einstein.internal.helpers.ConstantUtil.MODELAPI_OPENAI_ADA_002;

public class ParamsEmbeddingModelDetails {

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @OfValues(EmbeddingModelApiNameProvider.class)
  @Optional(defaultValue = MODELAPI_OPENAI_ADA_002)
  @DisplayName("Model API Name")
  private String modelApiName;

  public String getModelApiName() {
    return modelApiName;
  }
}
