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
import java.util.*

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

fun to2D(arr: IntArray, numRows: Int, numCols: Int): Array<IntArray> {
    val grid = Array(numRows) { IntArray(numCols) }
    arr.forEachIndexed { index, it ->
        val row = index / numCols
        val col = index % numCols
        grid[row][col] = it
    }
    return grid
}

fun solved(grid: Array<IntArray>, range: Int):Boolean {
    val numRows = grid.size
    val numCols = grid[0].size
    val visited = Array(numRows) { BooleanArray(numCols) }
    val checkedColors = BooleanArray(range)

    for (row in 0 until numRows) {
        for (col in 0 until numCols) {
            val type = grid[row][col]
            if (checkedColors[type] && !visited[row][col]) {
                return false
            }
            checkedColors[type] = true
            bfs(grid, visited, row, col)
        }
    }
    return true
}

private fun bfs(grid: Array<IntArray>, visited: Array<BooleanArray>, r: Int, c: Int) {
    var row = r
    var col = c
    val numRows = grid.size
    val numCols = grid[0].size
    val queue = LinkedList<Point>()
    queue.add(Point(row, col))
    visited[row][col] = true
    while (!queue.isEmpty()) {
        val currentCell = queue.poll()
        row = currentCell.x
        col = currentCell.y
        val `val` = grid[row][col]
        if (col > 0) {
            if (!visited[row][col - 1] && grid[row][col - 1] == `val`) {
                queue.add(Point(row, col - 1))
                visited[row][col - 1] = true
            }
        }
        if (col < numCols - 1 && grid[row][col + 1] == `val`) {
            if (!visited[row][col + 1]) {
                queue.add(Point(row, col + 1))
                visited[row][col + 1] = true
            }
        }
        if (row > 0 && grid[row - 1][col] == `val`) {
            if (!visited[row - 1][col]) {
                queue.add(Point(row - 1, col))
                visited[row - 1][col] = true
            }
        }
        if (row < numRows - 1 && grid[row + 1][col] == `val`) {
            if (!visited[row + 1][col]) {
                queue.add(Point(row + 1, col))
                visited[row + 1][col] = true
            }
        }
    }
}