package com.rolandas.jasiunas.coding.iotshield.output;

import com.rolandas.jasiunas.coding.iotshield.actions.DeviceAction;
import com.rolandas.jasiunas.coding.iotshield.actions.RequestAction;

public interface OutputWriter {
  void write(DeviceAction action);

  void write(RequestAction action);
}
