package com.dc.sandeep.contactorganizer.com.dc.beans.contact.com.dc;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.dc.sandeep.contactorganizer.MainActivity;

/**
 * Created by Sandeep on 18-10-2015.
 */
public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 200;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private MainActivity mainActivity;

    public MyGestureDetector(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        try {
            float diffAbs = Math.abs(e1.getY() - e2.getY());
            float diff = e1.getX() - e2.getX();

            if (diffAbs > SWIPE_MAX_OFF_PATH)
                return false;

            // Left swipe
            if (diff > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mainActivity.onLeftSwipe();

                // Right swipe
            } else if (-diff > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mainActivity.onRightSwipe();
            }
        } catch (Exception e) {
            Log.e("YourActivity", "Error on gestures");
        }
        return false;

    }
}
