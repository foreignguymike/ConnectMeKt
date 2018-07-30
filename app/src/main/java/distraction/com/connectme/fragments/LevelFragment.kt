package distraction.com.connectme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import distraction.com.connectme.R
import distraction.com.connectme.data.LevelData
import distraction.com.connectme.utils.forEach
import distraction.com.connectme.utils.getColorCompat
import distraction.com.connectme.utils.setBackgroundTint
import distraction.com.connectme.views.GridListener
import kotlinx.android.synthetic.main.fragment_level.*

private const val KEY_DATA = "data"

class LevelFragment : BaseFragment(), GridListener {
    private val data by lazy<LevelData> {
        arguments.let { it.getParcelable(KEY_DATA) }
    }

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
        movesTextView.text = resources.getString(R.string.moves_number, 0.toString())

        forEach(targetTextView, bestTextView, movesTextView, grid) {
            it.setBackgroundTint(context.getColorCompat(R.color.colorPrimary))
        }

        grid.createGrid(data.numRows, data.numCols, data.grid)
        grid.setGridListener(this)
    }

    override fun onMove(grid: IntArray) {

    }
}
