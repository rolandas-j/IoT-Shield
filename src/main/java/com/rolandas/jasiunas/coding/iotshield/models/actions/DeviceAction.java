package com.rolandas.jasiunas.coding.iotshield.models.actions;

import java.util.Objects;

public class DeviceAction extends Action {
  private final String deviceId;

  public static DeviceAction quarantine(String deviceId) {
    return new DeviceAction(deviceId, ActionType.QUARANTINE);
  }

  public DeviceAction(String deviceId, ActionType action) {
    super(action);
    this.deviceId = deviceId;
  }

  public String getDeviceId() {
    return deviceId;
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
    DeviceAction that = (DeviceAction) o;
    return Objects.equals(deviceId, that.deviceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), deviceId);
  }

  @Override
  public String toString() {
    return "DeviceAction{" + "deviceId='" + deviceId + '\'' + "} " + super.toString();
  }
}
