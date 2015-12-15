package me.jp.bottominputlayout.view;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiangp on 15/12/1.
 */
public class SoftKeyboardStateHelper implements ViewTreeObserver.OnGlobalLayoutListener {

    public interface SoftKeyboardStateListener {
        void onSoftKeyboardOpened(int keyboardHeightInPx);

        void onSoftKeyboardClosed();
    }

    private final List<SoftKeyboardStateListener> mListeners = new LinkedList<>();
    private final View mActivityRootView;
    private int mLastSoftKeyboardHeightInPx;
    private boolean mIsSoftKeyboardOpened;

    public SoftKeyboardStateHelper(View activityRootView) {
        this(activityRootView, false);
    }

    public SoftKeyboardStateHelper(View activityRootView, boolean isSoftKeyboardOpened) {

        this.mActivityRootView = activityRootView;
        this.mIsSoftKeyboardOpened = isSoftKeyboardOpened;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private final int HEIGHT_OFF = 299;
    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        mActivityRootView.getWindowVisibleDisplayFrame(r);

        final int heightDiff = mActivityRootView.getRootView().getHeight()
                - (r.bottom - r.top);
        if (!mIsSoftKeyboardOpened && heightDiff > HEIGHT_OFF) {
            mIsSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened(heightDiff);
        } else if (mIsSoftKeyboardOpened && heightDiff < HEIGHT_OFF) {
            mIsSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
        }
    }

    public void setIsSoftKeyboardOpened(boolean isSoftKeyboardOpened) {
        this.mIsSoftKeyboardOpened = isSoftKeyboardOpened;
    }

    public boolean isSoftKeyboardOpened() {
        return mIsSoftKeyboardOpened;
    }

    /**
     * Default value is zero (0)
     *
     * @return last saved keyboard height in px
     */
    public int getLastSoftKeyboardHeightInPx() {
        return mLastSoftKeyboardHeightInPx;
    }

    public void addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        mListeners.add(listener);
    }

    public void removeSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        mListeners.remove(listener);
    }

    private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
        this.mLastSoftKeyboardHeightInPx = keyboardHeightInPx;
        for (SoftKeyboardStateListener listener : mListeners) {
            if (listener != null) {
                listener.onSoftKeyboardOpened(keyboardHeightInPx);
            }
        }
    }

    private void notifyOnSoftKeyboardClosed() {
        for (SoftKeyboardStateListener listener : mListeners) {
            if (listener != null) {
                listener.onSoftKeyboardClosed();
            }
        }
    }

    public void debug(String msg) {
        Log.i("debug", msg);
    }
}
