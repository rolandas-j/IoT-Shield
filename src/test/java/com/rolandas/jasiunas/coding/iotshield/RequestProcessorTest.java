package com.rolandas.jasiunas.coding.iotshield;

import static com.rolandas.jasiunas.coding.iotshield.Fixtures.ACTIVE_DEVICE;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.ALLOW_DEVICE_PROFILE;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.BLOCK_DEVICE_PROFILE;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.DEVICE_ID;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.MODEL_NAME;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.NON_MATCHING_REQUEST_EVENT;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.REQUEST_EVENT_WITH_BLACKLISTED_URL;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.REQUEST_EVENT_WITH_WHITELISTED_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import com.rolandas.jasiunas.coding.iotshield.models.device.Device;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestProcessorTest {

  private RequestProcessor requestProcessor;
  private DeviceManager deviceManager;

  @BeforeEach
  void beforeEach() {
    deviceManager = mock(DeviceManager.class);
    requestProcessor = new RequestProcessor(deviceManager);
  }

  @Test
  void givenRequestWithQuarantinedDevice_shouldBlock() {
    Device quarantinedDevice = ACTIVE_DEVICE.quarantine();
    when(deviceManager.getOrRegisterDevice(any(), any())).thenReturn(quarantinedDevice);
    when(deviceManager.getDeviceProfile(MODEL_NAME)).thenReturn(Optional.empty());

    ActionType result = requestProcessor.handleRequest(NON_MATCHING_REQUEST_EVENT);

    assertThat(result, equalTo(ActionType.BLOCK));
  }

  @Test
  void givenRequestWithNoDeviceProfile_shouldAllow() {
    when(deviceManager.getOrRegisterDevice(any(), any())).thenReturn(ACTIVE_DEVICE);
    when(deviceManager.getDeviceProfile(MODEL_NAME)).thenReturn(Optional.empty());

    ActionType result = requestProcessor.handleRequest(NON_MATCHING_REQUEST_EVENT);

    assertThat(result, equalTo(ActionType.ALLOW));
  }

  @Test
  void givenRequestWithNonMatchingSecurityList_shouldUseDefaultAllowPolicy() {
    when(deviceManager.getOrRegisterDevice(any(), any())).thenReturn(ACTIVE_DEVICE);
    when(deviceManager.getDeviceProfile(MODEL_NAME)).thenReturn(Optional.of(ALLOW_DEVICE_PROFILE));

    ActionType result = requestProcessor.handleRequest(NON_MATCHING_REQUEST_EVENT);

    assertThat(result, equalTo(ActionType.ALLOW));
  }

  @Test
  void givenRequestWithNonMatchingSecurityListAndDefaultBlockPolicy_shouldMoveDeviceToQuarantine() {
    when(deviceManager.getOrRegisterDevice(any(), any())).thenReturn(ACTIVE_DEVICE);
    when(deviceManager.getDeviceProfile(MODEL_NAME)).thenReturn(Optional.of(BLOCK_DEVICE_PROFILE));

    ActionType result = requestProcessor.handleRequest(NON_MATCHING_REQUEST_EVENT);

    assertThat(result, equalTo(ActionType.QUARANTINE));
    verify(deviceManager, times(1)).quarantineDevice(DEVICE_ID);
  }

  @Test
  void givenRequestWithMatchingSecurityList_shouldAllowRequest() {
    when(deviceManager.getOrRegisterDevice(any(), any())).thenReturn(ACTIVE_DEVICE);
    when(deviceManager.getDeviceProfile(MODEL_NAME)).thenReturn(Optional.of(BLOCK_DEVICE_PROFILE));

    ActionType result = requestProcessor.handleRequest(REQUEST_EVENT_WITH_WHITELISTED_URL);

    assertThat(result, equalTo(ActionType.ALLOW));
  }

  @Test
  void givenRequestWithMatchingSecurityList_shouldBlockRequest() {
    when(deviceManager.getOrRegisterDevice(any(), any())).thenReturn(ACTIVE_DEVICE);
    when(deviceManager.getDeviceProfile(MODEL_NAME)).thenReturn(Optional.of(ALLOW_DEVICE_PROFILE));

    ActionType result = requestProcessor.handleRequest(REQUEST_EVENT_WITH_BLACKLISTED_URL);

    assertThat(result, equalTo(ActionType.BLOCK));
  }
}
