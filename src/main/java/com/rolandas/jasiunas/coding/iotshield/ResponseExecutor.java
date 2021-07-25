package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import com.rolandas.jasiunas.coding.iotshield.models.actions.DeviceAction;
import com.rolandas.jasiunas.coding.iotshield.models.actions.RequestAction;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import com.rolandas.jasiunas.coding.iotshield.output.OutputWriter;
import javax.inject.Inject;

public class ResponseExecutor {

  private final OutputWriter outputWriter;
  private final StatisticCollector statisticCollector;

  @Inject
  public ResponseExecutor(OutputWriter outputWriter, StatisticCollector statisticCollector) {
    this.outputWriter = outputWriter;
    this.statisticCollector = statisticCollector;
  }

  public void executeResponse(RequestEvent requestEvent, ActionType actionType) {
    switch (actionType) {
      case ALLOW:
        RequestAction allowRequestAction = RequestAction.allow(requestEvent.getRequestId());
        outputWriter.write(allowRequestAction);
        break;
      case QUARANTINE: // On quarantine action we also execute block for request
        DeviceAction deviceAction = DeviceAction.quarantine(requestEvent.getDeviceId());
        outputWriter.write(deviceAction);
      case BLOCK:
        RequestAction blockRequestAction = RequestAction.block(requestEvent.getRequestId());
        outputWriter.write(blockRequestAction);
        break;
    }
    statisticCollector.collectEventStatistics(requestEvent, actionType);
  }
}
