package com.github.pjfanning.pekko.serialization.fory

import org.apache.pekko.serialization.fory.ActorSystemAccess
import org.apache.pekko.serialization.{SerializationExtension, Serializer, Serializers}

final class ForySerializationSerializer extends ActorSystemAccess {
  def serialization = SerializationExtension(currentSystem())
  /*
  override def serialize(value: AnyRef, jgen: JsonGenerator, provider: SerializerProvider): Unit = {
    val serializer: Serializer = serialization.findSerializerFor(value)
    val serId = serializer.identifier
    val manifest = Serializers.manifestFor(serializer, value)
    val serialized = serializer.toBinary(value)
    jgen.writeStartObject()
    jgen.writeStringField("serId", serId.toString)
    jgen.writeStringField("serManifest", manifest)
    jgen.writeBinaryField("payload", serialized)
    jgen.writeEndObject()
  }
  */
}

final class ForySerializationDeserializer extends ActorSystemAccess {

  def serialization = SerializationExtension(currentSystem())

  /*
  def deserialize(jp: JsonParser, ctxt: DeserializationContext): AnyRef = {
    val codec: ObjectCodec = jp.getCodec()
    val jsonNode = codec.readTree[JsonNode](jp)
    val id = jsonNode.get("serId").textValue().toInt
    val manifest = jsonNode.get("serManifest").textValue()
    val payload = jsonNode.get("payload").binaryValue()
    serialization.deserialize(payload, id, manifest).get
  }
   */
}
