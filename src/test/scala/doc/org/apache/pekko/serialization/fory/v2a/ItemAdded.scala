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

package doc.org.apache.pekko.serialization.fory.v2a

import jdoc.org.apache.pekko.serialization.fory.MySerializable

// #add-optional
case class ItemAdded(shoppingCartId: String, productId: String, quantity: Int, discount: Option[Double], note: String)
    extends MySerializable {

  // alternative constructor because `note` should have default value "" when not defined in json
  def this(shoppingCartId: String, productId: String, quantity: Int, discount: Option[Double], note: Option[String]) =
    this(shoppingCartId, productId, quantity, discount, note.getOrElse(""))
}
// #add-optional
