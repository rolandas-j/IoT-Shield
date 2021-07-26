package com.rolandas.jasiunas.coding.iotshield.models.security;

import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import java.util.Collections;
import java.util.Set;

public class Blacklist extends SecurityList {
  public static final Blacklist EMPTY = Blacklist.create(Collections.emptySet());

  public static Blacklist create(Set<String> securedUrls) {
    return new Blacklist(securedUrls);
  }

  private Blacklist(Set<String> securedUrls) {
    super(securedUrls, ActionType.BLOCK);
  }

  @Override
  public String toString() {
    return "Blacklist{"
        + "securedUrls="
        + getSecuredUrls()
        + ", listAction="
        + getListAction()
        + '}';
  }
}
