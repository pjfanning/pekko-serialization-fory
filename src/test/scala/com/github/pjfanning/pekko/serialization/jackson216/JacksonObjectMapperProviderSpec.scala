package com.github.pjfanning.pekko.serialization.jackson216

import com.fasterxml.jackson.core.util.JsonRecyclerPools.BoundedPool
import com.typesafe.config.ConfigFactory
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.testkit.TestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class JacksonObjectMapperProviderSpec extends TestKit(
  ActorSystem(
    "JacksonObjectMapperProviderSpec",
    ConfigFactory.parseString("""
                                 | pekko.serialization.jackson216 {
                                 |   read {
                                 |     max-nesting-depth = 1001
                                 |     max-number-length = 999
                                 |     max-string-length = 12345
                                 |     max-name-length = 234
                                 |     max-document-length = 567890
                                 |   }
                                 |   write {
                                 |     max-nesting-depth = 800
                                 |   }
                                 |   buffer-recycler {
                                 |     pool-instance = "bounded"
                                 |     bounded-pool-size = 1234
                                 |   }
                                 | }""".stripMargin)
      .withFallback(ConfigFactory.parseString(JacksonSerializerSpec.baseConfig("jackson-json")))))
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    shutdown()
  }

  "JacksonObjectMapperProvider" should {
    "pick up stream constraints" in {
      val objectMapper = JacksonObjectMapperProvider(system).create("test", None)
      val src = objectMapper.getFactory.streamReadConstraints()
      val swc = objectMapper.getFactory.streamWriteConstraints()
      src.getMaxNestingDepth shouldEqual 1001
      src.getMaxNumberLength shouldEqual 999
      src.getMaxNameLength shouldEqual 234
      src.getMaxStringLength shouldEqual 12345
      src.getMaxDocumentLength shouldEqual 567890

      swc.getMaxNestingDepth shouldEqual 800
    }
    "pick up recycler pool config" in {
      val objectMapper = JacksonObjectMapperProvider(system).create("test", None)
      val recyclerPool = objectMapper.getFactory._getRecyclerPool()
      recyclerPool.getClass.getSimpleName shouldEqual "BoundedPool"
      recyclerPool.asInstanceOf[BoundedPool].capacity() shouldEqual 1234
    }
  }

}
