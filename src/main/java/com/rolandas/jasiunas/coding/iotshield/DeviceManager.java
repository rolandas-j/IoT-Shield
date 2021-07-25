package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.models.BlacklistProperty;
import com.rolandas.jasiunas.coding.iotshield.models.WhitelistProperty;
import com.rolandas.jasiunas.coding.iotshield.models.device.Device;
import com.rolandas.jasiunas.coding.iotshield.models.device.DeviceProfile;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileCreateEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileLifecycleEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileUpdateEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DeviceManager {
  public Map<String, DeviceProfile> activeProfiles;
  public Map<String, Device> devices;

  public DeviceManager() {
    activeProfiles = new HashMap<>();
    devices = new HashMap<>();
  }

  private DeviceManager(Map<String, DeviceProfile> activeProfiles, Map<String, Device> devices) {
    this.activeProfiles = activeProfiles;
    this.devices = devices;
  }

  public DeviceManager withCopyOfCurrentProfileState() {
    return new DeviceManager(new HashMap<>(activeProfiles), new HashMap<>());
  }

  public int getProtectedDevices() {
    return devices.size();
  }

  public void registerDeviceProfile(DeviceProfile deviceProfile) {
    activeProfiles.put(deviceProfile.getModelName(), deviceProfile);
  }

  public Device registerDevice(Device device) {
    devices.put(device.getId(), device);

    return device;
  }

  public Device getOrRegisterDevice(String deviceId, String modelName) {
    return getDevice(deviceId).orElseGet(() -> registerDevice(deviceId, modelName));
  }

  public void quarantineDevice(String deviceId) {
    Device device = devices.get(deviceId);
    Device quarantinedDevice = device.quarantine();

    devices.put(deviceId, quarantinedDevice);
  }

  public Optional<DeviceProfile> getDeviceProfile(String modelName) {
    return Optional.ofNullable(activeProfiles.get(modelName));
  }

  public Optional<Device> getDevice(String deviceId) {
    return Optional.ofNullable(devices.get(deviceId));
  }

  public void handleProfileLifecycleEvent(ProfileLifecycleEvent profileLifecycleEvent) {
    if (profileLifecycleEvent instanceof ProfileUpdateEvent) {
      handleProfileUpdate((ProfileUpdateEvent) profileLifecycleEvent);
      return;
    }
    if (profileLifecycleEvent instanceof ProfileCreateEvent) {
      handleProfileCreate((ProfileCreateEvent) profileLifecycleEvent);
      return;
    }

    throw new IllegalArgumentException(
        String.format(
            "Unknown profile lifecycle event: %s", profileLifecycleEvent.getClass().getName()));
  }

  private Device registerDevice(String deviceId, String modelName) {
    Device device = Device.createDevice(deviceId, modelName);

    return registerDevice(device);
  }

  private void handleProfileCreate(ProfileCreateEvent profileCreateEvent) {
    Set<WhitelistProperty> whiteList = mapToWhitelistCollection(profileCreateEvent.getWhitelist());
    Set<BlacklistProperty> blacklist = mapToBlacklistCollection(profileCreateEvent.getBlacklist());
    DeviceProfile deviceProfile =
        new DeviceProfile(
            profileCreateEvent.getModelName(),
            profileCreateEvent.getDefaultPolicy(),
            whiteList,
            blacklist);

    registerDeviceProfile(deviceProfile);
  }

  private void handleProfileUpdate(ProfileUpdateEvent profileUpdateEvent) {
    DeviceProfile currentProfile =
        getDeviceProfile(profileUpdateEvent.getModelName())
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        String.format(
                            "Profile for device %s is not available",
                            profileUpdateEvent.getModelName())));

    Set<WhitelistProperty> updatedWhitelist =
        Optional.ofNullable(profileUpdateEvent.getWhitelist())
            .map(this::mapToWhitelistCollection)
            .orElse(Collections.emptySet());

    Set<BlacklistProperty> updatedBlacklist =
        Optional.ofNullable(profileUpdateEvent.getBlacklist())
            .map(this::mapToBlacklistCollection)
            .orElse(Collections.emptySet());
    DeviceProfile updatedProfile =
        currentProfile.withUpdatedConfig(updatedWhitelist, updatedBlacklist);

    updateProfileAndRemoveQuarantine(updatedProfile);
  }

  private void updateProfileAndRemoveQuarantine(DeviceProfile deviceProfile) {
    Set<Device> devicesInQuarantine =
        devices.values().stream()
            .filter(Device::inQuarantine)
            .filter(deviceProfile::matchesDevice)
            .collect(Collectors.toSet());

    devicesInQuarantine.stream().map(Device::activate).forEach(this::registerDevice);

    registerDeviceProfile(deviceProfile);
  }

  private Set<WhitelistProperty> mapToWhitelistCollection(Collection<String> stringList) {
    return stringList.stream().map(WhitelistProperty::new).collect(Collectors.toSet());
  }

  private Set<BlacklistProperty> mapToBlacklistCollection(Collection<String> stringList) {
    return stringList.stream().map(BlacklistProperty::new).collect(Collectors.toSet());
  }
}
