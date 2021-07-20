package com.rolandas.jasiunas.coding.iotshield;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.rolandas.jasiunas.coding.iotshield.models.events.DeviceEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {

  public static void main(String[] args) {

    final IotShield iotShield = new IotShield();

    readDeviceEvents(iotShield);

    iotShield.getEvents();
  }

  public static void readDeviceEvents(IotShield shield) {
    ObjectMapper objectMapper =
        new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module());

    InputStream inputStream = Main.class.getResourceAsStream("/input.json");
    InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    BufferedReader reader = new BufferedReader(streamReader);

    try {
      String line = reader.readLine();

      while (line != null) {
        try {
          DeviceEvent deviceEvent = objectMapper.readValue(line, DeviceEvent.class);
          shield.consumeEvent(deviceEvent);
        } catch (InvalidFormatException exception) {
          exception.printStackTrace();
        }
        line = reader.readLine();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
