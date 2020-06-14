package com.gitsurfer.gitsurf.data.network.api

import com.gitsurfer.gitsurf.data.network.models.response.Feed
import com.gitsurfer.gitsurf.data.network.models.response.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

  /**
   * Lists public and private profile information when authenticated
   * through basic auth or OAuth with the user scope
   * @param authToken
   */
  @GET("/user")
  @Headers("Accept: application/json")
  suspend fun getUserInformation(
    @Header("Authorization") authToken: String
  ): Response<User>

  @GET("/users/{user}/received_events")
  @Headers("Accept: application/json")
  suspend fun getReceivedFeeds(
    @Header("Authorization") authToken: String?,
    @Path("user") user: String?,
    @Query("page") page: Int?,
    @Query("per_page") perPage: Int?
  ): Response<List<Feed>>
}