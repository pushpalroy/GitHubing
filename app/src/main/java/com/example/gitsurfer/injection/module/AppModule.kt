package com.example.gitsurfer.injection.module

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gitsurfer.BaseApplication
import com.example.gitsurfer.injection.factory.ViewModelFactory
import com.example.gitsurfer.injection.qualifiers.ApplicationContext
import com.example.gitsurfer.model.network.NetworkManager
import com.example.gitsurfer.utils.AUTH_DATA
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class AppModule {

  @Provides
  @Singleton
  fun provideViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory {
    return ViewModelFactory(creators = creators)
  }

  @Provides
  @Singleton
  @ApplicationContext
  fun provideAppContext(baseApplication: BaseApplication): Context {
    return baseApplication
  }

  @Provides
  @Singleton
  fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
    return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  }

  @Provides
  @Singleton
  fun provideNetworkManager(connectivityManager: ConnectivityManager): NetworkManager {
    return NetworkManager(connectivityManager)
  }

  @Provides
  @Singleton
  fun provideSharePrefs(context: BaseApplication): SharedPreferences {
    return context.getSharedPreferences(AUTH_DATA, Context.MODE_PRIVATE)
  }
}