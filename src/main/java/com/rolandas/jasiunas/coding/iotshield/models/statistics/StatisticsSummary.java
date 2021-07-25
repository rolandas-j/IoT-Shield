package com.rolandas.jasiunas.coding.iotshield.models.statistics;

import java.util.Objects;

public class StatisticsSummary {

  private final int protectedDevices;
  private final int hackedDevices;
  private final int missedBlocks;
  private final int incorrectBlocks;

  public StatisticsSummary(
      int protectedDevices, int hackedDevices, int missedBlocks, int incorrectBlocks) {
    this.protectedDevices = protectedDevices;
    this.hackedDevices = hackedDevices;
    this.missedBlocks = missedBlocks;
    this.incorrectBlocks = incorrectBlocks;
  }

  public String buildStatisticsString() {
    return String.format("Devices protected: %s %n", protectedDevices)
        + String.format("Suspected to be hacked: %s %n", hackedDevices)
        + String.format("Missed blocks: %s %n", missedBlocks)
        + String.format("Incorrect blocks: %s %n", incorrectBlocks);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticsSummary that = (StatisticsSummary) o;
    return protectedDevices == that.protectedDevices
        && hackedDevices == that.hackedDevices
        && missedBlocks == that.missedBlocks
        && incorrectBlocks == that.incorrectBlocks;
  }

  @Override
  public int hashCode() {
    return Objects.hash(protectedDevices, hackedDevices, missedBlocks, incorrectBlocks);
  }

  @Override
  public String toString() {
    return "StatisticsSummary{"
        + "protectedDevices="
        + protectedDevices
        + ", hackedDevices="
        + hackedDevices
        + ", missedBlocks="
        + missedBlocks
        + ", incorrectBlocks="
        + incorrectBlocks
        + '}';
  }
}
