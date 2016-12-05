package com.olsplus.balancemall.component.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.olsplus.balancemall.R;


public class RectLinearLayout extends LinearLayout
{
    private float activeType = 1.0F;

    public RectLinearLayout(Context paramContext)
    {
        super(paramContext);
    }

    public RectLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        TypedArray arr = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CustomViewBounds);
        this.activeType = arr.getFloat(R.styleable.CustomViewBounds_widthToheight, 1.0F);
        arr.recycle();
    }

    private int measureHeight(int widthMeasureSpec, int heightMeasureSpec)
    {
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        int   result = 0;
        if (specMode != MeasureSpec.UNSPECIFIED) {
            if ((specMode == Integer.MIN_VALUE) || (specMode == 0))
            {
                if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
                    result = (int)(MeasureSpec.getSize(widthMeasureSpec) * this.activeType);
                }
            }
            else {
                return result;
            }
        }
        return specSize;
    }

    private int measureWidth(int widthMeasureSpec, int heightMeasureSpec)
    {
        int specMode  = MeasureSpec.getMode(widthMeasureSpec);
        int specSize  = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        if (specMode != MeasureSpec.UNSPECIFIED) {
            if ((specMode== Integer.MIN_VALUE) || (specMode == 0))
            {
                if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
                    result = (int)(MeasureSpec.getSize(heightMeasureSpec) * this.activeType);
                }
            }
            else {
                return result;
            }
        }
        return specSize;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec, heightMeasureSpec), measureHeight(widthMeasureSpec, heightMeasureSpec));
    }
}

