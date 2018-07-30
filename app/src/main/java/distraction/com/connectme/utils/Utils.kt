package distraction.com.connectme.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.View
import distraction.com.connectme.R

fun FragmentManager.transaction(layout: Int, f: Fragment, tag: String = f.javaClass.simpleName, backstack: Boolean = true, animate: Boolean = true) {
    beginTransaction()
            .apply {
                if (animate) setCustomAnimations(R.anim.enter_left, R.anim.exit_left, R.anim.enter_right, R.anim.exit_right)
                if (backstack) addToBackStack(tag)
            }
            .replace(layout, f)
            .commit()
}

fun View.setBackgroundTint(color: Int) {
    val drawable = background
    if (drawable is GradientDrawable) {
        drawable.setColor(color)
    }
}

inline fun View.containsPoint(x: Float, y: Float, f: (View) -> Unit) {
    val pos = IntArray(2).apply { getLocationInWindow(this) }
    if (x > pos[0] && x < pos[0] + width && y > pos[1] && y < pos[1] + height) {
        f(this)
    }
}

fun View.setPadding(padding: Int) {
    setPadding(padding, padding, padding, padding)
}

fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Activity.getScreenSize() = Point().apply { windowManager.defaultDisplay.getSize(this) }

inline fun <T> forEach(vararg elements: T, func: (T) -> Unit) = elements.forEach { func(it) }