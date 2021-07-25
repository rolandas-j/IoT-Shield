package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import com.rolandas.jasiunas.coding.iotshield.models.statistics.ReplaySummary;
import com.rolandas.jasiunas.coding.iotshield.models.statistics.StatisticsSummary;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class StatisticCollector {

  private final LinkedHashMap<RequestEvent, ActionType> requestEventActions;
  private final Set<String> quarantinedDevices;

  private RequestProcessor requestProcessor;

  public StatisticCollector() {
    requestEventActions = new LinkedHashMap<>();
    quarantinedDevices = new HashSet<>();
  }

  public void collectEventStatistics(RequestEvent requestEvent, ActionType actionType) {
    requestEventActions.put(requestEvent, actionType);
    if (actionType == ActionType.QUARANTINE) {
      quarantinedDevices.add(requestEvent.getDeviceId());
    }
  }

  public StatisticsSummary calculateStatistics(RequestProcessor requestProcessor) {
    this.requestProcessor = requestProcessor;

    ReplaySummary replaySummary = replayRequestsWithNewestDeviceProfile();

    return new StatisticsSummary(
        requestProcessor.getProtectedDevices(),
        quarantinedDevices.size(),
        replaySummary.getMissedBlocks(),
        replaySummary.getIncorrectBlocks());
  }

  public ReplaySummary replayRequestsWithNewestDeviceProfile() {
    return requestEventActions.entrySet().stream()
        .map(this::replayRequest)
        .reduce(ReplaySummary::add)
        .orElse(ReplaySummary.EMPTY);
  }

  private ReplaySummary replayRequest(Map.Entry<RequestEvent, ActionType> entry) {
    return replayRequest(entry.getKey(), entry.getValue());
  }

  private ReplaySummary replayRequest(RequestEvent requestEvent, ActionType initialOutcome) {
    ActionType newOutcome = requestProcessor.handleRequest(requestEvent);
    if (initialOutcome != newOutcome) {
      switch (newOutcome) {
        case ALLOW:
          return ReplaySummary.incorrectBlock();
        case QUARANTINE:
        case BLOCK:
          return ReplaySummary.missedBlock();
      }
    }
    return ReplaySummary.EMPTY;
  }
}
