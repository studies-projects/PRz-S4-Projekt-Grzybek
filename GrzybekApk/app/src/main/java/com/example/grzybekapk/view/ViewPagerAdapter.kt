package com.example.grzybekapk.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.grzybekapk.view.fragments.Tab2


class ViewPagerAdapter(fm: FragmentManager, internal var titles: Array<CharSequence>, internal var numbOfTabs: Int) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            CompactCalendarTab()
        } else {
            Tab2()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getCount(): Int {
        return numbOfTabs
    }
}