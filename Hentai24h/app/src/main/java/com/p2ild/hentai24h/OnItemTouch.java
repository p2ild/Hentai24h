package com.p2ild.hentai24h;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by duypi on 8/29/2016.
 */
public class OnItemTouch implements RecyclerView.OnItemTouchListener {
    private static final String TAG = OnItemTouch.class.getSimpleName();
    private final ItemClick itemClick;
    private final GestureDetector gestureDetector;
    private final ScaleGestureDetector scaleGestureDetector;
    private float scale = 1F;
    private Matrix matrix = new Matrix();

    public OnItemTouch(Context context, RecyclerView rcv, ItemClick itemClick) {
        this.itemClick = itemClick;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                scale = scale * scaleGestureDetector.getScaleFactor();
                scale = Math.max(0.1f, Math.min(scale, 5f));
                matrix.setScale(scale,scale);
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View viewChild = rv.findChildViewUnder(e.getX(), e.getY());
        if (itemClick != null && viewChild != null && gestureDetector.onTouchEvent(e)) {
            itemClick.onClick(viewChild, rv.getChildPosition(viewChild));
        }

        if (itemClick != null && viewChild != null && scaleGestureDetector.onTouchEvent(e)) {
            itemClick.onZoom(matrix);
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    public interface ItemClick {
        void onClick(View viewChild, int position);

        void onLongClick(View viewChild, int position);

        void onZoom(Matrix matrix);
    }
}
