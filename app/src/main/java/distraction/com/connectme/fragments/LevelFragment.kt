package distraction.com.connectme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import distraction.com.connectme.R
import kotlinx.android.synthetic.main.fragment_level.*

private const val KEY_LEVEL = "level"

class LevelFragment : BaseFragment() {

    private var level: Int = 0

    companion object {
        @JvmStatic
        fun newInstance(level: Int) = LevelFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_LEVEL, level)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            level = it.getInt(KEY_LEVEL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_level, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        headerTextView.text = resources.getString(R.string.level_number, level)
        targetTextView.text = resources.getString(R.string.target_number, 4.toString())
        bestTextView.text = resources.getString(R.string.best_number, resources.getString(R.string.blank))
        movesTextView.text = resources.getString(R.string.moves_number, 0.toString())
    }
}
