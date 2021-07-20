package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.actions.ActionDetails;
import com.rolandas.jasiunas.coding.iotshield.models.BlacklistProperty;
import com.rolandas.jasiunas.coding.iotshield.models.WhitelistProperty;
import com.rolandas.jasiunas.coding.iotshield.models.device.DeviceProfile;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import java.util.Set;

public class RequestProcessor {

  private static final RequestProcessor INSTANCE = new RequestProcessor();
  private DeviceManager deviceManager;
  private ResponseExecutor responseExecutor;

  private RequestProcessor() {

    deviceManager = DeviceManager.getInstance();
    responseExecutor = ResponseExecutor.getInstance();
  }

  public static RequestProcessor getInstance() {
    return INSTANCE;
  }

  public void handleRequest(RequestEvent event) {
    System.out.printf("Request got %s %n", event.getRequestId());

    deviceManager
        .getDeviceProfile(event.getModelName())
        .ifPresentOrElse(
            (deviceProfile) -> handleRequest(event, deviceProfile),
            () -> responseExecutor.allow(event, ActionDetails.DEVICE_HAS_NO_ACTIVE_PROFILE));
  }

  public void handleRequest(RequestEvent event, DeviceProfile deviceProfile) {
    if (deviceManager.isDeviceActive(event.getDeviceId())) {
      filterByProfile(deviceProfile, event);
      return;
    }

    responseExecutor.block(event, ActionDetails.DEVICE_IN_QUARANTINE);
  }

  private void filterByProfile(DeviceProfile deviceProfile, RequestEvent event) {
    switch (deviceProfile.getDefaultPolicy()) {
      case ALLOW:
        validateBlacklist(deviceProfile.getBlacklist(), event);
        break;
      case BLOCK:
        validateWhitelist(deviceProfile.getWhitelist(), event);
        break;
    }
  }

  private void validateBlacklist(Set<BlacklistProperty> blacklist, RequestEvent event) {
    boolean blacklisted =
        blacklist.stream()
            .anyMatch(blacklistProperty -> blacklistProperty.getValue().equals(event.getUrl()));
    if (blacklisted) {
      responseExecutor.block(event);
      return;
    }

    responseExecutor.allow(event);
  }

  private void validateWhitelist(Set<WhitelistProperty> whitelist, RequestEvent event) {
    boolean whitelisted =
        whitelist.stream()
            .anyMatch(whitelistProperty -> whitelistProperty.getValue().equals(event.getUrl()));

    if (whitelisted) {
      responseExecutor.allow(event);
      return;
    }

    deviceManager.quarantineDevice(event.getDeviceId());
    responseExecutor.quarantine(event.getDeviceId());
  }
}
