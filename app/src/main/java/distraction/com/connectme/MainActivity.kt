package distraction.com.connectme

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import distraction.com.connectme.fragments.BaseFragment
import distraction.com.connectme.fragments.TitleScreenFragment
import distraction.com.connectme.utils.transaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BaseFragment.FragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.transaction(container.id, TitleScreenFragment.newInstance(), backstack = false, animate = false)
    }

    override fun changeFragment(fragment: Fragment, backstack: Boolean) {
        supportFragmentManager.transaction(container.id, fragment, backstack = backstack)
    }
}
