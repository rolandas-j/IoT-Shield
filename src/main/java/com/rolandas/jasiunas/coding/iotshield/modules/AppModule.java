package com.rolandas.jasiunas.coding.iotshield.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rolandas.jasiunas.coding.iotshield.DeviceManager;
import com.rolandas.jasiunas.coding.iotshield.RequestProcessor;
import com.rolandas.jasiunas.coding.iotshield.ResponseExecutor;
import com.rolandas.jasiunas.coding.iotshield.StatisticCollector;
import com.rolandas.jasiunas.coding.iotshield.output.FileOutputWriter;
import com.rolandas.jasiunas.coding.iotshield.output.OutputPath;
import com.rolandas.jasiunas.coding.iotshield.output.OutputWriter;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public interface AppModule {
  @Provides
  @Singleton
  static DeviceManager provideDeviceManager() {
    return new DeviceManager();
  }

  @Provides
  @Singleton
  static RequestProcessor provideRequestProcessor(DeviceManager deviceManager) {
    return new RequestProcessor(deviceManager);
  }

  @Provides
  @Singleton
  static ResponseExecutor provideResponseExecutor(
      OutputWriter outputWriter, StatisticCollector statisticCollector) {
    return new ResponseExecutor(outputWriter, statisticCollector);
  }

  @Provides
  @Singleton
  static OutputWriter outputWriter(@OutputPath String outputPath, ObjectMapper objectMapper) {
    return new FileOutputWriter(outputPath, objectMapper);
  }

  @Provides
  @Singleton
  static StatisticCollector provideStatisticsCollector() {
    return new StatisticCollector();
  }
}
