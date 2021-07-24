package com.esgi.fooders.ui.photo.presenter

import com.esgi.fooders.ui.photo.domain.PhotoContract


internal class PhotoPresenter : PhotoContract.Presenter {

    private var view: PhotoContract.View? = null
    private var counter = 0

    override fun bindView(view: PhotoContract.View) {
        this.view = view
        this.view?.updateRotationCounter(counter.toString())
    }

    override fun unbindView() {
        view = null
    }

    override fun onRotateClick() {
        counter += 90
        view?.rotate(90)
        if (counter == 360) counter = 0
        view?.updateRotationCounter(counter.toString())
    }
}
