package com.github.pjfanning.pekko.serialization.fory

import org.apache.fory.Fory
import org.apache.fory.memory.MemoryBuffer
import org.apache.fory.serializer.AbstractObjectSerializer
import org.apache.pekko.actor.{Address, AddressFromURIString}

class AddressSerializer(fory: Fory)
  extends AbstractObjectSerializer[Address](fory, classOf[Address]) {

  override def write(buffer: MemoryBuffer, address: Address): Unit = {
    fory.writeString(buffer, address.toString)
  }

  override def read(buffer: MemoryBuffer): Address = {
    AddressFromURIString(fory.readString(buffer))
  }
}
