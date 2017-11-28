package com.azoft.carousellayoutmanager;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

/**
 * Class for centering items after scroll event.<br />
 * This class will listen to current scroll state and if item is not centered after scroll it will automatically scroll it to center.
 */
public class CenterScrollListener extends RecyclerView.OnScrollListener {

    private boolean mAutoSet = true;

    @Override
    public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof CarouselLayoutManager)) {
            mAutoSet = true;
            return;
        }

        final CarouselLayoutManager lm = (CarouselLayoutManager) layoutManager;
        if (!mAutoSet) {
            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                final int scrollNeeded = lm.getOffsetCenterView();
                /*
                if(scrollNeeded != 0){
                    float[] moveVaule = ((SpeedRecyclerView)recyclerView).getScrollMove();
                    float move = moveVaule[1] - moveVaule[0];
                    final int centerPosition = lm.getCenterItemPosition();
                    if(move > 0){//right
                        if(sco && centerPosition > 0){
                            recyclerView.smoothScrollToPosition(centerPosition - 1);
                        }else{
                            recyclerView.smoothScrollToPosition(centerPosition);
                        }
                    }else{//left
                        if(move < -60){
                            recyclerView.smoothScrollToPosition(centerPosition + 1);
                        }else{
                            recyclerView.smoothScrollToPosition(centerPosition);
                        }
                    }
                    ((SpeedRecyclerView)recyclerView).clearScrollMove();
                }*/
                if (CarouselLayoutManager.HORIZONTAL == lm.getOrientation()) {
                    if(scrollNeeded < 0){
                        recyclerView.smoothScrollBy(scrollNeeded, 0);
                    }else{
                        recyclerView.smoothScrollBy(scrollNeeded, 0);
                    }
                } else {
                    recyclerView.smoothScrollBy(0, scrollNeeded);
                }
                mAutoSet = true;
            }
        }
        if (RecyclerView.SCROLL_STATE_DRAGGING == newState || RecyclerView.SCROLL_STATE_SETTLING == newState) {
            mAutoSet = false;
        }
    }
}
