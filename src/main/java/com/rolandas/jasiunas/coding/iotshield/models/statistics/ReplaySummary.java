package com.rolandas.jasiunas.coding.iotshield.models.statistics;

import java.util.Objects;

public class ReplaySummary {
  public static ReplaySummary missedBlock() {
    return new ReplaySummary(1, 0);
  }

  public static ReplaySummary incorrectBlock() {
    return new ReplaySummary(0, 1);
  }

  public static final ReplaySummary EMPTY = new ReplaySummary(0, 0);

  private final int missedBlocks;
  private final int incorrectBlocks;

  public ReplaySummary(int missedBlocks, int incorrectBlocks) {
    this.missedBlocks = missedBlocks;
    this.incorrectBlocks = incorrectBlocks;
  }

  public int getMissedBlocks() {
    return missedBlocks;
  }

  public int getIncorrectBlocks() {
    return incorrectBlocks;
  }

  public ReplaySummary add(ReplaySummary other) {
    return new ReplaySummary(
        missedBlocks + other.missedBlocks, incorrectBlocks + other.incorrectBlocks);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReplaySummary that = (ReplaySummary) o;
    return missedBlocks == that.missedBlocks && incorrectBlocks == that.incorrectBlocks;
  }

  @Override
  public int hashCode() {
    return Objects.hash(missedBlocks, incorrectBlocks);
  }

  @Override
  public String toString() {
    return "ReplaySummary{"
        + "missedBlocks="
        + missedBlocks
        + ", incorrectBlocks="
        + incorrectBlocks
        + '}';
  }
}
