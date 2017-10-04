package com.bonnetrouge.toonup.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bonnetrouge.toonup.Activities.BrowseActivity
import com.bonnetrouge.toonup.Adapters.SearchAdapter
import com.bonnetrouge.toonup.Commons.Ext.getDisplayWidth
import com.bonnetrouge.toonup.Commons.Ext.ifAdded
import com.bonnetrouge.toonup.Commons.Ext.lazyAndroid
import com.bonnetrouge.toonup.Delegates.SearchDelegate
import com.bonnetrouge.toonup.Fragment.BaseFragment
import com.bonnetrouge.toonup.Model.BasicSeriesInfo
import com.bonnetrouge.toonup.R
import com.bonnetrouge.toonup.UI.GridAutofitLayoutManager
import com.bonnetrouge.toonup.UI.RVItem
import com.bonnetrouge.toonup.ViewModels.BrowseViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment @Inject constructor() : BaseFragment() {

    val browseViewModel by lazyAndroid { ViewModelProviders.of(activity).get(BrowseViewModel::class.java) }

    val itemWidth = getDisplayWidth() / 3
    val searchAdapter by lazyAndroid { SearchAdapter(this, itemWidth) }

    var searchDelegate: SearchDelegate? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater?.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchRecyclerView.layoutManager = GridAutofitLayoutManager(itemWidth.toDouble())
        searchRecyclerView.adapter = searchAdapter
        popularRecyclerView()
    }

    fun popularRecyclerView() {
        searchDelegate?.getFilteredSearchResults("", browseViewModel) {
            searchAdapter.items.clear()
            searchAdapter.items.addAll(it)
            searchAdapter.notifyDataSetChanged()
        }
    }

    fun dispatchSearch(s: CharSequence) {
        ifAdded {
            searchDelegate?.getFilteredSearchResults(s, browseViewModel) {
                val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                            it[newItemPosition] == searchAdapter.items[oldItemPosition]

                    override fun getOldListSize() = searchAdapter.items.size

                    override fun getNewListSize() = it.size

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
                })
                searchAdapter.items.clear()
                searchAdapter.items.addAll(it)
                diffResult.dispatchUpdatesTo(searchAdapter)
            }
        }
    }

    override fun onRVItemClicked(item: RVItem, imageView: ImageView) {
        (activity as BrowseActivity).navigateDetail(item as BasicSeriesInfo, imageView)
    }
}