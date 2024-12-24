package com.mulesoft.connector.agentforce.internal.models.provider;

import java.util.Set;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

public class ModelApiNameProvider implements ValueProvider {

  private static final Set<Value> VALUES_FOR = ValueBuilder.getValuesFor(
                                                                         "sfdc_ai__DefaultBedrockAnthropicClaude3Haiku",
                                                                         "sfdc_ai__DefaultAzureOpenAIGPT35Turbo",
                                                                         "sfdc_ai__DefaultAzureOpenAIGPT35Turbo_16k",
                                                                         "sfdc_ai__DefaultAzureOpenAIGPT4Turbo",
                                                                         "sfdc_ai__DefaultOpenAIGPT35Turbo",
                                                                         "sfdc_ai__DefaultOpenAIGPT35Turbo_16k",
                                                                         "sfdc_ai__DefaultOpenAIGPT4",
                                                                         "sfdc_ai__DefaultOpenAIGPT4_32k",
                                                                         "sfdc_ai__DefaultGPT4Omni",
                                                                         "sfdc_ai__DefaultOpenAIGPT4OmniMini",
                                                                         "sfdc_ai__DefaultOpenAIGPT4Turbo");

  @Override
  public Set<Value> resolve() throws ValueResolvingException {

    return VALUES_FOR;
  }
}
