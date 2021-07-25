package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.models.BlacklistProperty;
import com.rolandas.jasiunas.coding.iotshield.models.WhitelistProperty;
import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import com.rolandas.jasiunas.coding.iotshield.models.device.Device;
import com.rolandas.jasiunas.coding.iotshield.models.device.DeviceProfile;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import java.util.Set;
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
        return filterByBlacklist(deviceProfile.getBlacklist(), event);
      case BLOCK:
        return filterByWhitelist(deviceProfile.getWhitelist(), event);
    }

    throw new IllegalStateException("Unknown device profile");
  }

  private ActionType filterByBlacklist(Set<BlacklistProperty> blacklist, RequestEvent event) {
    boolean blacklisted =
        blacklist.stream()
            .anyMatch(
                blacklistProperty ->
                    blacklistProperty
                        .getValue()
                        .equals(event.getUrl())); // FIXME this is blacklist logic, should be moved
    if (blacklisted) {
      return ActionType.BLOCK;
    }

    return ActionType.ALLOW;
  }

  private ActionType filterByWhitelist(Set<WhitelistProperty> whitelist, RequestEvent event) {
    boolean whitelisted =
        whitelist.stream()
            .anyMatch(
                whitelistProperty ->
                    whitelistProperty
                        .getValue()
                        .equals(event.getUrl())); // FIXME this is whitelist logic, should be moved

    if (whitelisted) {
      return ActionType.ALLOW;
    }

    deviceManager.quarantineDevice(event.getDeviceId());
    return ActionType.QUARANTINE;
  }
}
