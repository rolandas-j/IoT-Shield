package com.rolandas.jasiunas.coding.iotshield.models.events;

import java.util.Objects;
import java.util.Set;

public abstract class ProfileLifecycleEvent extends DeviceEvent {
  private final Set<String> whitelist;
  private final Set<String> blacklist;

  public ProfileLifecycleEvent(
      String modelName, Long timestamp, Set<String> whitelist, Set<String> blacklist) {
    super(modelName, timestamp);
    this.whitelist = whitelist;
    this.blacklist = blacklist;
  }

  public Set<String> getWhitelist() {
    return whitelist;
  }

  public Set<String> getBlacklist() {
    return blacklist;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ProfileLifecycleEvent that = (ProfileLifecycleEvent) o;
    return Objects.equals(whitelist, that.whitelist) && Objects.equals(blacklist, that.blacklist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), whitelist, blacklist);
  }
}
