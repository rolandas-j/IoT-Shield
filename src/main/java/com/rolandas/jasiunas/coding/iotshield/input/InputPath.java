package com.rolandas.jasiunas.coding.iotshield.input;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;

@Documented
@Retention(RUNTIME)
@Qualifier
public @interface InputPath {}
