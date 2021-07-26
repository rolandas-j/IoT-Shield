package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.models.device.Device;
import com.rolandas.jasiunas.coding.iotshield.models.device.DevicePolicy;
import com.rolandas.jasiunas.coding.iotshield.models.device.DeviceProfile;
import com.rolandas.jasiunas.coding.iotshield.models.device.DeviceStatus;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileCreateEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.ProfileUpdateEvent;
import com.rolandas.jasiunas.coding.iotshield.models.events.RequestEvent;
import com.rolandas.jasiunas.coding.iotshield.models.security.Blacklist;
import com.rolandas.jasiunas.coding.iotshield.models.security.Whitelist;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

public class Fixtures {
  public static final String MATCHING_BLACKLIST_URL = "www.coding.com";
  public static final String MATCHING_WHITELIST_URL = "www.iot-shield.com";
  public static final String OTHER_URL = "www.other.net";
  public static final String NON_MATCHING_URL = "www.non-matching.com";

  public static final String DEVICE_ID = "DEVICE_1";

  public static final String REQUEST_ID = "REQUEST_1";
  public static final String OTHER_REQUEST_ID = "REQUEST_2";

  public static final Blacklist BLACKLIST =
      Blacklist.create(Set.of(MATCHING_BLACKLIST_URL, OTHER_URL));
  public static final Whitelist WHITELIST =
      Whitelist.create(Set.of(MATCHING_WHITELIST_URL, OTHER_URL));

  public static final String MODEL_NAME = "iPhone";

  public static final ProfileCreateEvent ALLOW_PROFILE_CREATE_EVENT =
      new ProfileCreateEvent(
          MODEL_NAME,
          Instant.now().getEpochSecond(),
          Collections.singleton(MATCHING_WHITELIST_URL),
          Collections.emptySet(),
          DevicePolicy.ALLOW);

  public static final ProfileUpdateEvent PROFILE_UPDATE_EVENT =
      new ProfileUpdateEvent(
          MODEL_NAME,
          Instant.now().getEpochSecond(),
          Set.of(MATCHING_WHITELIST_URL, OTHER_URL),
          Set.of(MATCHING_BLACKLIST_URL, OTHER_URL));

  public static final RequestEvent NON_MATCHING_REQUEST_EVENT =
      new RequestEvent(
          MODEL_NAME, Instant.now().getEpochSecond(), REQUEST_ID, DEVICE_ID, NON_MATCHING_URL);

  public static final RequestEvent REQUEST_EVENT_WITH_WHITELISTED_URL =
      new RequestEvent(
          MODEL_NAME,
          Instant.now().getEpochSecond(),
          OTHER_REQUEST_ID,
          DEVICE_ID,
          MATCHING_WHITELIST_URL);

  public static final RequestEvent REQUEST_EVENT_WITH_BLACKLISTED_URL =
      new RequestEvent(
          MODEL_NAME,
          Instant.now().getEpochSecond(),
          OTHER_REQUEST_ID,
          DEVICE_ID,
          MATCHING_BLACKLIST_URL);

  public static final DeviceProfile BLOCK_DEVICE_PROFILE =
      new DeviceProfile(MODEL_NAME, DevicePolicy.BLOCK, WHITELIST, Blacklist.EMPTY);

  public static final DeviceProfile ALLOW_DEVICE_PROFILE =
      new DeviceProfile(MODEL_NAME, DevicePolicy.ALLOW, Whitelist.EMPTY, BLACKLIST);

  public static final Device ACTIVE_DEVICE = new Device(DEVICE_ID, MODEL_NAME, DeviceStatus.ACTIVE);

  private Fixtures() {}
}
