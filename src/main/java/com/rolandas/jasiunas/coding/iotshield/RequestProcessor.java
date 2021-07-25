package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import com.rolandas.jasiunas.coding.iotshield.models.device.Device;
import com.rolandas.jasiunas.coding.iotshield.models.device.DeviceProfile;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import javax.inject.Inject;

public class RequestProcessor {

  private final DeviceManager deviceManager;

  @Inject
  public RequestProcessor(DeviceManager deviceManager) {
    this.deviceManager = deviceManager;
  }

  public RequestProcessor buildReplayableProcessor() {
    DeviceManager managerWithProfiles = deviceManager.withCopyOfCurrentProfileState();
    return new RequestProcessor(managerWithProfiles);
  }

  public int getProtectedDevices() {
    return deviceManager.getProtectedDevices();
  }

  public ActionType handleRequest(RequestEvent event) {
    Device device = deviceManager.getOrRegisterDevice(event.getDeviceId(), event.getModelName());
    if (device.isActive()) {
      return filterRequestByProfile(event);
    }

    return ActionType.BLOCK;
  }

  private ActionType filterRequestByProfile(RequestEvent event) {
    return deviceManager
        .getDeviceProfile(event.getModelName())
        .map(deviceProfile -> filterRequestByProfile(deviceProfile, event))
        .orElse(ActionType.ALLOW);
  }

  private ActionType filterRequestByProfile(DeviceProfile deviceProfile, RequestEvent event) {
    switch (deviceProfile.getDefaultPolicy()) {
      case ALLOW:
        return deviceProfile.getBlacklist().matches(event).orElse(ActionType.ALLOW);
      case BLOCK:
        return deviceProfile
            .getWhitelist()
            .matches(event)
            .orElseGet(
                () -> {
                  deviceManager.quarantineDevice(event.getDeviceId());
                  return ActionType.QUARANTINE;
                });
    }

    throw new IllegalStateException("Unknown device profile");
  }
}
