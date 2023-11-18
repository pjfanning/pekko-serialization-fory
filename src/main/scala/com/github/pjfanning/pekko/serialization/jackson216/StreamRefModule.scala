/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * license agreements; and to You under the Apache License, version 2.0:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * This file is part of the Apache Pekko project, which was derived from Akka.
 */

/*
 * Copyright (C) 2009-2022 Lightbend Inc. <https://www.lightbend.com>
 */

package com.github.pjfanning.pekko.serialization.jackson216

// FIXME maybe move many things to `com.github.pjfanning.pekko.serialization.jackson216.internal` package?

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonTokenId
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import org.apache.pekko
import org.apache.pekko.serialization.jackson216.ActorSystemAccess
import pekko.annotation.InternalApi
import pekko.stream.SinkRef
import pekko.stream.SourceRef
import pekko.stream.StreamRefResolver

/**
 * INTERNAL API: Adds support for serializing and deserializing [[pekko.stream.SourceRef]] and [[pekko.stream.SinkRef]].
 */
@InternalApi private[jackson216] trait StreamRefModule extends JacksonModule {
  addSerializer(classOf[SourceRef[_]], () => SourceRefSerializer.instance, () => SourceRefDeserializer.instance)
  addSerializer(classOf[SinkRef[_]], () => SinkRefSerializer.instance, () => SinkRefDeserializer.instance)
}

/**
 * INTERNAL API
 */
@InternalApi private[jackson216] object SourceRefSerializer {
  val instance: SourceRefSerializer = new SourceRefSerializer
}

/**
 * INTERNAL API
 */
@InternalApi private[jackson216] class SourceRefSerializer
    extends StdScalarSerializer[SourceRef[_]](classOf[SourceRef[_]])
    with ActorSystemAccess {

  override def serialize(value: SourceRef[_], jgen: JsonGenerator, provider: SerializerProvider): Unit = {
    val resolver = StreamRefResolver(currentSystem())
    val serializedSourceRef = resolver.toSerializationFormat(value)
    jgen.writeString(serializedSourceRef)
  }
}

/**
 * INTERNAL API
 */
@InternalApi private[jackson216] object SourceRefDeserializer {
  val instance: SourceRefDeserializer = new SourceRefDeserializer
}

/**
 * INTERNAL API
 */
@InternalApi private[jackson216] class SourceRefDeserializer
    extends StdScalarDeserializer[SourceRef[_]](classOf[SourceRef[_]])
    with ActorSystemAccess {

  def deserialize(jp: JsonParser, ctxt: DeserializationContext): SourceRef[_] = {
    if (jp.currentTokenId() == JsonTokenId.ID_STRING) {
      val serializedSourceRef = jp.getText()
      StreamRefResolver(currentSystem()).resolveSourceRef(serializedSourceRef)
    } else
      ctxt.handleUnexpectedToken(handledType(), jp).asInstanceOf[SourceRef[_]]
  }
}

/**
 * INTERNAL API
 */
@InternalApi private[jackson216] object SinkRefSerializer {
  val instance: SinkRefSerializer = new SinkRefSerializer
}

/**
 * INTERNAL API
 */
@InternalApi private[jackson216] class SinkRefSerializer
    extends StdScalarSerializer[SinkRef[_]](classOf[SinkRef[_]])
    with ActorSystemAccess {

  override def serialize(value: SinkRef[_], jgen: JsonGenerator, provider: SerializerProvider): Unit = {
    val resolver = StreamRefResolver(currentSystem())
    val serializedSinkRef = resolver.toSerializationFormat(value)
    jgen.writeString(serializedSinkRef)
  }
}

/**
 * INTERNAL API
 */
@InternalApi private[jackson216] object SinkRefDeserializer {
  val instance: SinkRefDeserializer = new SinkRefDeserializer
}

/**
 * INTERNAL API
 */
@InternalApi private[jackson216] class SinkRefDeserializer
    extends StdScalarDeserializer[SinkRef[_]](classOf[SinkRef[_]])
    with ActorSystemAccess {

  def deserialize(jp: JsonParser, ctxt: DeserializationContext): SinkRef[_] = {
    if (jp.currentTokenId() == JsonTokenId.ID_STRING) {
      val serializedSinkref = jp.getText()
      StreamRefResolver(currentSystem()).resolveSinkRef(serializedSinkref)
    } else
      ctxt.handleUnexpectedToken(handledType(), jp).asInstanceOf[SinkRef[_]]
  }
}
