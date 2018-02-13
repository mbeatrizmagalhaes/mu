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

import java.net.ServerSocket

import cats.Functor
import cats.syntax.functor._
import freestyle.rpc.common._
import freestyle.rpc.server._

import scala.util.{Failure, Success, Try}

trait CommonUtils {

  object database {

    val i: Int = 5
    val a1: A  = A(1, 2)
    val a2: A  = A(10, 20)
    val a3: A  = A(100, 200)
    val a4: A  = A(1000, 2000)
    val b1: B  = B(a1, a2)
    val c1: C  = C("foo1", a1)
    val c2: C  = C("foo2", a1)
    val e1: E  = E(a3, "foo3")
    val e2: E  = E(a4, "foo4")

    val aList = List(a1, a2)
    val cList = List(c1, c2)
    val eList = List(e1, e2)

    val dResult: D   = D(6)
    val dResult33: D = D(33)
  }

  def createChannelFor: ChannelFor = ChannelForAddress(SC.host, SC.port)

  def createChannelForPort(port: Int): ChannelFor =
    ChannelForAddress(SC.host, port)

  def createServerConf(grpcConfigs: List[GrpcConfig]): ServerW =
    ServerW.default(SC.port, grpcConfigs)

  def createServerConfOnRandomPort(grpcConfigs: List[GrpcConfig]): ServerW =
    ServerW.default(pickUnusedPort, grpcConfigs)

  def serverStart[F[_]: Functor](implicit S: GrpcServer[F]): F[Unit] =
    S.start().void

  def serverStop[F[_]: Functor](implicit S: GrpcServer[F]): F[Unit] =
    S.shutdownNow().void

  def debug(str: String): Unit =
    println(s"\n\n$str\n\n")

  implicit val S: monix.execution.Scheduler = monix.execution.Scheduler.Implicits.global

  val pickUnusedPort: Int =
    Try {
      val serverSocket: ServerSocket = new ServerSocket(0)
      val port: Int                  = serverSocket.getLocalPort
      serverSocket.close()
      port
    } match {
      case Success(s) => s
      case Failure(e) =>
        throw new RuntimeException(e)
    }
}
