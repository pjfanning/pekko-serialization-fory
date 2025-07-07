package com.github.pjfanning.pekko.serialization.fory

object ClassCheck {
  lazy val typedActorRefClass: OptionVal[Class[_]] = {
    try {
      OptionVal.Some(Class.forName("org.apache.pekko.actor.typed.ActorRef"))
    } catch {
      case _: ClassNotFoundException => OptionVal.None
    }
  }

  lazy val sourceRefClass: OptionVal[Class[_]] = {
    try {
      OptionVal.Some(Class.forName("org.apache.pekko.stream.SourceRef"))
    } catch {
      case _: ClassNotFoundException => OptionVal.None
    }
  }

  lazy val sinkRefClass: OptionVal[Class[_]] = {
    try {
      OptionVal.Some(Class.forName("org.apache.pekko.stream.SinkRef"))
    } catch {
      case _: ClassNotFoundException => OptionVal.None
    }
  }

  def typedActorSupported: Boolean = typedActorRefClass.isDefined
  def pekkoStreamSupported: Boolean = sourceRefClass.isDefined
}
