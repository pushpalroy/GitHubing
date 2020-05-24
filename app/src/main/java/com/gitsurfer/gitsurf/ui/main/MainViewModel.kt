package com.gitsurfer.gitsurf.ui.main

import com.gitsurfer.gitsurf.model.AppRepository
import com.gitsurfer.gitsurf.model.network.NetworkManager
import com.gitsurfer.gitsurf.model.utils.SharedPrefUtils
import com.gitsurfer.gitsurf.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
  private val appRepository: AppRepository,
  private val networkManager: NetworkManager,
  private val prefUtils: SharedPrefUtils
) : BaseViewModel() {

  fun isAuthorized(): Boolean {
    return prefUtils.authToken != null
  }
}