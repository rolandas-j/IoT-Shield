package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.actions.ActionType;
import com.rolandas.jasiunas.coding.iotshield.models.events.DeviceEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileLifecycleEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import java.util.LinkedList;
import java.util.List;

public class IotShield {

  private final List<DeviceEvent> events = new LinkedList<>();
  private DeviceManager deviceManager;
  private RequestProcessor requestProcessor;
  private ResponseExecutor responseExecutor;

  public IotShield() {
    deviceManager = DeviceManager.getInstance();
    requestProcessor = RequestProcessor.getInstance();
    responseExecutor = ResponseExecutor.getInstance();
  }

  public void consumeEvent(DeviceEvent deviceEvent) {
    if (deviceEvent instanceof ProfileLifecycleEvent) {
      deviceManager.handleProfileLifecycleEvent((ProfileLifecycleEvent) deviceEvent);
      return;
    }
    if (deviceEvent instanceof RequestEvent) {
      RequestEvent requestEvent = (RequestEvent) deviceEvent;
      ActionType actionType = requestProcessor.handleRequest(requestEvent);
      handleResponseAction(actionType, requestEvent);
      return;
    }
    throw new IllegalArgumentException(
        String.format("Unknown device event received: %s", deviceEvent.getClass().getName()));
  }

  private void handleResponseAction(ActionType actionType, RequestEvent requestEvent) {
    switch (actionType) {
      case BLOCK:
        responseExecutor.block(requestEvent);
        break;
      case ALLOW:
        responseExecutor.allow(requestEvent);
        break;
      case QUARANTINE:
        deviceManager.quarantineDevice(requestEvent.getDeviceId());
        responseExecutor.quarantine(requestEvent.getDeviceId());
        break;
    }
  }

  public List<DeviceEvent> getEvents() {
    return events;
  }
}
