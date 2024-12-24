package com.mulesoft.connector.agentforce.internal.models.provider;

import java.util.Set;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

public class EmbeddingModelApiNameProvider implements ValueProvider {

  private static final Set<Value> VALUES_FOR = ValueBuilder.getValuesFor(
                                                                         "sfdc_ai__DefaultAzureOpenAITextEmbeddingAda_002",
                                                                         "sfdc_ai__DefaultOpenAITextEmbeddingAda_002");

  @Override
  public Set<Value> resolve() throws ValueResolvingException {

    return VALUES_FOR;
  }

}
