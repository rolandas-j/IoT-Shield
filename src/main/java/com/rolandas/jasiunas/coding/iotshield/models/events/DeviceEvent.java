package com.rolandas.jasiunas.coding.iotshield.models.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ProfileCreateEvent.class, name = ProfileCreateEvent.EVENT_TYPE),
  @JsonSubTypes.Type(value = ProfileUpdateEvent.class, name = ProfileUpdateEvent.EVENT_TYPE),
  @JsonSubTypes.Type(value = RequestEvent.class, name = RequestEvent.EVENT_TYPE)
})
public abstract class DeviceEvent {

  @JsonProperty("model_name")
  private final String modelName;

  private final Long timestamp;

  public DeviceEvent(String modelName, Long timestamp) {
    this.modelName = modelName;
    this.timestamp = timestamp;
  }

  public String getModelName() {
    return modelName;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceEvent that = (DeviceEvent) o;
    return Objects.equals(modelName, that.modelName) && Objects.equals(timestamp, that.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modelName, timestamp);
  }
}
