package com.github.pjfanning.pekko.serialization.fory

import org.apache.fory.{Fory, ThreadLocalFory}
import org.apache.fory.config.{ForyBuilder, Language}
import org.apache.fory.serializer.scala.ScalaSerializers
import org.apache.pekko.actor.{ActorRef, Address}

object ForyInitializer {

  private var foryBuilderModifier: ForyBuilder => ForyBuilder = identity
  private var foryModifier: Fory => Fory = identity

  /**
   * Creates a thread-local instance of Fory with the default configuration.
   * @return a new ThreadLocalFory instance
   */
  def createThreadLocalForyInstance(): ThreadLocalFory =
    new ThreadLocalFory(classLoader =>
      createDefaultForyInstance(classLoader))

  /**
   * Registers a modifier function for the <code>ForyBuilder</code> used to create the
   * default Fory instance. Only 1 modifier can be registered (so this method
   * overrides the previous one). This modifier is applied the first time, the
   * ForySerializer is used so this call must be made early in application startup.
   * @param modifier a function that takes a <code>ForyBuilder</code> and
   *                 returns a modified <code>ForyBuilder</code>
   */
  def registerForyBuilderModifier(modifier: ForyBuilder => ForyBuilder): Unit = {
    this.foryBuilderModifier = modifier
  }

  /**
   * Registers a modifier function for the <code>Fory</code> used in the ForySerializer.
   * Only 1 modifier can be registered (so this method overrides the previous one).
   * This modifier is applied the first time, the ForySerializer is used so this call must
   * be made early in application startup.
   * @param modifier a function that takes a <code>Fory</code> instance and
   *                 returns a modified <code>Fory</code> instance
   */
  def registerForyModifier(modifier: Fory => Fory): Unit = {
    this.foryModifier = modifier
  }

  /**
   * Creates a default instance of Fory with the setup used in pekko-serialization-fory tests.
   * <p>
   *   This instance is not thread-safe.
   * </p>
   * @return a new instance of Fory
   */
  private def createDefaultForyInstance(classLoader: ClassLoader): Fory = {
    val fory = createDefaultForyBuilder(classLoader).build()
    ScalaSerializers.registerSerializers(fory)
    fory.registerSerializer(classOf[ActorRef], new ActorRefSerializer(fory))
    fory.registerSerializer(classOf[Address], new AddressSerializer(fory))
    if (ClassCheck.typedActorSupported) {
      fory.registerSerializer(ClassCheck.typedActorRefClass.get, new TypedActorRefSerializer(fory))
    }
    if (ClassCheck.pekkoStreamSupported) {
      fory.registerSerializer(ClassCheck.sourceRefClass.get, new SourceRefSerializer(fory))
      fory.registerSerializer(ClassCheck.sinkRefClass.get, new SinkRefSerializer(fory))
    }
    foryModifier(fory)
  }

  private def createDefaultForyBuilder(classLoader: ClassLoader): ForyBuilder = {
    foryBuilderModifier(
      Fory.builder()
        .withLanguage(Language.JAVA)
        .withClassLoader(classLoader)
        .withRefTracking(true) // TODO make this configurable
        .withScalaOptimizationEnabled(true)
        .requireClassRegistration(false) // TODO remove this
    )
  }
}
