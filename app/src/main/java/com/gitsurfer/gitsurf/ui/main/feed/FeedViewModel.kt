package com.gitsurfer.gitsurf.ui.main.feed

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gitsurfer.gitsurf.data.AppRepository
import com.gitsurfer.gitsurf.data.network.models.response.Feed
import com.gitsurfer.gitsurf.data.network.models.response.toRoomFeed
import com.gitsurfer.gitsurf.data.utils.SharedPrefUtils
import com.gitsurfer.gitsurf.ui.base.BaseViewModel
import com.gitsurfer.gitsurf.ui.main.feed.paging.FeedClickListener
import com.gitsurfer.gitsurf.ui.main.feed.paging.FeedDataSource.Companion.INITIAL_LOAD_SIZE_HINT
import com.gitsurfer.gitsurf.ui.main.feed.paging.FeedDataSource.Companion.PAGE_SIZE
import com.gitsurfer.gitsurf.ui.main.feed.paging.FeedDataSourceFactory
import com.gitsurfer.gitsurf.ui.main.feed.paging.FeedPagedListAdapter
import com.gitsurfer.gitsurf.utils.ui.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class FeedViewModel @ViewModelInject constructor(
  private val appRepository: AppRepository,
  prefUtils: SharedPrefUtils
) : BaseViewModel(), FeedClickListener {

  companion object {
    private const val TAG = "Feed"
  }

  var feedClickEvent = SingleLiveEvent<Pair<Feed, FragmentNavigator.Extras>>()
  private var feedPagedList: LiveData<PagedList<Feed>>
  val adapter: FeedPagedListAdapter = FeedPagedListAdapter(feedClickListener = this)

  private val _initialProgressLiveData = SingleLiveEvent<Boolean>()
  val initialProgressLiveData: SingleLiveEvent<Boolean>
    get() = _initialProgressLiveData

  private var noMoreItemsToLoad: Boolean = false

  private var feedDataSourceFactory = FeedDataSourceFactory(
    appRepository = appRepository,
    prefUtils = prefUtils,
    scope = viewModelScope,
    viewModel = this
  )

  init {
    val config = PagedList.Config.Builder()
      .setEnablePlaceholders(true)
      .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
      .setPageSize(PAGE_SIZE)
      .build()
    feedPagedList = LivePagedListBuilder(feedDataSourceFactory, config).build()
  }

  fun getFeed(): LiveData<PagedList<Feed>> = feedPagedList

  fun refresh() {
    feedDataSourceFactory.getFeedLiveDataSource().value?.invalidate()
  }

  internal fun updateInitialProgressLiveData(progress: Boolean) {
    _initialProgressLiveData.postValue(progress)
  }

  fun setNoMoreItemsToLoad(flag: Boolean) {
    noMoreItemsToLoad = flag
  }

  fun isNoMoreItemsToLoad(): Boolean {
    return noMoreItemsToLoad
  }

  fun updateAdapter(items: List<Feed>) {
    adapter.setItems(items)
    adapter.notifyDataSetChanged()
  }

  fun bookmarkFeed(position: Int) {
    viewModelScope.launch {
      adapter.getFeedItem(position)
        ?.let { roomFeed ->
          val res = appRepository.insertRoomFeed(
            roomFeed.toRoomFeed()
          )
          Timber.tag(TAG)
            .i(res.toString())
        }
    }
  }

  override fun onFeedClicked(
    position: Int,
    extras: FragmentNavigator.Extras
  ) {
    adapter.getFeedItem(position)?.let {
      feedClickEvent.postValue(Pair(it, extras))
    }
  }
}