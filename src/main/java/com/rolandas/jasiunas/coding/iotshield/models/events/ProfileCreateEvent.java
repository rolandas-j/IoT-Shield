package com.rolandas.jasiunas.coding.iotshield.models.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.rolandas.jasiunas.coding.iotshield.models.device.DevicePolicy;
import java.util.Objects;
import java.util.Set;

@JsonTypeName(ProfileCreateEvent.EVENT_TYPE)
public class ProfileCreateEvent extends ProfileLifecycleEvent {
  public static final String EVENT_TYPE = "profile_create";

  @JsonProperty("default")
  private final DevicePolicy defaultPolicy;

  @JsonCreator
  public ProfileCreateEvent(
      @JsonProperty("model_name") String modelName,
      @JsonProperty("timestamp") Long timestamp,
      @JsonProperty("whitelist") Set<String> whitelist,
      @JsonProperty("blacklist") Set<String> blacklist,
      @JsonProperty("default") DevicePolicy defaultPolicy) {
    super(modelName, timestamp, whitelist, blacklist);
    this.defaultPolicy = defaultPolicy;
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
    ProfileCreateEvent that = (ProfileCreateEvent) o;
    return defaultPolicy == that.defaultPolicy;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), defaultPolicy);
  }

  public DevicePolicy getDefaultPolicy() {
    return defaultPolicy;
  }
}
