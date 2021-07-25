package com.rolandas.jasiunas.coding.iotshield.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.rolandas.jasiunas.coding.iotshield.models.events.DeviceEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputProcessor {
  private static final Logger logger = LoggerFactory.getLogger(InputProcessor.class);

  private final String inputPath;
  private final ObjectMapper objectMapper;

  @Inject
  public InputProcessor(@InputPath String inputPath, ObjectMapper objectMapper) {
    this.inputPath = inputPath;
    this.objectMapper = objectMapper;
  }

  public List<DeviceEvent> readEvents() {
    InputStream inputStream = InputProcessor.class.getResourceAsStream(inputPath);
    if (inputStream == null) {
      throw new RuntimeException(String.format("Unable to find resource: %s", inputPath));
    }
    InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    BufferedReader reader = new BufferedReader(streamReader);

    LinkedList<DeviceEvent> deviceEvents = new LinkedList<>();
    try {
      String line = reader.readLine();

      while (line != null) {
        try {
          DeviceEvent deviceEvent = objectMapper.readValue(line, DeviceEvent.class);
          deviceEvents.add(deviceEvent);
        } catch (InvalidFormatException exception) {
          // For now we ignore unprocessable inputs, might be a good idea to try and salvage
          // some info from the input if possible and for example quarantine all the devices
          // if its a profile event, etc.
          logger.error("Unable to process input {}", line, exception);
        }
        line = reader.readLine();
      }
    } catch (IOException e) {
      throw new RuntimeException(String.format("Unable to find resource: %s", inputStream));
    }

    return deviceEvents;
  }
}
