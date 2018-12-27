package com.gxyj.base.listener

import android.support.v4.view.ViewPager

/**
 * @author LiZep
 * @time 2017/4/17 19:16
 * @desc TODO
 * @updateAuthor Author
 * @updateDate Date
 */

abstract class CommonPagerChangeListener : ViewPager.OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}
}
