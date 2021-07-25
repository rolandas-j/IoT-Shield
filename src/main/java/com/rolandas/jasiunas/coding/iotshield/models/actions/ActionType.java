package com.rolandas.jasiunas.coding.iotshield.models.actions;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ActionType {
  ALLOW("allow"),
  BLOCK("block"),
  QUARANTINE("quarantine");

  private final String value;

  ActionType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
