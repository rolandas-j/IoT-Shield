package com.rolandas.jasiunas.coding.iotshield.models.device;

public class Device {

  private final String id;
  private final String modelName;
  private final DeviceStatus status;

  public Device(String id, String modelName, DeviceStatus status) {
    this.id = id;
    this.modelName = modelName;
    this.status = status;
  }

  public static Device createDevice(String deviceId, String modelName) {
    return new Device(deviceId, modelName, DeviceStatus.ACTIVE);
  }

  public Device activate() {
    return new Device(id, modelName, DeviceStatus.ACTIVE);
  }

  public String getId() {
    return id;
  }

  public DeviceStatus getStatus() {
    return status;
  }

  public String getModelName() {
    return modelName;
  }

  public Device quarantine() {
    return new Device(getId(), getModelName(), DeviceStatus.IN_QUARANTINE);
  }

  public boolean isActive() {
    return status == DeviceStatus.ACTIVE;
  }

  public boolean inQuarantine() {
    return status == DeviceStatus.IN_QUARANTINE;
  }
}
