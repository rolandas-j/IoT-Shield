package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.models.events.DeviceEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileLifecycleEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import java.util.LinkedList;
import java.util.List;

public class IotShield {

  private final List<DeviceEvent> events = new LinkedList<>();
  private DeviceManager deviceManager;
  private RequestProcessor requestProcessor;

  public IotShield() {
    deviceManager = DeviceManager.getInstance();
    requestProcessor = RequestProcessor.getInstance();
  }

  public void consumeEvent(DeviceEvent deviceEvent) {
    if (deviceEvent instanceof ProfileLifecycleEvent) {
      deviceManager.handleProfileLifecycleEvent((ProfileLifecycleEvent) deviceEvent);
      return;
    }
    if (deviceEvent instanceof RequestEvent) {
      requestProcessor.handleRequest((RequestEvent) deviceEvent);
      return;
    }
    throw new IllegalArgumentException(
        String.format("Unknown device event received: %s", deviceEvent.getClass().getName()));
  }

  public List<DeviceEvent> getEvents() {
    return events;
  }
}
