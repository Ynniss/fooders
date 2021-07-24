package com.esgi.fooders.ui.photo.domain

internal interface PhotoContract {
    interface View {
        fun updateRotationCounter(counter: String)
        fun rotate(counter: Int)
    }

    interface Presenter {
        fun bindView(view: View)
        fun unbindView()
        fun onRotateClick()
    }
}
