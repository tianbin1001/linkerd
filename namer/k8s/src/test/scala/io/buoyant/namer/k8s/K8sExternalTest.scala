package io.buoyant.namer.k8s

import com.twitter.finagle.Stack
import com.twitter.finagle.util.LoadService
import io.buoyant.config.Parser
import io.buoyant.config.types.Port
import io.buoyant.namer.{NamerConfig, NamerInitializer}
import org.scalatest.FunSuite

class K8sExternalTest extends FunSuite {

  test("sanity") {
    // ensure it doesn't totally blowup
    val _ = K8sExternalConfig(None, None).newNamer(Stack.Params.empty)
  }

  test("service registration") {
    assert(LoadService[NamerInitializer]().exists(_.isInstanceOf[K8sExternalInitializer]))
  }

  test("parse config") {
    val yaml = s"""
                  |kind: io.l5d.k8s.external
                  |experimental: true
                  |host: k8s-master.site.biz
                  |port: 80
      """.stripMargin

    val mapper = Parser.objectMapper(yaml, Iterable(Seq(K8sExternalInitializer)))
    val k8s = mapper.readValue[NamerConfig](yaml).asInstanceOf[K8sExternalConfig]
    assert(k8s.host == Some("k8s-master.site.biz"))
    assert(k8s.port == Some(Port(80)))
    assert(!k8s.disabled)
  }

  test("parse config without experimental param") {
    val yaml = s"""
                  |kind: io.l5d.k8s.external
                  |host: k8s-master.site.biz
                  |port: 80
      """.stripMargin

    val mapper = Parser.objectMapper(yaml, Iterable(Seq(K8sExternalInitializer)))
    val k8s = mapper.readValue[NamerConfig](yaml).asInstanceOf[K8sExternalConfig]
    assert(k8s.host == Some("k8s-master.site.biz"))
    assert(k8s.port == Some(Port(80)))
    assert(k8s.disabled)
  }
}