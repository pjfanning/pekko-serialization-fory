/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * license agreements; and to You under the Apache License, version 2.0:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * This file is part of the Apache Pekko project, which was derived from Akka.
 */

/*
 * Copyright (C) 2019-2022 Lightbend Inc. <https://www.lightbend.com>
 */

package doc.org.apache.pekko.serialization.fory.v2c

import com.github.pjfanning.pekko.serialization.fory.MySerializable

// #rename
case class ItemAdded(shoppingCartId: String, itemId: String, quantity: Int) extends MySerializable
// #rename
