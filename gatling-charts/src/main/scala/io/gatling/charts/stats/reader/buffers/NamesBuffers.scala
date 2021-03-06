/**
 * Copyright 2011-2015 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.charts.stats.reader.buffers

import scala.collection.mutable

import io.gatling.charts.stats.reader.{ GroupRecord, RequestRecord, UserRecord }
import io.gatling.core.stats.{ GroupStatsPath, RequestStatsPath, StatsPath }

private[reader] trait NamesBuffers {

  class NameBuffer[A] {

    val map = mutable.Map.empty[A, Int]

    def update(name: A, time: Int): Unit =
      map += (name -> (time min map.getOrElse(name, Int.MaxValue)))
  }

  val groupAndRequestsNameBuffer = new NameBuffer[StatsPath]
  val scenarioNameBuffer = new NameBuffer[String]

  def addScenarioName(record: UserRecord): Unit =
    scenarioNameBuffer.update(record.scenario, record.startBucket)

  def addRequestName(record: RequestRecord): Unit =
    groupAndRequestsNameBuffer.update(RequestStatsPath(record.name, record.group), record.startBucket)

  def addGroupName(record: GroupRecord): Unit =
    groupAndRequestsNameBuffer.update(GroupStatsPath(record.group), record.startBucket)
}
