package distraction.com.connectme.fragments

import android.content.Context
import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

    interface FragmentListener {
        fun changeFragment(fragment: Fragment)
    }

    protected var fragmentListener: FragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is FragmentListener) {
            fragmentListener = context
        } else {
            throw RuntimeException("${context.toString()} must implement FragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentListener = null
    }

}