package com.github.pjfanning.pekko.serialization.fory.javadsl;

import org.apache.fory.Fory;
import scala.jdk.FunctionWrappers;

import org.apache.fory.config.ForyBuilder;

import java.util.function.Function;

public class ForyInitializer {

  /**
   * Registers a modifier function for the <code>ForyBuilder</code> used to create the
   * default Fory instance. Only 1 modifier can be registered (so this method
   * overrides the previous one). This modifier is applied the first time, the
   * ForySerializer is used so this call must be made early in application startup.
   * @param modifier a function that takes a <code>ForyBuilder</code> and
   *                 returns a modified <code>ForyBuilder</code>
   */
  public static void registerForyBuilderModifier(Function<ForyBuilder, ForyBuilder> modifier) {
    com.github.pjfanning.pekko.serialization.fory.ForyInitializer
        .registerForyBuilderModifier(
            new FunctionWrappers.FromJavaFunction<>(modifier));
  }

  /**
   * Registers a modifier function for the <code>Fory</code> used in the ForySerializer.
   * Only 1 modifier can be registered (so this method overrides the previous one).
   * This modifier is applied the first time, the ForySerializer is used so this call must
   * be made early in application startup.
   * @param modifier a function that takes a <code>Fory</code> instance and
   *                 returns a modified <code>Fory</code> instance
   */
  public static void registerForyModifier(Function<Fory, Fory> modifier) {
    com.github.pjfanning.pekko.serialization.fory.ForyInitializer
        .registerForyModifier(
            new FunctionWrappers.FromJavaFunction<>(modifier));
  }

}
