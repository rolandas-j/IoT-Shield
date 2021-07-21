package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.actions.ActionType;
import com.rolandas.jasiunas.coding.iotshield.models.BlacklistProperty;
import com.rolandas.jasiunas.coding.iotshield.models.WhitelistProperty;
import com.rolandas.jasiunas.coding.iotshield.models.device.Device;
import com.rolandas.jasiunas.coding.iotshield.models.device.DeviceProfile;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import java.util.Set;

public class RequestProcessor {

  private static final RequestProcessor INSTANCE = new RequestProcessor();
  private DeviceManager deviceManager;

  private RequestProcessor() {
    deviceManager = DeviceManager.getInstance();
  }

  public static RequestProcessor getInstance() {
    return INSTANCE;
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
            .anyMatch(blacklistProperty -> blacklistProperty.getValue().equals(event.getUrl()));
    if (blacklisted) {
      return ActionType.BLOCK;
    }

    return ActionType.ALLOW;
  }

  private ActionType filterByWhitelist(Set<WhitelistProperty> whitelist, RequestEvent event) {
    boolean whitelisted =
        whitelist.stream()
            .anyMatch(whitelistProperty -> whitelistProperty.getValue().equals(event.getUrl()));

    if (whitelisted) {
      return ActionType.ALLOW;
    }

    return ActionType.QUARANTINE;
  }
}
