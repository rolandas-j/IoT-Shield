package com.rolandas.jasiunas.coding.iotshield.models.device;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Locale;

public enum DevicePolicy {
  ALLOW,
  BLOCK;

  @JsonCreator
  public static DevicePolicy create(String value) {
    return DevicePolicy.valueOf(value.toUpperCase(Locale.ROOT)); // FIXME fixme
  }
}
