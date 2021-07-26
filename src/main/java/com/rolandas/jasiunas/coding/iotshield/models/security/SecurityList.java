package com.rolandas.jasiunas.coding.iotshield.models.security;

import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class SecurityList {
  private final Set<String> securedUrls;
  private final ActionType listAction;

  public SecurityList(Set<String> securedUrls, ActionType listAction) {
    this.securedUrls = securedUrls;
    this.listAction = listAction;
  }

  public Optional<ActionType> matches(RequestEvent event) {
    return matches(event.getUrl());
  }

  public Optional<ActionType> matches(String url) {
    boolean contains = securedUrls.stream().anyMatch(url::equals);
    if (contains) {
      return Optional.ofNullable(listAction);
    }

    return Optional.empty();
  }

  public Set<String> getSecuredUrls() {
    return securedUrls;
  }

  public ActionType getListAction() {
    return listAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecurityList that = (SecurityList) o;
    return Objects.equals(securedUrls, that.securedUrls) && listAction == that.listAction;
  }

  @Override
  public int hashCode() {
    return Objects.hash(securedUrls, listAction);
  }
}
