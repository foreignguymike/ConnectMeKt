package distraction.com.connectme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import distraction.com.connectme.R
import distraction.com.connectme.utils.*
import distraction.com.connectme.views.GridListener
import kotlinx.android.synthetic.main.fragment_level.*

private const val KEY_LEVEL = "level"

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
        fun newInstance(level: Int) = LevelFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_LEVEL, level)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_level, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        best = getScore(context, level)

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
                saveScore(context, level, best)
            }
            showScore()
        }
    }

    private fun init() {
        grid.createGrid(data.numRows, data.numCols, data.grid)
        moves = 0
        movesTextView.text = resources.getString(R.string.moves_number, (moves).toString())
        starImage1.visibility = View.INVISIBLE
        starImage2.visibility = View.INVISIBLE
    }

    private fun showScore() {
        starImage1.visibility = View.VISIBLE
        starImage2.visibility = View.VISIBLE
        with(moves) {
            starImage2.visibility = if (this > 0) View.VISIBLE else View.INVISIBLE
            starImage1.visibility = if (this == data.target) View.VISIBLE else View.INVISIBLE
        }
        ScaleAnimation(
                50f, 1f, 50f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 200
            starImage1.startAnimation(this)
        }
        ScaleAnimation(
                50f, 1f, 50f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 200
            starImage2.startAnimation(this)
        }
    }
}
