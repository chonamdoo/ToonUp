package com.bonnetrouge.toonup.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bonnetrouge.toonup.Listeners.OnRecyclerViewItemClicked
import com.bonnetrouge.toonup.Model.BasicSeriesInfo
import com.bonnetrouge.toonup.R
import com.bonnetrouge.toonup.UI.MoviesAdapter
import com.bonnetrouge.toonup.ViewModels.BrowseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_browse_movies.*
import javax.inject.Inject

class BrowseMoviesFragment @Inject constructor(): Fragment(), OnRecyclerViewItemClicked {

	val moviesAdapter by lazy {	MoviesAdapter(this) }
	val browseViewModel by lazy { ViewModelProviders.of(activity).get(BrowseViewModel::class.java) }

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater?.inflate(R.layout.fragment_browse_movies, container, false)
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		browseMoviesRecyclerView.layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
		browseMoviesRecyclerView.adapter = moviesAdapter
		swipeRefreshLayout.setOnRefreshListener { refreshRecyclerView() }
		refreshRecyclerView()
	}

	fun refreshRecyclerView() {
		if (browseViewModel.popularMovies != null) {
			hideErrorMsg()
			moviesAdapter.itemList.addAll(browseViewModel.popularMovies!!)
			moviesAdapter.notifyDataSetChanged()
			swipeRefreshLayout.isRefreshing = false
		} else {
			browseViewModel.getPopularMoviesObservable()
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.doOnSubscribe {
						swipeRefreshLayout.isRefreshing = true
					}
					.subscribe({
						hideErrorMsg()
						moviesAdapter.itemList.addAll(it)
						moviesAdapter.notifyItemRangeInserted(0, it.size)
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

	override fun onRecyclerViewItemClicked(item: Any) {
		with (item as BasicSeriesInfo) {
			Toast.makeText(context, item.name, Toast.LENGTH_LONG).show()
		}
	}
}