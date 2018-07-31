package distraction.com.connectme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import distraction.com.connectme.R
import distraction.com.connectme.utils.Res
import distraction.com.connectme.utils.getColorCompat
import distraction.com.connectme.utils.setBackgroundTint
import distraction.com.connectme.views.GridListener
import kotlinx.android.synthetic.main.fragment_level.*

private const val KEY_LEVEL = "level"
private const val KEY_BEST = "best"

class LevelFragment : BaseFragment(), GridListener {
    private val level by lazy {
        arguments.getInt(KEY_LEVEL)
    }
    private val data by lazy {
        Res.levelData!![level]
    }

    private var best = 0
    private var moves = 0

    companion object {
        fun newInstance(level: Int, best: Int = 0) = LevelFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_LEVEL, level)
                putInt(KEY_BEST, best)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        best = arguments.getInt(KEY_BEST)
        return inflater.inflate(R.layout.fragment_level, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headerTextView.text = resources.getString(R.string.level_number, data.level)
        targetTextView.text = resources.getString(R.string.target_number, data.target.toString())
        bestTextView.text = resources.getString(R.string.best_number, if (best > 0) best.toString() else "-")
        movesTextView.text = resources.getString(R.string.moves_number, moves.toString())

        grid.setBackgroundTint(context.getColorCompat(R.color.colorPrimary))

        refreshButton.setOnClickListener {
            init()
        }

        nextLevelButton.setOnClickListener {
            fragmentListener?.changeFragment(LevelFragment.newInstance(level + 1), false)
        }

        init()
        grid.setGridListener(this)
    }

    override fun onMove(arr: IntArray) {
        movesTextView.text = resources.getString(R.string.moves_number, (++moves).toString())
        if (grid.solved) {
            if (best == 0 || best > moves) {
                best = moves
                bestTextView.text = resources.getString(R.string.best_number, best.toString())
            }
        }
    }

    private fun init() {
        grid.createGrid(data.numRows, data.numCols, data.grid)
        moves = 0
        movesTextView.text = resources.getString(R.string.moves_number, (moves).toString())
    }
}
