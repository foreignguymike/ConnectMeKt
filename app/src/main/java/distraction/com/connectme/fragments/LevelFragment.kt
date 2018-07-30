package distraction.com.connectme.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import distraction.com.connectme.R
import distraction.com.connectme.data.LevelData
import distraction.com.connectme.utils.*
import distraction.com.connectme.views.GridListener
import kotlinx.android.synthetic.main.fragment_level.*

private const val KEY_DATA = "data"

class LevelFragment : BaseFragment(), GridListener {
    private val data by lazy<LevelData> {
        arguments.let { it.getParcelable(KEY_DATA) }
    }

    private var moves: Int = 0

    companion object {
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
        movesTextView.text = resources.getString(R.string.moves_number, moves.toString())

        forEach(targetTextView, bestTextView, movesTextView, grid) {
            it.setBackgroundTint(context.getColorCompat(R.color.colorPrimary))
        }

        refreshButton.setOnClickListener {
            init()
        }

        init()
        grid.setGridListener(this)
    }

    override fun onMove(grid: IntArray) {
        movesTextView.text = resources.getString(R.string.moves_number, (++moves).toString())
        val solved = solved(to2D(grid, data.numRows, data.numCols), 3)
        Log.e("TAG", "solved = $solved")
    }

    fun init() {
        grid.createGrid(data.numRows, data.numCols, data.grid)
        moves = 0
        movesTextView.text = resources.getString(R.string.moves_number, (moves).toString())
    }
}
