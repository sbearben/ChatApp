package uk.co.victoriajanedavis.chatapp.presentation.ext

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar


/**
 * Faster lazy delegation for Android.
 * Warning: Only use for objects accessed on main thread
 */
fun <T> lazyAndroid(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

/** Syntax helper to convert
 * data.observe(this, Observer<Int> { ... })
 * to
 * data.observe(this) { ... }
 */
fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T?) -> Unit) = observe(owner, Observer(observer))

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.disable() {
    this.isEnabled = false
}

fun View.enable() {
    this.isEnabled = true
}

fun EditText.isEmpty(): Boolean {
    return this.text.toString().isEmpty()
}

fun Fragment.setSupportActionBar(toolbar: Toolbar) {
    val activity: FragmentActivity? = this.activity
    if (activity is AppCompatActivity) {
        activity.setSupportActionBar(toolbar)
    }
}

fun Fragment.getSupportActionBar(): ActionBar? {
    val activity: FragmentActivity? = this.activity
    if (activity is AppCompatActivity) {
        return activity.supportActionBar
    }
    return null
}

fun Activity.isNetworkAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    this.activity?.hideKeyboard()
}

fun Fragment.showSnackbar(text: String, duration: Int) : Snackbar {
    this.view?.let { view ->
        return Snackbar.make(view, text, duration)
    }
}