package com.rolandas.jasiunas.coding.iotshield.models.actions;

import java.util.Objects;

public class RequestAction extends Action {
  private final String requestId;

  public static RequestAction block(String requestId) {
    return new RequestAction(requestId, ActionType.BLOCK);
  }

  public static RequestAction allow(String requestId) {
    return new RequestAction(requestId, ActionType.ALLOW);
  }

  public RequestAction(String requestId, ActionType action) {
    super(action);
    this.requestId = requestId;
  }

  public String getRequestId() {
    return requestId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    RequestAction that = (RequestAction) o;
    return Objects.equals(requestId, that.requestId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), requestId);
  }

  @Override
  public String toString() {
    return "RequestAction{" + "requestId=" + requestId + ", action=" + getAction() + '}';
  }
}
