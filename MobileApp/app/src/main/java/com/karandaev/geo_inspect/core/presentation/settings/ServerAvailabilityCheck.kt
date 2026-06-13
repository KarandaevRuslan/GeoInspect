package com.karandaev.geo_inspect.core.presentation.settings

import kotlinx.coroutines.Job

internal class ServerAvailabilityCheck {

  var job: Job? = null
    private set

  var url: String? = null
    private set

  fun isActiveFor(serverBaseUrl: String): Boolean {
    return job?.isActive == true && url == serverBaseUrl
  }

  fun start(
    serverBaseUrl: String,
    job: Job
  ) {
    this.url = serverBaseUrl
    this.job = job
  }

  fun clear() {
    job = null
    url = null
  }

  fun cancel() {
    job?.cancel()
    clear()
  }
}