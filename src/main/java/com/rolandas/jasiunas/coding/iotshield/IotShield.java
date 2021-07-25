package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.input.InputProcessor;
import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import com.rolandas.jasiunas.coding.iotshield.models.events.DeviceEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileLifecycleEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import com.rolandas.jasiunas.coding.iotshield.models.statistics.StatisticsSummary;
import com.rolandas.jasiunas.coding.iotshield.output.OutputWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;

public class IotShield {

  private final List<DeviceEvent> events = new LinkedList<>();
  private final DeviceManager deviceManager;
  private final RequestProcessor requestProcessor;
  private final ResponseExecutor responseExecutor;
  private final InputProcessor inputProcessor;
  private final OutputWriter outputWriter;
  private final StatisticCollector statisticCollector;

  @Inject
  public IotShield(
      DeviceManager deviceManager,
      RequestProcessor requestProcessor,
      ResponseExecutor responseExecutor,
      InputProcessor inputProcessor,
      OutputWriter outputWriter,
      StatisticCollector statisticCollector) {
    this.deviceManager = deviceManager;
    this.requestProcessor = requestProcessor;
    this.responseExecutor = responseExecutor;
    this.inputProcessor = inputProcessor;
    this.outputWriter = outputWriter;
    this.statisticCollector = statisticCollector;
  }

  public void start() throws Exception {
    try (OutputWriter ignored = outputWriter.initialize()) {
      List<DeviceEvent> events = inputProcessor.readEvents();

      events.forEach(this::consumeEvent);
    } catch (Exception ex) {
      if (ex instanceof IOException) {
        throw new RuntimeException("Unable to close output writer", ex);
      }
      throw ex;
    }

    RequestProcessor replayRequestProcessor = requestProcessor.buildReplayableProcessor();
    StatisticsSummary statistics = statisticCollector.calculateStatistics(replayRequestProcessor);

    System.out.println(statistics.buildStatisticsString());
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
    responseExecutor.executeResponse(requestEvent, actionType);
  }

  public List<DeviceEvent> getEvents() {
    return events;
  }
}
