package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.actions.ActionType;
import com.rolandas.jasiunas.coding.iotshield.actions.DeviceAction;
import com.rolandas.jasiunas.coding.iotshield.actions.RequestAction;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import com.rolandas.jasiunas.coding.iotshield.output.OutputFileWriter;

public class ResponseExecutor {

  private static final ResponseExecutor INSTANCE = new ResponseExecutor();

  private ResponseExecutor() {}

  public static ResponseExecutor getInstance() {
    return INSTANCE;
  }

  public void allow(RequestEvent request) {
    allow(request, null);
  }

  public void allow(RequestEvent request, String details) {
    RequestAction action = new RequestAction(request.getDeviceId(), ActionType.ALLOW);

    OutputFileWriter.getInstance().write(action);
  }

  public void block(RequestEvent request) {
    block(request, null);
  }

  public void block(RequestEvent request, String details) {
    RequestAction action = new RequestAction(request.getRequestId(), ActionType.BLOCK);

    OutputFileWriter.getInstance().write(action);
  }

  public void quarantine(String deviceId) {
    DeviceAction action = new DeviceAction(deviceId, ActionType.QUARANTINE);

    OutputFileWriter.getInstance().write(action);
  }
}
