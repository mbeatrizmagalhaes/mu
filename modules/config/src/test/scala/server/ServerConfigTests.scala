/*
 * Copyright 2017-2018 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package freestyle.rpc
package server
package config

import freestyle.tagless.config.implicits._
import freestyle.rpc.common.{ConcurrentMonad, SC}

import scala.concurrent.ExecutionContext

class ServerConfigTests extends RpcServerTestSuite {

  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

  "ServerConfig.Op" should {

    "load the port specified in the config file" in {

      val serverConf: ServerW =
        BuildServerFromConfig[ConcurrentMonad]("rpc.server.port").unsafeRunSync()

      serverConf.server.start().getPort shouldBe SC.port
      serverConf.server.shutdownNow().awaitTermination()
    }

    "load the default port when the config port path is not found" in {

      val serverConf: ServerW =
        BuildServerFromConfig[ConcurrentMonad]("rpc.wrong.path").unsafeRunSync()

      serverConf.server.start().getPort shouldBe defaultPort
      serverConf.server.shutdownNow().awaitTermination()
    }

  }
}
