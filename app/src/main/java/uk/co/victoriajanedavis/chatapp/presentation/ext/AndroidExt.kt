package uk.co.victoriajanedavis.chatapp.presentation.ext

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Editable
import android.text.TextWatcher
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
import com.google.android.material.textfield.TextInputLayout


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


/* TextInputLayout/EditText Extensions */
fun EditText.isEmpty(): Boolean {
    return this.text.toString().isEmpty()
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
    })
}

fun TextInputLayout.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.editText?.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
    })
}

fun EditText.validate(errorMessage: String, validator: (String) -> Boolean) {
    this.afterTextChanged { s ->
        this.error = if(validator(s)) null else errorMessage
    }
    //this.error = if(validator(this.text.toString())) null else errorMessage
}

fun TextInputLayout.validate(errorMessage: String, validator: (String) -> Boolean) {
    this.editText?.afterTextChanged { s ->
        this.error = if(validator(s)) null else errorMessage
    }
    //this.error = if(validator(this.text.toString())) null else errorMessage
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

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    this.activity?.hideKeyboard()
}

fun Fragment.makeSnackbar(text: String, duration: Int) : Snackbar {
    this.view?.let { view ->
        return Snackbar.make(view, text, duration)
    }
}

fun Fragment.showSnackbar(text: String, duration: Int) {
    makeSnackbar(text, duration).show()
}