package distraction.com.connectme.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.GridLayout
import distraction.com.connectme.R
import distraction.com.connectme.utils.*
import kotlin.properties.Delegates

private const val SCREEN_PADDING_RATIO = 0.80f
private const val CELL_PADDING_RATIO = 0.05f
private const val DURATION = 100L

interface GridListener {
    fun onMove(grid: IntArray)
}

class Grid @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        GridLayout(context, attrs, defStyle), View.OnTouchListener {

    private var gridListener: GridListener? = null
    private lateinit var grid: IntArray

    init {
        if (context !is Activity) {
            throw RuntimeException("${this.javaClass.simpleName} must have an Activity as the context")
        }
    }

    fun setGridListener(gridListener: GridListener) {
        this.gridListener = gridListener
    }

    fun createGrid(numRows: Int, numCols: Int, grid: IntArray) {
        this.grid = grid.copyOf()

        removeAllViews()

        val size = (context as Activity).getScreenSize()
        val cellSize = Math.round(if (size.x < size.y) size.x * SCREEN_PADDING_RATIO else size.y * SCREEN_PADDING_RATIO) / numCols
        val padding = Math.round(cellSize * CELL_PADDING_RATIO)

        val cellParams = ViewGroup.MarginLayoutParams(cellSize, cellSize)
        val containerParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setPadding(padding)

        rowCount = numRows
        columnCount = numCols
        grid.forEachIndexed { index, it ->
            val view = View(context).apply {
                layoutParams = cellParams
                setBackgroundResource(R.drawable.rounded_rect)
                when (it) {
                    0 -> setBackgroundTint(context.getColorCompat(R.color.red))
                    1 -> setBackgroundTint(context.getColorCompat(R.color.green))
                    2 -> setBackgroundTint(context.getColorCompat(R.color.blue))
                }
            }
            val container = FrameLayout(context).apply {
                layoutParams = containerParams
                setPadding(padding)
                setOnTouchListener(this@Grid)
                tag = index
            }
            container.addView(view)
            addView(container)
        }
    }

    var selectedView: View? = null
    override fun onTouch(view: View?, e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                selectedView = view
                return true
            }
            MotionEvent.ACTION_UP -> {
                selectedView = null
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (selectedView == null) return false
                for (i in 0 until childCount) {
                    with(getChildAt(i)) {
                        if (view != this) {
                            containsPoint(e.rawX, e.rawY) {
                                swap(selectedView!!, this)
                                selectedView = null
                                return true
                            }
                        }
                    }
                }
                return true
            }
        }
        return false
    }

    fun swap(view1: View, view2: View) {
        val index1 = view1.tag as Int
        val index2 = view2.tag as Int
        removeView(view1)
        addView(view1, index2)
        removeView(view2)
        addView(view2, index1)
        TranslateAnimation(view1.x - view2.x, 0f, view1.y - view2.y, 0f).apply {
            duration = DURATION
            view1.startAnimation(this)
        }
        TranslateAnimation(view2.x - view1.x, 0f, view2.y - view1.y, 0f).apply {
            duration = DURATION
            view2.startAnimation(this)
        }
        view1.tag = index2
        view2.tag = index1

        val temp = grid[index1]
        grid[index1] = grid[index2]
        grid[index2] = temp
        gridListener?.onMove(grid)
    }

}