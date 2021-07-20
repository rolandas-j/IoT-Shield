package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;

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
    System.out.println("Allow");
  }

  public void block(RequestEvent request) {
    block(request, null);
  }

  public void block(RequestEvent request, String details) {
    System.out.println("Block");
  }

  public void quarantine(String deviceId) {
    System.out.println("Quarantine");
  }
}
