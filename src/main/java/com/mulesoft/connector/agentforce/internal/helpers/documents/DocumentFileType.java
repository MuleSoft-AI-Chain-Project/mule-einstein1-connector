package com.mulesoft.connector.agentforce.internal.helpers.documents;

import java.util.Set;

import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

public class DocumentFileType implements ValueProvider {

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    return ValueBuilder.getValuesFor("PDF", "TEXT", "CSV", "URL");
  }

}
