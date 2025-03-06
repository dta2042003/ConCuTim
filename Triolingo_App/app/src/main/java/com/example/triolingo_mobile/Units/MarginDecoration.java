package com.example.triolingo_mobile.Units;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int mSpacing;

    public MarginDecoration(int spacing) {
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % 2;

        outRect.left = column *mSpacing;
        outRect.right = (1 - column) * mSpacing;
        outRect.bottom = mSpacing;
    }
}