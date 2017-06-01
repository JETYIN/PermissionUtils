package com.work.service.permisson.component;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/5/31.创建可拉拽的布局
 */

public class DragerLinearLayout extends LinearLayout {

    private ViewDragHelper dragHelper;

    public DragerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                /**设置为true打开可拖拽的view**/
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                /**返回移动边界信息**/
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {

                return top;
            }

            /**当当前view需要消费事件，如使用button，textView设置onclick时需要重写方法**/
            @Override
            public int getViewHorizontalDragRange(View child) {
                Log.e("dylan", "" + getMeasuredWidth());
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                Log.e("dylan", "" + getMeasuredHeight());
                return getMeasuredHeight() - child.getMeasuredHeight();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
