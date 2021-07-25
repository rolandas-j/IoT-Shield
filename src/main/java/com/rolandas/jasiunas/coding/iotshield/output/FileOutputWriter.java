package com.rolandas.jasiunas.coding.iotshield.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.inject.Inject;

public class FileOutputWriter implements OutputWriter {

  private final String outputPath;
  private final ObjectMapper objectMapper;
  private BufferedWriter bufferedWriter;
  private FileWriter fileWriter;

  @Inject
  public FileOutputWriter(@OutputPath String outputPath, ObjectMapper objectMapper)
      throws RuntimeException {
    this.outputPath = outputPath;
    this.objectMapper = objectMapper;
  }

  @Override
  public OutputWriter initialize() throws RuntimeException {
    try {
      File file = new File(outputPath);
      FileWriter fileWriter = new FileWriter(file, false);

      bufferedWriter = new BufferedWriter(fileWriter);
      this.fileWriter = fileWriter;
      return this;
    } catch (IOException ex) {
      throw new RuntimeException(String.format("Unable to open file: %s", outputPath), ex);
    }
  }

  @Override
  public void write(Object object) {
    try {
      String objectString = objectMapper.writeValueAsString(object);
      bufferedWriter.write(objectString);
      bufferedWriter.newLine();
    } catch (IOException ex) {
      throw new RuntimeException(String.format("Unable to write to file: %s", outputPath), ex);
    }
  }

  @Override
  public void close() throws Exception {
    bufferedWriter.close();
    fileWriter.close();
  }
}
