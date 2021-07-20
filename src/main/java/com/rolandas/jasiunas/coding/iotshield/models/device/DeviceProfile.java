package com.rolandas.jasiunas.coding.iotshield.models.device;

import com.rolandas.jasiunas.coding.iotshield.models.BlacklistProperty;
import com.rolandas.jasiunas.coding.iotshield.models.WhitelistProperty;
import java.util.Collections;
import java.util.Set;
import javax.validation.constraints.NotNull;

public class DeviceProfile {

  private final String modelName;
  private final DevicePolicy defaultPolicy;
  private final Set<WhitelistProperty> whitelist;
  private final Set<BlacklistProperty> blacklist;

  public DeviceProfile(
      String modelName,
      DevicePolicy defaultPolicy,
      Set<WhitelistProperty> whitelist,
      Set<BlacklistProperty> blacklist) {
    this.modelName = modelName;
    this.defaultPolicy = defaultPolicy;
    this.whitelist = Collections.unmodifiableSet(whitelist);
    this.blacklist = Collections.unmodifiableSet(blacklist);
  }

  public DeviceProfile withUpdatedConfig(
      @NotNull Set<WhitelistProperty> updatedWhitelist,
      @NotNull Set<BlacklistProperty> updatedBlacklist) {
    return new DeviceProfile(modelName, defaultPolicy, updatedWhitelist, updatedBlacklist);
  }

  public String getModelName() {
    return modelName;
  }

  public DevicePolicy getDefaultPolicy() {
    return defaultPolicy;
  }

  public Set<WhitelistProperty> getWhitelist() {
    return whitelist;
  }

  public Set<BlacklistProperty> getBlacklist() {
    return blacklist;
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
}
