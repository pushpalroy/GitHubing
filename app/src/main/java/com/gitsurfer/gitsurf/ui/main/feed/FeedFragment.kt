package com.gitsurfer.gitsurf.ui.main.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import com.gitsurfer.gitsurf.R
import com.gitsurfer.gitsurf.databinding.FragmentFeedBinding
import com.gitsurfer.gitsurf.ui.base.BaseFragment
import com.gitsurfer.gitsurf.ui.main.MainActivity
import com.gitsurfer.gitsurf.ui.main.MainViewModel
import com.gitsurfer.gitsurf.ui.main.feed.adapter.FeedAdapter

class FeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel, MainViewModel>() {

  private var fragmentView: View? = null
  private lateinit var feedAdapter: FeedAdapter

  override fun getViewModelClass() = FeedViewModel::class.java
  override fun getActivityViewModelClass(): Class<MainViewModel> = MainViewModel::class.java
  override fun getActivityViewModelOwner(): ViewModelStoreOwner = (activity as MainActivity)
  override fun getLayoutId() = R.layout.fragment_feed

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    if (fragmentView == null) {
      fragmentView = super.onCreateView(inflater, container, savedInstanceState)
      init()
    }
    return fragmentView
  }

  private fun init() {
    binding.viewModel = viewModel
    viewModel.loadPersonalFeed()
    listenToLiveData()
  }

  private fun listenToLiveData() {
    activity?.let { owner ->
      viewModel.feedListLiveData.observe(owner, Observer { feedList ->
        viewModel.updateAdapter(feedList)
      })
    }
  }
}