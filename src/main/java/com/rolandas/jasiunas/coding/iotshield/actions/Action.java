package com.rolandas.jasiunas.coding.iotshield.actions;

public enum Action {
  ALLOW("allow"),
  BLOCK("block"),
  QUARANTINE("quarantine");

  private String value;

  Action(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
