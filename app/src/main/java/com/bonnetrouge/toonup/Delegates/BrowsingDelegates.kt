package com.bonnetrouge.toonup.Delegates

import com.bonnetrouge.toonup.Model.BannerModel
import com.bonnetrouge.toonup.ViewModels.BrowseViewModel
import io.reactivex.Observable

sealed class DataFetchingDelegate {
    abstract fun fetchBrowsingData(loadingAction: () -> Unit, viewModel: BrowseViewModel, onSuccess: Observable<MutableList<BannerModel>>.() -> Unit, onFailure: () -> Unit)
}

class CartoonFetchingDelegate : DataFetchingDelegate() {
    override fun fetchBrowsingData(loadingAction: () -> Unit, viewModel: BrowseViewModel, onSuccess: Observable<MutableList<BannerModel>>.() -> Unit, onFailure: () -> Unit) {
        viewModel.fetchCartoons(loadingAction, onSuccess, onFailure)
    }
}

class MovieFetchingDelegate : DataFetchingDelegate() {
    override fun fetchBrowsingData(loadingAction: () -> Unit, viewModel: BrowseViewModel, onSuccess: Observable<MutableList<BannerModel>>.() -> Unit, onFailure: () -> Unit) {
        viewModel.fetchMovies(loadingAction, onSuccess, onFailure)
    }
}

class AnimeFetchingDelegate : DataFetchingDelegate() {
    override fun fetchBrowsingData(loadingAction: () -> Unit, viewModel: BrowseViewModel, onSuccess: Observable<MutableList<BannerModel>>.() -> Unit, onFailure: () -> Unit) {
        viewModel.fetchAnime(loadingAction, onSuccess, onFailure)
    }
}
