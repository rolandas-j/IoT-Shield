package com.rolandas.jasiunas.coding.iotshield.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rolandas.jasiunas.coding.iotshield.actions.DeviceAction;
import com.rolandas.jasiunas.coding.iotshield.actions.RequestAction;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputFileWriter implements OutputWriter, AutoCloseable {

  private final BufferedWriter bufferedWriter;
  private final FileWriter fileWriter;
  private final ObjectMapper objectMapper = new ObjectMapper(); // FIXME

  private static OutputFileWriter INSTANCE;

  private OutputFileWriter(BufferedWriter bufferedWriter, FileWriter fileWriter) {
    this.bufferedWriter = bufferedWriter;
    this.fileWriter = fileWriter;
  }

  private static final String OUTPUT_FILE_NAME = "output.json";

  public static OutputFileWriter getInstance() {
    return INSTANCE;
  }

  public static OutputFileWriter initialize() throws IOException {
    File file = new File(OUTPUT_FILE_NAME);
    FileWriter fileWriter = new FileWriter(file, false);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

    INSTANCE = new OutputFileWriter(bufferedWriter, fileWriter);
    return INSTANCE;
  }

  @Override
  public void write(DeviceAction action) {
    writeToFile(action);
  }

  @Override
  public void write(RequestAction action) {
    writeToFile(action);
  }

  private void writeToFile(Object object) {
    try {
      String objectString = objectMapper.writeValueAsString(object);
      bufferedWriter.write(objectString);
      bufferedWriter.newLine();
    } catch (IOException e) {
      e.printStackTrace(); // FIXME
    }
  }

  @Override
  public void close() throws Exception {
    bufferedWriter.close();
    fileWriter.close();
  }
}
