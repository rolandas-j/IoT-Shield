package com.rolandas.jasiunas.coding.iotshield.models.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Set;

@JsonTypeName(ProfileUpdateEvent.EVENT_TYPE)
public class ProfileUpdateEvent extends ProfileLifecycleEvent {
  public static final String EVENT_TYPE = "profile_update";

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public ProfileUpdateEvent(
      @JsonProperty("model_name") String modelName,
      @JsonProperty("timestamp") Long timestamp,
      @JsonProperty("whitelist") Set<String> whitelist,
      @JsonProperty("blacklist") Set<String> blacklist) {
    super(modelName, timestamp, whitelist, blacklist);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return super.equals(o);
  }
}
