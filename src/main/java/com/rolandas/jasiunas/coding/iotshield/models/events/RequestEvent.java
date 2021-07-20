package com.rolandas.jasiunas.coding.iotshield.models.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Objects;

@JsonTypeName(RequestEvent.EVENT_TYPE)
public class RequestEvent extends DeviceEvent {
  public static final String EVENT_TYPE = "request";

  @JsonProperty("request_id")
  private final String requestId;

  @JsonProperty("device_id")
  private final String deviceId;

  private final String url;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public RequestEvent(
      @JsonProperty("model_name") String modelName,
      @JsonProperty("timestamp") Long timestamp,
      @JsonProperty("request_id") String requestId,
      @JsonProperty("device_id") String deviceId,
      @JsonProperty("url") String url) {
    super(modelName, timestamp);
    this.requestId = requestId;
    this.deviceId = deviceId;
    this.url = url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    RequestEvent that = (RequestEvent) o;
    return Objects.equals(requestId, that.requestId)
        && Objects.equals(deviceId, that.deviceId)
        && Objects.equals(url, that.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), requestId, deviceId, url);
  }

  public String getRequestId() {
    return requestId;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public String getUrl() {
    return url;
  }
}
