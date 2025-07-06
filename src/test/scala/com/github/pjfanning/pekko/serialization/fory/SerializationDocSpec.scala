package com.github.pjfanning.pekko.serialization.fory

import com.typesafe.config.ConfigFactory
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.serialization.{Serialization, SerializationExtension, SerializerWithStringManifest, Serializers}
import org.apache.pekko.testkit.TestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

//#marker-interface
/**
 * Marker interface for messages, events and snapshots that are serialized with Fory.
 */
trait MySerializable

final case class Message(name: String, nr: Int) extends MySerializable
//#marker-interface

object SerializationDocSpec {
  /*
  val config =
    """
    #//#serialization-bindings
    pekko.actor {
      serialization-bindings {
        "com.myservice.MySerializable" = fory
      }
    }
    #//#serialization-bindings
  """
   */

  def verifySerialization(serialization: Serialization, obj: AnyRef): AnyRef = {
    val serializer = serialization.serializerFor(obj.getClass)
    val manifest = Serializers.manifestFor(serializer, obj)
    val serializerId = serializer.identifier
    val blob = serialization.serialize(obj).get
    val deserialized = serialization.deserialize(blob, serializerId, manifest).get
    deserialized
  }
}

object Polymorphism {

  // #polymorphism
  final case class Zoo(primaryAttraction: Animal) extends MySerializable

  sealed trait Animal

  final case class Lion(name: String) extends Animal

  final case class Elephant(name: String, age: Int) extends Animal
  // #polymorphism
}

class SerializationDocSpec
  extends TestKit(
    ActorSystem(
      "SerializationDocSpec",
      ConfigFactory.parseString(s"""
    pekko.actor {
      allow-java-serialization = off
      serialization-bindings {
        "${classOf[MySerializable].getName}" = fory
      }
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

  private def serializerFor(obj: Any): SerializerWithStringManifest =
    serialization.serializerFor(obj.getClass).asInstanceOf[SerializerWithStringManifest]

  "serialize trait + case classes" in {
    import Polymorphism._
    verifySerialization(Zoo(Lion("Simba"))) should ===(Zoo(Lion("Simba")))
    verifySerialization(Zoo(Elephant("Dumbo", 1))) should ===(Zoo(Elephant("Dumbo", 1)))
  }

}
