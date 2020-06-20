package com.gitsurfer.gitsurf.data.network.models.response

import android.os.Parcelable
import com.gitsurfer.gitsurf.data.persistence.models.RoomActor
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Actor(
  @Json(name = "id") val id: String,
  @Json(name = "login") val login: String,
  @Json(name = "display_login") val displayLogin: String?,
  @Json(name = "gravatar_id") val gravatarId: String,
  @Json(name = "url") val profileApiUrl: String,
  @Json(name = "avatar_url") val avatarUrl: String
) : Parcelable

fun Actor.toRoomActor() = RoomActor(
  id, login, displayLogin, gravatarId, profileApiUrl, avatarUrl
)