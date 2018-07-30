package distraction.com.connectme.fragments

import android.graphics.Point
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import distraction.com.connectme.R
import distraction.com.connectme.data.LevelData
import kotlinx.android.synthetic.main.fragment_level.*

private const val KEY_DATA = "data"
private const val SCREEN_PADDING_RATIO = 0.80f
private const val CELL_PADDING_RATIO = 0.05f

fun View.setBackgroundTint(color: Int) {
    val drawable = background
    if (drawable is GradientDrawable) {
        drawable.setColor(color)
    }
}

class LevelFragment : BaseFragment() {
    private val data by lazy<LevelData> {
        arguments.let { it.getParcelable(KEY_DATA) }
    }

    companion object {
        @JvmStatic
        fun newInstance(data: LevelData) = LevelFragment().apply {
            arguments = Bundle().apply { putParcelable(KEY_DATA, data) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_level, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headerTextView.text = resources.getString(R.string.level_number, data.level)
        targetTextView.text = resources.getString(R.string.target_number, data.target.toString())
        bestTextView.text = resources.getString(R.string.best_number, if (data.best > 0) data.best.toString() else "-")
        movesTextView.text = resources.getString(R.string.moves_number, 0.toString())

        targetTextView.setBackgroundTint(context.getColorCompat(R.color.colorPrimary))
        bestTextView.setBackgroundTint(context.getColorCompat(R.color.colorPrimary))
        movesTextView.setBackgroundTint(context.getColorCompat(R.color.colorPrimary))
        gridLayout.setBackgroundTint(context.getColorCompat(R.color.colorPrimary))

        createGrid(gridLayout, data.numRows, data.numCols, data.grid)
    }

    fun createGrid(layout: GridLayout, numRows: Int, numCols: Int, grid: IntArray) {
        layout.removeAllViews()

        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val cellSize = Math.round(if (size.x < size.y) size.x * SCREEN_PADDING_RATIO else size.y * SCREEN_PADDING_RATIO) / numCols
        val padding = Math.round(cellSize * CELL_PADDING_RATIO)

        val cellParams = ViewGroup.MarginLayoutParams(cellSize, cellSize).apply {
            leftMargin = padding
            rightMargin = padding
            topMargin = padding
            bottomMargin = padding
        }
        layout.setPadding(padding, padding, padding, padding)

        layout.rowCount = numRows
        layout.columnCount = numCols
        grid.forEachIndexed { index, _ ->
            val view = View(context).apply {
                layoutParams = cellParams
                setBackgroundResource(R.drawable.rounded_rect)
                when (grid[index]) {
                    0 -> setBackgroundTint(context.getColorCompat(R.color.red))
                    1 -> setBackgroundTint(context.getColorCompat(R.color.green))
                    2 -> setBackgroundTint(context.getColorCompat(R.color.blue))
                }
            }
            layout.addView(view, index)
        }
    }
}
