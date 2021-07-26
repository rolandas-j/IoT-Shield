package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.models.device.Device;
import com.rolandas.jasiunas.coding.iotshield.models.device.DeviceProfile;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileCreateEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileLifecycleEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileUpdateEvent;
import com.rolandas.jasiunas.coding.iotshield.models.security.Blacklist;
import com.rolandas.jasiunas.coding.iotshield.models.security.Whitelist;
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

  public Optional<Device> getDevice(String deviceId) {
    return Optional.ofNullable(devices.get(deviceId));
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

  private void registerDeviceProfile(DeviceProfile deviceProfile) {
    activeProfiles.put(deviceProfile.getModelName(), deviceProfile);
  }

  private Device registerDevice(String deviceId, String modelName) {
    Device device = Device.createDevice(deviceId, modelName);

    return registerDevice(device);
  }

  private Device registerDevice(Device device) {
    devices.put(device.getId(), device);

    return device;
  }

  private void handleProfileCreate(ProfileCreateEvent profileCreateEvent) {
    Whitelist whiteList = Whitelist.create(profileCreateEvent.getWhitelist());
    Blacklist blacklist = Blacklist.create(profileCreateEvent.getBlacklist());
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

    Whitelist updatedWhitelist =
        Optional.ofNullable(profileUpdateEvent.getWhitelist())
            .map(Whitelist::create)
            .orElse(Whitelist.EMPTY);

    Blacklist updatedBlacklist =
        Optional.ofNullable(profileUpdateEvent.getBlacklist())
            .map(Blacklist::create)
            .orElse(Blacklist.EMPTY);
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
}
