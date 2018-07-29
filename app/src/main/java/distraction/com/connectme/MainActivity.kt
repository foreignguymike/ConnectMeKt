package distraction.com.connectme

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import distraction.com.connectme.fragments.BaseFragment
import distraction.com.connectme.fragments.TitleScreenFragment
import kotlinx.android.synthetic.main.activity_main.*

fun FragmentManager.transaction(layout: Int, f: Fragment, tag: String = f.javaClass.simpleName, backstack: Boolean = true, animate: Boolean = true) {
    beginTransaction()
            .also { if (animate) it.setCustomAnimations(R.anim.enter_left, R.anim.exit_left, R.anim.enter_right, R.anim.exit_right) }
            .replace(layout, f)
            .also { if (backstack) it.addToBackStack(tag) }
            .commit()
}

class MainActivity : AppCompatActivity(), BaseFragment.FragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.transaction(container.id, TitleScreenFragment.newInstance(), backstack = false, animate = false)
    }

    override fun changeFragment(fragment: Fragment) {
        supportFragmentManager.transaction(container.id, fragment)
    }
}
