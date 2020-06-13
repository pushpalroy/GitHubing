package com.gitsurfer.gitsurf.injection.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.gitsurfer.gitsurf.BuildConfig
import com.gitsurfer.gitsurf.model.AppRepository
import com.gitsurfer.gitsurf.model.network.NetworkDataProvider
import com.gitsurfer.gitsurf.model.network.api.LoginApi
import com.gitsurfer.gitsurf.model.network.api.UserApi
import com.gitsurfer.gitsurf.model.roomdatabase.LocalDataProvider
import com.gitsurfer.gitsurf.model.roomdatabase.base.AppDatabase
import com.gitsurfer.gitsurf.model.utils.SharedPrefUtils
import com.gitsurfer.gitsurf.utils.BASE_URL
import com.gitsurfer.gitsurf.utils.ROOM_DATABASE_NAME
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {

  @Provides
  @Singleton
  fun provideAppRepository(
    networkDataProvider: NetworkDataProvider,
    localDataProvider: LocalDataProvider
  ) = AppRepository(
    networkDataProvider = networkDataProvider,
    localDataProvider = localDataProvider
  )

  @Provides
  @Singleton
  fun provideNetworkDataProvider(
    loginApi: LoginApi,
    userApi: UserApi
  ) = NetworkDataProvider(
    loginApi = loginApi,
    userApi = userApi
  )

  @Provides
  @Singleton
  fun provideLocalDataProvider(
    database: AppDatabase
  ): LocalDataProvider {
    return LocalDataProvider(
      appDatabase = database
    )
  }

  @Provides
  @Singleton
  fun provideLoginApi(okHttpClient: OkHttpClient): LoginApi {
    return getRetrofit(okHttpClient).create(LoginApi::class.java)
  }

  @Provides
  @Singleton
  fun provideUserApi(okHttpClient: OkHttpClient): UserApi {
    return getRetrofit(okHttpClient).create(UserApi::class.java)
  }

  @Provides
  @Singleton
  fun provideAppDatabase(
    @ApplicationContext context: Context
  ): AppDatabase {
    return Room.databaseBuilder(
      context,
      AppDatabase::class.java,
      ROOM_DATABASE_NAME
    )
      .build()
  }

  @Provides
  @Singleton
  fun provideSharedPrefUtils(
    sharedPreferences: SharedPreferences
  ): SharedPrefUtils {
    return SharedPrefUtils(sharedPreferences)
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
    val httpBuilder = OkHttpClient.Builder()
    when {
      BuildConfig.DEBUG -> {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpBuilder.interceptors()
          .add(httpLoggingInterceptor)
        httpBuilder.interceptors()
          .add(ChuckInterceptor(context))
      }
    }
    return httpBuilder.build()
  }

  private fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .add(Date::class.java, Rfc3339DateJsonAdapter())
      .build()

    return Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(okHttpClient)
      .build()
  }
}