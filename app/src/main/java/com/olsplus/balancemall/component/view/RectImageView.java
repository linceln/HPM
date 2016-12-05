package com.olsplus.balancemall.component.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.olsplus.balancemall.R;


public class RectImageView extends ImageView
{
    private float activeType = 1.0F;

    public RectImageView(Context paramContext)
    {
        super(paramContext);
    }

    public RectImageView(Context paramContext, AttributeSet paramAttributeSet)
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
        switch(specMode){
            case MeasureSpec.UNSPECIFIED:
                if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
                    result = (int)(MeasureSpec.getSize(widthMeasureSpec) * this.activeType);
                }

                break;
            case MeasureSpec.AT_MOST:
                result = specSize;
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
//        if (specMode != MeasureSpec.UNSPECIFIED) {
//            if ((specMode == Integer.MIN_VALUE) || (specMode == 0))
//            {
//                if (View.MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
//                    result = (int)(View.MeasureSpec.getSize(widthMeasureSpec) * this.activeType);
//                }
//            }
//            else {
//                return result;
//            }
//        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec, int heightMeasureSpec)
    {
        int specMode  = MeasureSpec.getMode(widthMeasureSpec);
        int specSize  = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        switch(specMode){
            case MeasureSpec.UNSPECIFIED:
                if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
                    result = (int)(MeasureSpec.getSize(heightMeasureSpec) * this.activeType);
                }
                break;
            case MeasureSpec.AT_MOST:
                result = specSize;
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
//        if (specMode != MeasureSpec.UNSPECIFIED) {
//            if ((specMode== Integer.MIN_VALUE) || (specMode == 0))
//            {
//                if (View.MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
//                    result = (int)(View.MeasureSpec.getSize(heightMeasureSpec) * this.activeType);
//                }
//            }
//            else {
//                return result;
//            }
//        }
        return specSize;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec, heightMeasureSpec), measureHeight(widthMeasureSpec, heightMeasureSpec));
    }

    public void setHeightToWidth(float paramFloat)
    {
        this.activeType = paramFloat;
        requestLayout();
    }

    public void setWidthToHeight(float paramFloat)
    {
        this.activeType = paramFloat;
        invalidate();
    }
}

