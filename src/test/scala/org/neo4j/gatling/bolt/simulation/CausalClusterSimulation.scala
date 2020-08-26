package org.neo4j.gatling.bolt.simulation

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import org.neo4j.driver.AccessMode
import org.neo4j.driver.GraphDatabase.driver
import org.neo4j.gatling.bolt.Predef._
import org.neo4j.gatling.bolt.builder.SessionHelper._

/** we connect to a neo4j causal cluster and make use of bolt+routing */
class CausalClusterSimulation extends Simulation {

//  val boltProtocol = bolt(driver("bolt+routing://localhost:7687"))
  val boltProtocol = bolt(driver("bolt://localhost:7687"))
  val testScenario = scenario("simpleCreate")
    .exec(
        // session with a default mode
        session(AccessMode.WRITE,
          transaction(
            cypher("create (n:Dummy) return n"),
            cypher("create (n:Dummy) return n")
          )
        )
    ).exec(
      session(AccessMode.WRITE, cypher("match (n:Dummy) return count(n) as x"))
    //.validateResult( (r:StatementResult) => true)
  )
    .pause(1)
  setUp(testScenario.inject(atOnceUsers(1))).protocols(boltProtocol)

}
