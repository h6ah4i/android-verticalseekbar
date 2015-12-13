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

/* This file contains AOSP code copied from /frameworks/base/core/java/android/widget/AbsSeekBar.java */
/*============================================================================*/
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*============================================================================*/

package com.h6ah4i.android.widget.verticalseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VerticalSeekBar extends AppCompatSeekBar {
    public static final int ROTATION_ANGLE_CW_90 = 90;
    public static final int ROTATION_ANGLE_CW_270 = 270;

    private boolean mIsDragging;
    private Drawable mThumb_;
    private Method mMethodSetProgress;
    private int mRotationAngle = ROTATION_ANGLE_CW_90;

    public VerticalSeekBar(Context context) {
        super(context);
        initialize(context, null, 0, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs, defStyle, 0);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalSeekBar, defStyleAttr, defStyleRes);
            final int rotationAngle = a.getInteger(R.styleable.VerticalSeekBar_seekBarRotation, 0);
            if (isValidRotationAngle(rotationAngle)) {
                mRotationAngle = rotationAngle;
            }
            a.recycle();
        }
    }

    @Override
    public void setThumb(Drawable thumb) {
        mThumb_ = thumb;
        super.setThumb(thumb);
    }

    private Drawable getThumbCompat() {
        return mThumb_;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (useViewRotation()) {
            return onTouchEventUseViewRotation(event);
        } else {
            return onTouchEventTraditionalRotation(event);
        }
    }

    private boolean onTouchEventTraditionalRotation(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        final Drawable mThumb = getThumbCompat();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                if (mThumb != null) {
                    // This may be within the padding region
                    invalidate(mThumb.getBounds());
                }
                onStartTrackingTouch();
                trackTouchEvent(event);
                attemptClaimDrag(true);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsDragging) {
                    trackTouchEvent(event);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    // Touch up when we never crossed the touch slop threshold
                    // should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    attemptClaimDrag(false);
                }
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate(); // see above explanation
                break;
        }
        return true;
    }

    private boolean onTouchEventUseViewRotation(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                attemptClaimDrag(true);
                break;

            case MotionEvent.ACTION_UP:
                attemptClaimDrag(false);
                break;
        }

        return super.onTouchEvent(event);
    }


    private void trackTouchEvent(MotionEvent event) {
        final int paddingTop = super.getPaddingTop();
        final int paddingBottom = super.getPaddingBottom();
        final int height = getHeight();

        final int available = height - paddingTop - paddingBottom;
        int y = (int) event.getY();

        final float scale;
        float value = 0;

        switch (mRotationAngle) {
            case ROTATION_ANGLE_CW_90:
                value = y - paddingTop;
                break;
            case ROTATION_ANGLE_CW_270:
                value = (height - paddingTop) - y;
                break;
        }

        if (value < 0 || available == 0) {
            scale = 0.0f;
        } else if (value > available) {
            scale = 1.0f;
        } else {
            scale = value / (float) available;
        }

        final int max = getMax();
        final float progress = scale * max;

        setProgress((int) progress, true);
    }

    /**
     * Tries to claim the user's drag motion, and requests disallowing any
     * ancestors from stealing events in the drag.
     */
    private void attemptClaimDrag(boolean active) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(active);
        }
    }

    /**
     * This is called when the user has started touching this widget.
     */
    private void onStartTrackingTouch() {
        mIsDragging = true;
    }

    /**
     * This is called when the user either releases his touch or the touch is
     * canceled.
     */
    private void onStopTrackingTouch() {
        mIsDragging = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isEnabled()) {
            final boolean handled;
            int direction = 0;

            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    direction = (mRotationAngle == ROTATION_ANGLE_CW_90) ? 1 : -1;
                    handled = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    direction = (mRotationAngle == ROTATION_ANGLE_CW_270) ? 1 : -1;
                    handled = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    handled = true;
                    break;
                default:
                    handled = false;
                    break;
            }

            if (handled) {
                final int keyProgressIncrement = getKeyProgressIncrement();
                int progress = getProgress();

                progress += (direction * keyProgressIncrement);

                if (progress >= 0 && progress <= getMax()) {
                    setProgress(progress - keyProgressIncrement, true);
                }

                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        if (!useViewRotation()) {
            refreshThumb();
        }
    }

    private synchronized void setProgress(int progress, boolean fromUser) {
        if (mMethodSetProgress == null) {
            try {
                Method m;
                m = this.getClass().getMethod("setProgress", int.class, boolean.class);
                m.setAccessible(true);
                mMethodSetProgress = m;
            } catch (NoSuchMethodException e) {
            }
        }

        if (mMethodSetProgress != null) {
            try {
                mMethodSetProgress.invoke(this, progress, fromUser);
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        } else {
            super.setProgress(progress);
        }
        refreshThumb();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (useViewRotation()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(heightMeasureSpec, widthMeasureSpec);

            final ViewGroup.LayoutParams lp = getLayoutParams();

            if (isInEditMode() && (lp != null) && (lp.height >= 0)) {
                setMeasuredDimension(super.getMeasuredHeight(), getLayoutParams().height);
            } else {
                setMeasuredDimension(super.getMeasuredHeight(), super.getMeasuredWidth());
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (useViewRotation()) {
            super.onSizeChanged(w, h, oldw, oldh);
        } else {
            super.onSizeChanged(h, w, oldh, oldw);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        if (!useViewRotation()) {
            switch (mRotationAngle) {
                case ROTATION_ANGLE_CW_90:
                    canvas.rotate(90);
                    canvas.translate(0, -super.getWidth());
                    break;
                case ROTATION_ANGLE_CW_270:
                    canvas.rotate(-90);
                    canvas.translate(-super.getHeight(), 0);
                    break;
            }
        }

        super.onDraw(canvas);
    }

    public int getRotationAngle() {
        return mRotationAngle;
    }

    public void setRotationAngle(int angle) {
        if (!isValidRotationAngle(angle)) {
            throw new IllegalArgumentException("Invalid angle specified :" + angle);
        }

        if (mRotationAngle == angle) {
            return;
        }

        mRotationAngle = angle;

        if (useViewRotation()) {
            VerticalSeekBarWrapper wrapper = getWrapper();
            if (wrapper != null) {
                wrapper.applyViewRotation();
            }
        } else {
            requestLayout();
        }
    }

    // refresh thumb position
    private void refreshThumb() {
        onSizeChanged(super.getWidth(), super.getHeight(), 0, 0);
    }

    /*package*/ boolean useViewRotation() {
        final boolean isSupportedApiLevel = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        final boolean inEditMode = isInEditMode();
        return isSupportedApiLevel && !inEditMode;
    }

    private VerticalSeekBarWrapper getWrapper() {
        final ViewParent parent = getParent();

        if (parent instanceof VerticalSeekBarWrapper) {
            return (VerticalSeekBarWrapper) parent;
        } else {
            return null;
        }
    }

    private static boolean isValidRotationAngle(int angle) {
        return (angle == ROTATION_ANGLE_CW_90 || angle == ROTATION_ANGLE_CW_270);
    }
}
