package com.example.lawrence.twittersearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

// class that defines dividers between items on a RecyclerView list.
public class ItemDivider extends RecyclerView.ItemDecoration{
    private final Drawable divider;

    // constructor will load Android's built-in divider
    public ItemDivider(Context context){
        int[] attrs  = {android.R.attr.listDivider};
        divider = context.obtainStyledAttributes(attrs).getDrawable(0);
    }

    // draws list item dividers onto RecyclerView
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state){
        super.onDrawOver(c, parent, state);

        // calc left/right x-coords for all dividers
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for( int i=0; i < parent.getChildCount()-1; ++i ){
            View item = parent.getChildAt(i);

            // calc top/bottom y-coords for current divider
            int top = item.getBottom() +
                ((RecyclerView.LayoutParams) item.getLayoutParams()).bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            // draw divider
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
