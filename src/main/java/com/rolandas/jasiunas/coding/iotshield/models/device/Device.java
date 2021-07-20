package com.rolandas.jasiunas.coding.iotshield.models.device;

public class Device {

  public static Device createDevice(String deviceId) {
    return new Device(deviceId, DeviceStatus.ACTIVE);
  }

  private final String id;
  private final DeviceStatus status;

  public Device(String id, DeviceStatus status) {
    this.id = id;
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public DeviceStatus getStatus() {
    return status;
  }

  public Device quarantine() {
    return new Device(getId(), DeviceStatus.IN_QUARANTINE);
  }

  public boolean isActive() {
    return status == DeviceStatus.ACTIVE;
  }
}
