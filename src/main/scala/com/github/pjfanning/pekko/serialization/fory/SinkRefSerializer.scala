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
import org.apache.pekko.serialization.fory.ActorSystemAccess
import org.apache.pekko.stream.{SinkRef, StreamRefResolver}

class SinkRefSerializer(fory: Fory)
  extends AbstractObjectSerializer[SinkRef[_]](fory, classOf[SinkRef[_]])
  with ActorSystemAccess {

  override def write(buffer: MemoryBuffer, sinkRef: SinkRef[_]): Unit = {
    val resolver = StreamRefResolver(currentSystem())
    fory.writeString(buffer, resolver.toSerializationFormat(sinkRef))
  }

  override def read(buffer: MemoryBuffer): SinkRef[_] = {
    val serializedSourceRef = fory.readString(buffer)
    StreamRefResolver(currentSystem()).resolveSinkRef(serializedSourceRef)
  }
}
