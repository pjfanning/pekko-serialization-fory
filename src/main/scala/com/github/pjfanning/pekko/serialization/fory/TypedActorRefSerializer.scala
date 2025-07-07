package com.github.pjfanning.pekko.serialization.fory

import org.apache.fory.Fory
import org.apache.fory.memory.MemoryBuffer
import org.apache.fory.serializer.AbstractObjectSerializer
import org.apache.pekko.actor.typed.{ ActorRef, ActorRefResolver }
import org.apache.pekko.actor.typed.scaladsl.adapter._
import org.apache.pekko.serialization.fory.ActorSystemAccess

class TypedActorRefSerializer(fory: Fory)
  extends AbstractObjectSerializer[ActorRef[_]](fory, classOf[ActorRef[_]])
  with ActorSystemAccess {

  override def write(buffer: MemoryBuffer, actorRef: ActorRef[_]): Unit = {
    val serializedActorRef = ActorRefResolver(currentSystem().toTyped).toSerializationFormat(actorRef)
    fory.writeString(buffer, serializedActorRef)
  }

  override def read(buffer: MemoryBuffer): ActorRef[_] = {
    val serializedActorRef = fory.readString(buffer)
    ActorRefResolver(currentSystem().toTyped).resolveActorRef(serializedActorRef)
  }
}
