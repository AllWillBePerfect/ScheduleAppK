package com.schedulev2.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.abs

class CustomSwipeRefreshLayout : SwipeRefreshLayout {
    private var mTouchSlop = 0
    private var mPrevX = 0f

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mPrevX = MotionEvent.obtain(ev).x
            }

            MotionEvent.ACTION_MOVE -> {
                val eventX = ev.x
                val xDiff = abs(eventX - mPrevX)
                if (xDiff > mTouchSlop) return false
            }

        }
        return super.onInterceptTouchEvent(ev)
    }
}