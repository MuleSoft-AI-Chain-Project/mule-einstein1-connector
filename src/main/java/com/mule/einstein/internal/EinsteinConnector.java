package com.mule.einstein.internal;

import com.mule.einstein.internal.connection.EinsteinConnectionProvider;
import com.mule.einstein.internal.operations.EinsteinGenerationOperations;
import com.mule.einstein.internal.operations.EinsteinEmbeddingOperations;
import com.mule.einstein.internal.error.EinsteinErrorType;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;
import org.mule.runtime.extension.api.annotation.license.RequiresEnterpriseLicense;
import org.mule.sdk.api.annotation.JavaVersionSupport;

import static org.mule.sdk.api.meta.JavaVersion.JAVA_11;
import static org.mule.sdk.api.meta.JavaVersion.JAVA_17;
import static org.mule.sdk.api.meta.JavaVersion.JAVA_8;

/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "ms-einstein")
@Extension(name = "Einstein AI")
@ErrorTypes(EinsteinErrorType.class)
@Operations({EinsteinEmbeddingOperations.class, EinsteinGenerationOperations.class})
@ConnectionProviders(EinsteinConnectionProvider.class)
@RequiresEnterpriseLicense(allowEvaluationLicense = true)
@JavaVersionSupport({JAVA_8, JAVA_11, JAVA_17})
public class EinsteinConnector {

}
