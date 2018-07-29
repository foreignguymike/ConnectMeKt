package distraction.com.connectme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import distraction.com.connectme.R
import kotlinx.android.synthetic.main.fragment_title_screen.*

class TitleScreenFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = TitleScreenFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_title_screen, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startButton.setOnClickListener {
            fragmentListener?.changeFragment(LevelSelectFragment.newInstance())
        }
    }
}
