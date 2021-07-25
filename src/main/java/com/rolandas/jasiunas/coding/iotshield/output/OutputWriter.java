package com.rolandas.jasiunas.coding.iotshield.output;

public interface OutputWriter extends AutoCloseable {
  OutputWriter initialize();

  void write(Object object);
}
