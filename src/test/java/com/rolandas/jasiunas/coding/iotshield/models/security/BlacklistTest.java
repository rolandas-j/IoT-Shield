package com.rolandas.jasiunas.coding.iotshield.models.security;

import static com.rolandas.jasiunas.coding.iotshield.Fixtures.BLACKLIST;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.MATCHING_BLACKLIST_URL;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.NON_MATCHING_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class BlacklistTest {

  @Test
  void givenMatchingString_blacklistMatches() {
    Optional<ActionType> actionType = BLACKLIST.matches(MATCHING_BLACKLIST_URL);

    assertThat(actionType.orElse(null), equalTo(ActionType.BLOCK));
  }

  @Test
  void givenNonMatchingString_blacklistDoesNotMatch() {
    Optional<ActionType> actionType = BLACKLIST.matches(NON_MATCHING_URL);

    assertThat(actionType.isEmpty(), is(Boolean.TRUE));
  }
}
