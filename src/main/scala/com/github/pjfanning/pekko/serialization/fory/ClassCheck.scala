package com.github.pjfanning.pekko.serialization.fory

object ClassCheck {
  lazy val typedActorRefClass: OptionVal[Class[_]] = {
    try {
      OptionVal.Some(Class.forName("org.apache.pekko.actor.typed.ActorRef"))
    } catch {
      case _: ClassNotFoundException => OptionVal.None
    }
  }

  def typedActorSupported: Boolean = typedActorRefClass.isDefined
}
