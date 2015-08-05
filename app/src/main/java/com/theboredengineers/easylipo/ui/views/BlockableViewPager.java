package com.theboredengineers.easylipo.ui.views;

/**
 * Created by Alex on 19/01/2015.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * @brief Non "Swipeable" viewpager used in ActivityNewBattery
 */
public class BlockableViewPager extends ViewPager {

    private Boolean blocked;

    public BlockableViewPager(Context context) {
        super(context);
    }

    public BlockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        blocked = attrs.getAttributeBooleanValue(null, "blocked", true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (blocked)
            return false;
        else
            return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (blocked)
            return false;
        else
            return super.onTouchEvent(event);
    }


    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}