package co.kubo.indiesco.adaptadores

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
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
