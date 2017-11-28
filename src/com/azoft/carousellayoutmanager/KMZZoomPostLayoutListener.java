package com.azoft.carousellayoutmanager;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

/**
 * KMZ special view layout.
 *
 * Implementation of {@link CarouselLayoutManager.PostLayoutListener} that makes interesting scaling of items. <br />
 * We are trying to make items scaling quicker for closer items for center and slower for when they are far away.<br />
 * Tis implementation uses atan function for this purpose.
 */
public class KMZZoomPostLayoutListener implements CarouselLayoutManager.PostLayoutListener {
    private final static String TAG = "KMZZoomPostLayout";
    @Override
    public ItemTransformation transformChild(@NonNull final View child, final float itemPositionToCenterDiff, final int orientation, final int maxVisibleItems) {
        //final float scale = (float) (3 * (2 * -StrictMath.atan(Math.abs(itemPositionToCenterDiff) + 1.0) / Math.PI + 0.9));

        // because scaling will make view smaller in its center, then we should move this item to the top or bottom to make it visible
        final float translateY;
        final float translateX;
        final float scale =(float)(1- Math.abs(itemPositionToCenterDiff)*0.05);
        if (CarouselLayoutManager.VERTICAL == orientation) {
            final float translateYGeneral = child.getMeasuredHeight() * (1 - scale) / 2f;
            translateY = Math.signum(itemPositionToCenterDiff) * translateYGeneral;
            translateX = 0;
        } else {
            float translateXGeneral = child.getMeasuredWidth() * (1 - scale) / 2f;
            translateXGeneral += (maxVisibleItems - (itemPositionToCenterDiff * Math.signum(itemPositionToCenterDiff))) * translateXGeneral;
            /*
            if (itemPositionToCenterDiff > 0) {
                translateXGeneral += (3 - itemPositionToCenterDiff) * translateXGeneral;
            } else if (itemPositionToCenterDiff < 0) {
                translateXGeneral += (3 + itemPositionToCenterDiff) * translateXGeneral;
            }
            */
            translateX = Math.signum(itemPositionToCenterDiff) * translateXGeneral;
            translateY = 0;
        }

        return new ItemTransformation(scale, scale, translateX, translateY);
    }
}
