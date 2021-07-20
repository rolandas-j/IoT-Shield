package com.rolandas.jasiunas.coding.iotshield.models;

import java.util.Objects;

public class Property<T> {
  private final T value;

  public Property(T value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Property<?> property = (Property<?>) o;
    return Objects.equals(value, property.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "Property{" + "value=" + value + '}';
  }

  public T getValue() {
    return value;
  }
}
