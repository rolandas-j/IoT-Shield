package com.rolandas.jasiunas.coding.iotshield.models.security;

import static com.rolandas.jasiunas.coding.iotshield.Fixtures.MATCHING_WHITELIST_URL;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.NON_MATCHING_URL;
import static com.rolandas.jasiunas.coding.iotshield.Fixtures.WHITELIST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.rolandas.jasiunas.coding.iotshield.models.actions.ActionType;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class WhitelistTest {
  @Test
  void givenMatchingString_whitelistMatches() {
    Optional<ActionType> actionType = WHITELIST.matches(MATCHING_WHITELIST_URL);

    assertThat(actionType.orElse(null), equalTo(ActionType.ALLOW));
  }

  @Test
  void givenNonMatchingString_whitelistDoesNotMatch() {
    Optional<ActionType> actionType = WHITELIST.matches(NON_MATCHING_URL);

    assertThat(actionType.isEmpty(), is(Boolean.TRUE));
  }
}
