package com.example.mywearosapp.utils

import android.view.View

fun showView(view: View) {
    view.visibility = View.VISIBLE
}

fun removeView(view: View) {
    view.visibility = View.GONE
}

fun hideView(view: View) {
    view.visibility = View.INVISIBLE
}
