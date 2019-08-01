package co.kubo.indiesco.adaptadores

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

/**
 * Created by estacion on 28/05/18.
 */

class PageAdapter(fm: FragmentManager, private val fragments: ArrayList<Fragment>)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}
