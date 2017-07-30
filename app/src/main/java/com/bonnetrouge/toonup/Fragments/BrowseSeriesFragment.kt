package com.bonnetrouge.toonup.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonnetrouge.toonup.R
import com.bonnetrouge.toonup.ViewModels.BrowseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_browse_series.*
import javax.inject.Inject
import com.bonnetrouge.toonup.UI.SeriesAdapter


class BrowseSeriesFragment @Inject constructor(): Fragment() {

	val seriesAdapter by lazy {
		SeriesAdapter(context)
	}
	val browseViewModel by lazy {
		ViewModelProviders.of(activity).get(BrowseViewModel::class.java)
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater?.inflate(R.layout.fragment_browse_series, container, false)
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		browseSeriesRecyclerView.layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
		browseSeriesRecyclerView.adapter = seriesAdapter
		swipeRefreshLayout.setOnRefreshListener {
			hideErrorMsg()
			populateRecyclerView()
		}
		populateRecyclerView()
	}

	fun populateRecyclerView() {
		if (browseViewModel.popularCartoons != null) {
			hideErrorMsg()
			seriesAdapter.itemList.addAll(browseViewModel.popularCartoons!!)
			seriesAdapter.notifyDataSetChanged()
			swipeRefreshLayout.isRefreshing = false
		} else {
			browseViewModel.getPopularCartoonObservable()
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.doOnSubscribe {
						swipeRefreshLayout.isRefreshing = true
					}
					.subscribe({
						seriesAdapter.itemList.addAll(it)
						seriesAdapter.notifyItemRangeInserted(0, it.size)
						swipeRefreshLayout.isRefreshing = false
					},{
						showErroMsg()
						swipeRefreshLayout.isRefreshing = false
					})
		}
	}

	fun showErroMsg() {
		errorMessage.visibility = View.VISIBLE
	}

	fun hideErrorMsg() {
		errorMessage.visibility = View.INVISIBLE
	}
}