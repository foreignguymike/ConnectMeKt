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

    override fun changeFragment(fragment: Fragment, backstack: Boolean, reverse: Boolean) {
        supportFragmentManager.transaction(container.id, fragment, backstack = backstack, reverse = reverse)
    }

    override fun onBackPressed() {
        with(supportFragmentManager) {
            if (fragments.size == 1 && findFragmentById(container.id) is TitleScreenFragment) {
                super.onBackPressed()
            } else if (fragments.size > 0) {
                beginTransaction().remove(findFragmentById(container.id)).commit()
                popBackStack()
                return
            }
        }
        super.onBackPressed()
    }
}
