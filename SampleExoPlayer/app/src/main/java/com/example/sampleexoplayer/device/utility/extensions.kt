package com.example.sampleexoplayer.device.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.sampleexoplayer.R
import com.example.sampleexoplayer.domain.Filter
import com.example.sampleexoplayer.domain.VideoLists
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun showSnackBar(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
}

fun showSnackBarLong(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
}

fun TextView.setCurrencyText(price: Double) {
    val indianLocale = Locale("en", "IN")
    val nf = NumberFormat.getCurrencyInstance(indianLocale)
    nf.minimumFractionDigits = 0
    nf.maximumFractionDigits = 0
    this.text = nf.format(price)
}

fun getCurrencyText(price: Double): String {
    val indianLocale = Locale("en", "IN")
    val nf = NumberFormat.getCurrencyInstance(indianLocale)
    nf.minimumFractionDigits = 0
    nf.maximumFractionDigits = 0
    return nf.format(price)
}

fun getCurrencyText(price: Float): String {
    val indianLocale = Locale("en", "IN")
    val nf = NumberFormat.getCurrencyInstance(indianLocale)
    nf.minimumFractionDigits = 0
    nf.maximumFractionDigits = 0
    return nf.format(price)
}

fun Context.showToastInCenter(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).also {
        it.setGravity(Gravity.CENTER, 0, 0)
    }.show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invis() {
    visibility = View.INVISIBLE
}


fun ImageView.loadImage(
    url: String?,
    previousUrl: String? = null,
    isCiruclar: Boolean = false,
    cornersRadius: Int = 0,
    centerCrop: Boolean = false
) {

    val requestOptions = when {
        isCiruclar -> RequestOptions.circleCropTransform()

        cornersRadius > 0 -> {
            RequestOptions().transform(
                if (centerCrop) CenterCrop() else FitCenter(),
                RoundedCorners(cornersRadius)
            )
        }

        else -> RequestOptions().dontTransform()
    }

    Glide.with(context)
        .load(url ?: "").apply(requestOptions)
        .let { req ->
            if (previousUrl != null) {
                req.thumbnail(
                    Glide.with(context).load(previousUrl).apply(requestOptions)
                )
            } else {
                req
            }
        }.signature(ObjectKey(System.currentTimeMillis()))
        .error(R.drawable.ic_photo)
        .into(this)
}

fun EditText.getString() = text.toString()
fun EditText.getDouble() = text.toString().toDouble()

fun TextView.getString() = text.toString()
fun TextView.getDouble() = text.toString().toDouble()

fun EditText.onTextChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = cb(s.toString())
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun getYesNo(): ArrayList<String> {
    return object : ArrayList<String>() {
        init {
            add("Yes")
            add("No")
        }
    }
}

fun Double.toDoubleFormat(): String {
    val pattern = "#,###.00"
    val decimalFormat = DecimalFormat(pattern)
    decimalFormat.groupingSize = 3

    return decimalFormat.format(this)
}

fun Double.toPrice(): String {
    val pattern = "#,###.00"
    val decimalFormat = DecimalFormat(pattern)
    decimalFormat.groupingSize = 3

    return "₹" + decimalFormat.format(this)
}

val String.containsDigit: Boolean
    get() = matches(Regex(".*[0-9].*"))

val String.isAlphanumeric: Boolean
    get() = matches(Regex("[a-zA-Z0-9]*"))

fun AppCompatActivity.callTo(phoneNumber: String, requestCode: Int) {
    val intent = Intent(Intent.ACTION_CALL)

    intent.data = Uri.parse("tel:$phoneNumber")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOfNulls<String>(1)
            permissions[0] = Manifest.permission.CALL_PHONE
            requestPermissions(permissions, requestCode)
        } else {
            startActivity(intent)
        }
    } else {
        startActivity(intent)
    }
}

val String.asUri: Uri?
    get() = try {
        if (URLUtil.isValidUrl(this))
            Uri.parse(this)
        else
            null
    } catch (e: Exception) {
        null
    }

fun Any?.isNull() = this == null
fun Any?.isNotNull() = this != null

fun hideNavigation(context: Activity) {
    val decorView = context.window.decorView
    decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
}

fun getVideoList(): ArrayList<VideoLists> {
    return object : ArrayList<VideoLists>() {
        init {
            add(
                VideoLists(
                    "NEFFEX - Cold",
                    "https://https://youtu.be/WzQBAc8i73E",
                    "https://i.ytimg.com/vi/WzQBAc8i73E/maxresdefault.jpg"
                )
            )
            add(
                VideoLists(
                    "NEFFEX - Grateful",
                    "https://youtu.be/83RUhxsfLWs",
                    "https://i.ytimg.com/vi/83RUhxsfLWs/maxresdefault.jpg"
                )
            )
            add(
                VideoLists(
                    "NEFFEX - Go!",
                    "https://www.youtube.com/watch?v=X5cfg26vkOQ",
                    "https://i.ytimg.com/vi/X5cfg26vkOQ/maxresdefault.jpg"
                )
            )
            add(
                VideoLists(
                    "NEFFEX - Coming For You",
                    "https://www.youtube.com/watch?v=xPi1O8g8zII",
                    "https://i.ytimg.com/vi/iPkMOT8JYBU/maxresdefault.jpg"
                )
            )
            add(
                VideoLists(
                    "NEFFEX - Hype",
                    "https://www.youtube.com/watch?v=DcfVqJV8-YM",
                    "https://i.ytimg.com/vi/DcfVqJV8-YM/maxresdefault.jpg"
                )
            )
            add(
                VideoLists(
                    "NEFFEX - Best Of Me",
                    "https://www.youtube.com/watch?v=A4LiP8WFuG0",
                    "https://i.ytimg.com/vi/A4LiP8WFuG0/maxresdefault.jpg"
                )
            )
        }
    }
}

fun getVideoQuality(): ArrayList<Filter> {
    return object : ArrayList<Filter>() {
        init {
            add(Filter(160, "144p"))
            add(Filter(133, "240p"))
            add(Filter(134, "360p", true))
            add(Filter(135, "480p"))
            add(Filter(136, "720p"))
            add(Filter(137, "1080p"))
            add(Filter(264, "1440p"))
            add(Filter(266, "2160p"))
        }
    }
}

fun getAudioQuality(): ArrayList<Filter> {
    return object : ArrayList<Filter>() {
        init {
            add(Filter(140, "128", true))
            add(Filter(141, "256"))
            add(Filter(256, "192"))
            add(Filter(258, "384"))
        }
    }
}