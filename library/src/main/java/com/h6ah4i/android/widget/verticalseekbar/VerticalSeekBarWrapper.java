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
            final int hPadding = getPaddingLeft() + getPaddingRight();
            final int vPadding = getPaddingTop() + getPaddingBottom();
            final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) seekBar.getLayoutParams();

            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = Math.max(0, h - vPadding);
            seekBar.setLayoutParams(lp);

            seekBar.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

            final int seekBarMeasuredWidth = seekBar.getMeasuredWidth();
            seekBar.measure(
                    MeasureSpec.makeMeasureSpec(Math.max(0, w - hPadding), MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(Math.max(0, h - vPadding), MeasureSpec.EXACTLY));

            lp.gravity = Gravity.TOP | Gravity.LEFT;
            lp.leftMargin = (Math.max(0, w - hPadding) - seekBarMeasuredWidth) / 2;
            seekBar.setLayoutParams(lp);
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void onSizeChangedUseViewRotation(int w, int h, int oldw, int oldh) {
        final VerticalSeekBar seekBar = getChildSeekBar();

        if (seekBar != null) {
            final int hPadding = getPaddingLeft() + getPaddingRight();
            final int vPadding = getPaddingTop() + getPaddingBottom();
            seekBar.measure(
                    MeasureSpec.makeMeasureSpec(Math.max(0, h - vPadding), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(Math.max(0, w - hPadding), MeasureSpec.AT_MOST));
        }

        applyViewRotation(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final VerticalSeekBar seekBar = getChildSeekBar();
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if ((seekBar != null) && (widthMode != MeasureSpec.EXACTLY)) {
            final int seekBarWidth;
            final int seekBarHeight;
            final int hPadding = getPaddingLeft() + getPaddingRight();
            final int vPadding = getPaddingTop() + getPaddingBottom();
            final int innerContentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, widthSize - hPadding), widthMode);
            final int innerContentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, heightSize - vPadding), heightMode);

            if (useViewRotation()) {
                seekBar.measure(innerContentHeightMeasureSpec, innerContentWidthMeasureSpec);
                seekBarWidth = seekBar.getMeasuredHeight();
                seekBarHeight = seekBar.getMeasuredWidth();
            } else {
                seekBar.measure(innerContentWidthMeasureSpec, innerContentHeightMeasureSpec);
                seekBarWidth = seekBar.getMeasuredWidth();
                seekBarHeight = seekBar.getMeasuredHeight();
            }

            final int measuredWidth = ViewCompat.resolveSizeAndState(seekBarWidth + hPadding, widthMeasureSpec, 0);
            final int measuredHeight = ViewCompat.resolveSizeAndState(seekBarHeight + vPadding, heightMeasureSpec, 0);

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
            final boolean isLTR = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR;
            final int rotationAngle = seekBar.getRotationAngle();
            final int seekBarMeasuredWidth = seekBar.getMeasuredWidth();
            final int seekBarMeasuredHeight = seekBar.getMeasuredHeight();
            final int hPadding = getPaddingLeft() + getPaddingRight();
            final int vPadding = getPaddingTop() + getPaddingBottom();
            final float hOffset = (Math.max(0, w - hPadding) - seekBarMeasuredHeight) * 0.5f;
            final ViewGroup.LayoutParams lp = seekBar.getLayoutParams();

            lp.width = Math.max(0, h - vPadding);
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            seekBar.setLayoutParams(lp);

            ViewCompat.setPivotX(seekBar, (isLTR) ? 0 : Math.max(0, h - vPadding));
            ViewCompat.setPivotY(seekBar, 0);

            switch (rotationAngle) {
                case VerticalSeekBar.ROTATION_ANGLE_CW_90:
                    ViewCompat.setRotation(seekBar, 90);
                    if (isLTR) {
                        ViewCompat.setTranslationX(seekBar, seekBarMeasuredHeight + hOffset);
                        ViewCompat.setTranslationY(seekBar, 0);
                    } else {
                        ViewCompat.setTranslationX(seekBar, -hOffset);
                        ViewCompat.setTranslationY(seekBar, seekBarMeasuredWidth);
                    }
                    break;
                case VerticalSeekBar.ROTATION_ANGLE_CW_270:
                    ViewCompat.setRotation(seekBar, 270);
                    if (isLTR) {
                        ViewCompat.setTranslationX(seekBar, hOffset);
                        ViewCompat.setTranslationY(seekBar, seekBarMeasuredWidth);
                    } else {
                        ViewCompat.setTranslationX(seekBar, -(seekBarMeasuredHeight + hOffset));
                        ViewCompat.setTranslationY(seekBar, 0);
                    }
                    break;
            }
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
}
