package com.gitsurfer.gitsurf.data.network.models.response

import com.squareup.moshi.Json

data class OauthToken(
  @Json(name = "access_token") val accessToken: String,
  @Json(name = "scope") val scope: String
)