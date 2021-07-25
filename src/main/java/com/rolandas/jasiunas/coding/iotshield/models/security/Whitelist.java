package com.rolandas.jasiunas.coding.iotshield.models.security;

import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import java.util.Collections;
import java.util.Set;

public class Whitelist extends SecurityList {

  public static final Whitelist EMPTY = Whitelist.create(Collections.emptySet());

  public static Whitelist create(Set<String> securedUrls) {
    return new Whitelist(securedUrls);
  }

  private Whitelist(Set<String> securedUrls) {
    super(securedUrls, ActionType.ALLOW);
  }
}
