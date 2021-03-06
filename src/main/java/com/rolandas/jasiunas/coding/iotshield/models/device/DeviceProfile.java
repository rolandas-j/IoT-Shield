package com.rolandas.jasiunas.coding.iotshield.models.device;

import com.rolandas.jasiunas.coding.iotshield.models.security.Blacklist;
import com.rolandas.jasiunas.coding.iotshield.models.security.Whitelist;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class DeviceProfile {

  private final String modelName;
  private final DevicePolicy defaultPolicy;
  private final Whitelist whitelist;
  private final Blacklist blacklist;

  public DeviceProfile(
      String modelName, DevicePolicy defaultPolicy, Whitelist whitelist, Blacklist blacklist) {
    this.modelName = modelName;
    this.defaultPolicy = defaultPolicy;
    this.whitelist = whitelist;
    this.blacklist = blacklist;
  }

  public DeviceProfile withUpdatedConfig(
      @NotNull Whitelist updatedWhitelist, @NotNull Blacklist updatedBlacklist) {
    return new DeviceProfile(modelName, defaultPolicy, updatedWhitelist, updatedBlacklist);
  }

  public String getModelName() {
    return modelName;
  }

  public DevicePolicy getDefaultPolicy() {
    return defaultPolicy;
  }

  public Whitelist getWhitelist() {
    return whitelist;
  }

  public Blacklist getBlacklist() {
    return blacklist;
  }

  public boolean matchesDevice(Device device) {
    return device.getModelName().equals(getModelName());
  }

  @Override
  public String toString() {
    return "DeviceProfile{"
        + "modelName='"
        + modelName
        + '\''
        + ", defaultPolicy="
        + defaultPolicy
        + ", whitelist="
        + whitelist
        + ", blacklist="
        + blacklist
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceProfile that = (DeviceProfile) o;
    return Objects.equals(modelName, that.modelName)
        && defaultPolicy == that.defaultPolicy
        && Objects.equals(whitelist, that.whitelist)
        && Objects.equals(blacklist, that.blacklist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modelName, defaultPolicy, whitelist, blacklist);
  }
}
