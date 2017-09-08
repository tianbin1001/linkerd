package io.buoyant.k8s.istio

import com.twitter.util.Duration
import io.buoyant.k8s.istio.mixer.MixerClient

trait IstioLoggerBase {

  def mixerClient: MixerClient

  //def report(istioPath: Option[Path], responseCode: Int, path: String, duration: Duration) = {
  def report(request: IstioRequest[_], response: IstioResponse, duration: Duration) = {

    mixerClient.report(
      response.responseCode,
      request.requestedPath,
      response.targetService,
      request.sourceLabel,
      request.targetLabel,
      response.responseDuration
    )
  }
}
