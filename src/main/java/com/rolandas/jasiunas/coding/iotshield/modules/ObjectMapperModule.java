package com.rolandas.jasiunas.coding.iotshield.modules;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public interface ObjectMapperModule {
  @Provides
  @Singleton
  static ObjectMapper objectMapper() {
    return new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
  }
}
