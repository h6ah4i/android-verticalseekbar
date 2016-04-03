/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.h6ah4i.android.widget.verticalseekbar;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class VerticalSeekBarWrapper extends FrameLayout {
    public VerticalSeekBarWrapper(Context context) {
        this(context, null, 0);
    }

    public VerticalSeekBarWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSeekBarWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (useViewRotation()) {
            onSizeChangedUseViewRotation(w, h, oldw, oldh);
        } else {
            onSizeChangedTraditionalRotation(w, h, oldw, oldh);
        }
    }

    private void onSizeChangedTraditionalRotation(int w, int h, int oldw, int oldh) {
        final VerticalSeekBar seekBar = getChildSeekBar();

        if (seekBar != null) {
            final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) seekBar.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = h;
            seekBar.setLayoutParams(lp);

            seekBar.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int seekBarWidth = seekBar.getMeasuredWidth();
            seekBar.measure(
                    MeasureSpec.makeMeasureSpec(w, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));

            lp.gravity = Gravity.TOP | Gravity.LEFT;
            lp.leftMargin = (w - seekBarWidth) / 2;
            seekBar.setLayoutParams(lp);
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void onSizeChangedUseViewRotation(int w, int h, int oldw, int oldh) {
        final VerticalSeekBar seekBar = getChildSeekBar();

        if (seekBar != null) {
            seekBar.measure(
                    MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(w, MeasureSpec.AT_MOST));
        }

        applyViewRotation(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final VerticalSeekBar seekBar = getChildSeekBar();
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if ((seekBar != null) && (widthMode != MeasureSpec.EXACTLY)) {
            final int seekBarWidth;
            final int seekBarHeight;

            if (useViewRotation()) {
                seekBar.measure(heightMeasureSpec, widthMeasureSpec);
                seekBarWidth = seekBar.getMeasuredHeight();
                seekBarHeight = seekBar.getMeasuredWidth();
            } else {
                seekBar.measure(widthMeasureSpec, heightMeasureSpec);
                seekBarWidth = seekBar.getMeasuredWidth();
                seekBarHeight = seekBar.getMeasuredHeight();
            }

            final int measuredWidth = ViewCompat.resolveSizeAndState(seekBarWidth, widthMeasureSpec, 0);
            final int measuredHeight = ViewCompat.resolveSizeAndState(seekBarHeight, heightMeasureSpec, 0);

            setMeasuredDimension(measuredWidth, measuredHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /*package*/ void applyViewRotation() {
        applyViewRotation(getWidth(), getHeight());
    }

    private void applyViewRotation(int w, int h) {
        final VerticalSeekBar seekBar = getChildSeekBar();

        if (seekBar != null) {
            final ViewGroup.LayoutParams lp = seekBar.getLayoutParams();
            final int rotationAngle = seekBar.getRotationAngle();
            final int paddingTop = seekBar.getPaddingTop();
            final int paddingBottom = seekBar.getPaddingBottom();
            final int thumbOffset = seekBar.getThumbOffset();
            final int w2 = paddingTop + paddingBottom + thumbOffset * 2;

            lp.width = h;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            seekBar.setLayoutParams(lp);


            ViewCompat.setRotation(seekBar, toRotationAngleToDegrees(rotationAngle));
            ViewCompat.setTranslationX(seekBar, -(h - w) * 0.5f);
            ViewCompat.setTranslationY(seekBar, Math.max(0.0f, (h - Math.max(w, w2)) * 0.5f));
        }
    }

    private VerticalSeekBar getChildSeekBar() {
        final View child = (getChildCount() > 0) ? getChildAt(0) : null;
        return (child instanceof VerticalSeekBar) ? (VerticalSeekBar) child : null;
    }

    private boolean useViewRotation() {
        final VerticalSeekBar seekBar = getChildSeekBar();
        if (seekBar != null) {
            return seekBar.useViewRotation();
        } else {
            return false;
        }
    }

    private static int toRotationAngleToDegrees(int angle) {
        switch (angle) {
            case VerticalSeekBar.ROTATION_ANGLE_CW_90:
                return 90;
            case VerticalSeekBar.ROTATION_ANGLE_CW_270:
                return -90;
            default:
                return 0;
        }
    }
}
