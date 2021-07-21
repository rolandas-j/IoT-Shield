package com.rolandas.jasiunas.coding.iotshield.actions;

import java.util.Objects;

public abstract class Action {
  private final ActionType action;

  public ActionType getAction() {
    return action;
  }

  public Action(ActionType action) {
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Action action1 = (Action) o;
    return action == action1.action;
  }

  @Override
  public int hashCode() {
    return Objects.hash(action);
  }

  @Override
  public String toString() {
    return "Action{" + "action=" + action + '}';
  }
}
