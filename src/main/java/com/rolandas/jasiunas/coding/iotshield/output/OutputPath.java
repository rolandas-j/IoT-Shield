package com.rolandas.jasiunas.coding.iotshield.output;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;

@Documented
@Retention(RUNTIME)
@Qualifier
public @interface OutputPath {}
