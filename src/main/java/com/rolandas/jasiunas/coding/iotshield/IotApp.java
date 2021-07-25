package com.rolandas.jasiunas.coding.iotshield;

import com.rolandas.jasiunas.coding.iotshield.input.InputPath;
import com.rolandas.jasiunas.coding.iotshield.modules.AppModule;
import com.rolandas.jasiunas.coding.iotshield.modules.ObjectMapperModule;
import com.rolandas.jasiunas.coding.iotshield.output.OutputPath;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;

public class IotApp {

  @Singleton
  @Component(modules = {AppModule.class, ObjectMapperModule.class})
  public interface IotAppApplication {
    IotShield iotShield();

    @Component.Builder
    interface Builder {
      @BindsInstance
      Builder inputPath(@InputPath String inputPath);

      @BindsInstance
      Builder outputPath(@OutputPath String outputPath);

      IotAppApplication build();
    }
  }

  public static void main(String[] args) throws Exception {
    IotAppApplication iotAppApplication =
        DaggerIotApp_IotAppApplication.builder()
            .inputPath("/input.json")
            .outputPath("output.json")
            .build();
    iotAppApplication.iotShield().start();
  }
}
