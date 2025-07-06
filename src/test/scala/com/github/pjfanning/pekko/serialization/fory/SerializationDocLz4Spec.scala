package com.github.pjfanning.pekko.serialization.fory

import com.typesafe.config.ConfigFactory
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.serialization.{Serialization, SerializationExtension}
import org.apache.pekko.testkit.TestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class SerializationDocLz4Spec
  extends TestKit(
    ActorSystem(
      "SerializationDocLz4Spec",
      ConfigFactory.parseString(s"""
    pekko.actor {
      allow-java-serialization = off
      serialization-bindings {
        "${classOf[jdoc.org.apache.pekko.serialization.fory.MySerializable].getName}" = fory
        "${classOf[MySerializable].getName}" = fory
      }
    }
    pekko.serialization.fory.compression {
      algorithm = lz4
      compress-larger-than = 0 KiB
    }
    """)))
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  val serialization: Serialization = SerializationExtension(system)

  override def afterAll(): Unit = {
    shutdown()
  }

  def verifySerialization(obj: AnyRef): AnyRef =
    SerializationDocSpec.verifySerialization(serialization, obj)

  "serialize trait + case classes" ignore {
    import Polymorphism._
    verifySerialization(Zoo(Lion("Simba"))) should ===(Zoo(Lion("Simba")))
    verifySerialization(Zoo(Elephant("Dumbo", 1))) should ===(Zoo(Elephant("Dumbo", 1)))
  }

}
