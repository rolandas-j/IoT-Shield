package com.rolandas.jasiunas.coding.iotshield;

import static com.rolandas.jasiunas.coding.iotshield.Fixtures.ALLOW_PROFILE_CREATE_EVENT;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.DEVICE_ID;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.PROFILE_UPDATE_EVENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.rolandas.jasiunas.coding.iotshield.models.device.Device;
import com.rolandas.jasiunas.coding.iotshield.models.device.DeviceProfile;
import com.rolandas.jasiunas.coding.iotshield.models.device.DeviceStatus;
import com.rolandas.jasiunas.coding.iotshield.models.security.Blacklist;
import com.rolandas.jasiunas.coding.iotshield.models.security.Whitelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeviceManagerTest {

  DeviceManager deviceManager;

  @BeforeEach
  void setup() {
    deviceManager = new DeviceManager();
  }

  @Test
  void givenDeviceCreate_registersDevice() {
    deviceManager.handleProfileLifecycleEvent(ALLOW_PROFILE_CREATE_EVENT);

    DeviceProfile deviceProfile =
        deviceManager
            .getDeviceProfile(ALLOW_PROFILE_CREATE_EVENT.getModelName())
            .orElseThrow(() -> new AssertionError("Cannot be null"));

    assertThat(deviceProfile.getModelName(), equalTo(ALLOW_PROFILE_CREATE_EVENT.getModelName()));
    assertThat(
        deviceProfile.getDefaultPolicy(), equalTo(ALLOW_PROFILE_CREATE_EVENT.getDefaultPolicy()));
    Blacklist expectedBlacklist = Blacklist.create(ALLOW_PROFILE_CREATE_EVENT.getBlacklist());
    assertThat(deviceProfile.getBlacklist(), equalTo(expectedBlacklist));
    Whitelist expectedWhitelist = Whitelist.create(ALLOW_PROFILE_CREATE_EVENT.getWhitelist());
    assertThat(deviceProfile.getWhitelist(), equalTo(expectedWhitelist));
  }

  @Test
  void givenDeviceUpdate_updatesDevice() {
    deviceManager.handleProfileLifecycleEvent(ALLOW_PROFILE_CREATE_EVENT);
    deviceManager.handleProfileLifecycleEvent(PROFILE_UPDATE_EVENT);

    DeviceProfile deviceProfile =
        deviceManager.getDeviceProfile(PROFILE_UPDATE_EVENT.getModelName()).orElseThrow();

    assertThat(deviceProfile.getModelName(), equalTo(ALLOW_PROFILE_CREATE_EVENT.getModelName()));
    assertThat(
        deviceProfile.getDefaultPolicy(), equalTo(ALLOW_PROFILE_CREATE_EVENT.getDefaultPolicy()));
    Blacklist expectedBlacklist = Blacklist.create(PROFILE_UPDATE_EVENT.getBlacklist());
    assertThat(deviceProfile.getBlacklist(), equalTo(expectedBlacklist));
    Whitelist expectedWhitelist = Whitelist.create(PROFILE_UPDATE_EVENT.getWhitelist());
    assertThat(deviceProfile.getWhitelist(), equalTo(expectedWhitelist));
  }

  @Test
  void givenDeviceDetails_ableToRegisterDevice() {
    deviceManager.handleProfileLifecycleEvent(ALLOW_PROFILE_CREATE_EVENT);

    Device device =
        deviceManager.getOrRegisterDevice(DEVICE_ID, ALLOW_PROFILE_CREATE_EVENT.getModelName());

    assertThat(device.getId(), equalTo(DEVICE_ID));
    assertThat(device.getModelName(), equalTo(ALLOW_PROFILE_CREATE_EVENT.getModelName()));
    assertThat(device.getStatus(), equalTo(DeviceStatus.ACTIVE));
  }

  @Test
  void givenActiveDevice_ableToQuarantineDevice() {
    deviceManager.handleProfileLifecycleEvent(ALLOW_PROFILE_CREATE_EVENT);

    Device device =
        deviceManager.getOrRegisterDevice(DEVICE_ID, ALLOW_PROFILE_CREATE_EVENT.getModelName());
    deviceManager.quarantineDevice(device.getId());

    assertThat(device.getStatus(), equalTo(DeviceStatus.IN_QUARANTINE));
  }

  @Test
  void givenProfileUpdateWithDevicesInQuarantine_movesDevicesToActive() {
    deviceManager.handleProfileLifecycleEvent(ALLOW_PROFILE_CREATE_EVENT);

    Device device =
        deviceManager.getOrRegisterDevice(DEVICE_ID, ALLOW_PROFILE_CREATE_EVENT.getModelName());
    deviceManager.quarantineDevice(device.getId());

    deviceManager.handleProfileLifecycleEvent(PROFILE_UPDATE_EVENT);

    Device updatedDevice = deviceManager.getDevice(device.getId()).orElseThrow();

    assertThat(updatedDevice.getStatus(), equalTo(DeviceStatus.ACTIVE));
  }
}
