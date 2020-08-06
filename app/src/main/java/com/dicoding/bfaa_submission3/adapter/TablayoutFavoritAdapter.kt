package com.dicoding.bfaa_submission3.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.bfaa_submission3.R
import com.dicoding.bfaa_submission3.activity.FollowersFavoritFragment
import com.dicoding.bfaa_submission3.activity.FollowingFavoritFragment

class TablayoutFavoritAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    var username: String? = null

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFavoritFragment().newInstance(username)
            1 -> fragment = FollowingFavoritFragment().newInstance(username)
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }
}