package com.example.gitsurfer.ui.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.gitsurfer.BR
import com.example.gitsurfer.utils.SnackBarAction
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel> : DaggerAppCompatActivity() {

  private var snackBar: Snackbar? = null
  lateinit var viewModel: VM
  lateinit var binding: B

  abstract fun getViewModelClass(): Class<VM>
  abstract fun getViewModelFactoryInstance(): ViewModelProvider.Factory
  abstract fun getLayoutId(): Int

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initView()
  }

  private fun initView() {
    viewModel = ViewModelProvider(this).get(getViewModelClass())
    binding = DataBindingUtil.setContentView(this, getLayoutId())
    binding.setVariable(BR.viewmodel, viewModel)
  }

  override fun onDestroy() {
    snackBar?.dismiss()
    super.onDestroy()
  }

  protected fun showSnackBarMessage(
    message: String?,
    duration: Int = Snackbar.LENGTH_SHORT
  ) {
    snackBar = snackBar?.let {
      message?.let { it1 -> it.setText(it1) }
      if (!it.isShown) {
        it.show()
      }
      it
    } ?: run {
      message?.let {
        snackBar = Snackbar.make(binding.root, it, duration)
        snackBar?.show()
        snackBar
      }
    }
  }

  protected fun showSnackBarWithAction(
    message: String,
    snackBarAction: SnackBarAction,
    duration: Int = Snackbar.LENGTH_SHORT
  ) {
    Snackbar.make(binding.root, message, duration)
        .setAction(snackBarAction.title, snackBarAction.listener)
        .show()
  }
}