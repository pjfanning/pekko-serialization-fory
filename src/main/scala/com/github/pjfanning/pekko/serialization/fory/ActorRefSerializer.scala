/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * license agreements; and to You under the Apache License, version 2.0:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * This file is part of the Apache Pekko project, which was derived from Akka.
 */

/*
 * Copyright (C) 2016-2022 Lightbend Inc. <https://www.lightbend.com>
 */

package com.github.pjfanning.pekko.serialization.fory

import org.apache.fory.Fory
import org.apache.fory.memory.MemoryBuffer
import org.apache.fory.serializer.AbstractObjectSerializer
import org.apache.pekko.actor.ActorRef
import org.apache.pekko.serialization.fory.ActorSystemAccess

class ActorRefSerializer(fory: Fory)
  extends AbstractObjectSerializer[ActorRef](fory, classOf[ActorRef])
  with ActorSystemAccess {

  override def write(buffer: MemoryBuffer, actorRef: ActorRef): Unit = {
    val serializedActorRef = actorRef.path.toSerializationFormatWithAddress(currentSystem().provider.getDefaultAddress)
    fory.writeString(buffer, serializedActorRef)
  }

  override def read(buffer: MemoryBuffer): ActorRef = {
    val serializedActorRef = fory.readString(buffer)
    currentSystem().provider.resolveActorRef(serializedActorRef)
  }
}
