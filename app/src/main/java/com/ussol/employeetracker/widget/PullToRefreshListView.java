package com.ussol.employeetracker.widget;
import com.ussol.employeetracker.R;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PullToRefreshListView  extends ListView {

    /**
     * The task will be executed when the list is pulled down.
     */
    private Runnable mReloadTask;

    /**
     * The callback handler will be called when {@link #mReloadTask} is done.
     */
    private OnRefreshDoneListener mOnRefreshDoneListener;

    private int mRefreshViewHeight;
    private View mRefreshLoading;
    private View mRefreshPullArrow;
    private View mRefreshView;
    private TextView mRefreshMessage;
    private boolean mRefreshViewMeasured = false;

    private boolean mAlreadySetupPullDownRefresher;
    private Interpolator mInterpolator;
    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;
    private boolean mGoingToStart;
    private boolean mGoingToStop;

    private int mStepMax;
    private int mStep;
    private int mStopPosition;
    private int mStartPosition;

    private int mRefreshState = PULL_TO_REFRESH;

    private static final int DURATION = 200;// in milliseconds
    private static final int INTERVAL = 10;// in milliseconds
    private static final int MSG_FLING_TO_DESTINATION = 0x0;
    private static final int MSG_STOP_LOADING = 0x1;
    private static final int MSG_START_LOADING = 0x2;

    private static final int PULL_TO_REFRESH = 1;
    private static final int RELEASE_TO_REFRESH = 2;
    private static final int REFRESHING = 3;

    public PullToRefreshListView(Context context) {
        super(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    private void initAnimations() {
        mInterpolator = new AccelerateDecelerateInterpolator();
        mFlipAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);
        mReverseFlipAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);
    }

    private void generateLoadingView() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mRefreshView = inflater.inflate(R.layout.pull_to_refresh_header, null);
        mRefreshLoading = mRefreshView
                .findViewById(R.id.pull_to_refresh_progress);
        mRefreshMessage = (TextView) mRefreshView
                .findViewById(R.id.pull_to_refresh_text);
        mRefreshPullArrow = mRefreshView
                .findViewById(R.id.pull_to_refresh_image);
    }

    /**
     * Setup & enable built-in pull-to-refresh for this list view.
     * 
     * @param task
     *            The runnable will be run on the background when pull down the
     *            list view.
     */
    public void setupPullDownRefresher(Runnable task) {
        if (mAlreadySetupPullDownRefresher || task == null) {
            return;
        }
        mAlreadySetupPullDownRefresher = true;

        mReloadTask = task;
        generateLoadingView();
        addHeaderView(mRefreshView);
        invalidate();
        setOnTouchListener(new DragWhenTouchListener());
        initAnimations();
        resetScroller();
    }

    /**
     * Register a callback to be called when the list is refreshed.
     */
    public void setOnRefreshDoneListener(OnRefreshDoneListener listener) {
        mOnRefreshDoneListener = listener;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        // need to reset the scroller
        resetScroller();
    }

    private void resetScroller() {
        scrollTo(0, mRefreshViewHeight);
        setSelection(0);
    }

    private void startLoading() {
        mHandler.removeMessages(MSG_START_LOADING);
        if (mRefreshState == REFRESHING) {
            return;
        }
        mRefreshState = REFRESHING;
        mRefreshLoading.setVisibility(View.VISIBLE);
        mRefreshPullArrow.clearAnimation();
        mRefreshPullArrow.setVisibility(View.GONE);
        mRefreshMessage.setText(getContext().getString(
                R.string.pull_to_refresh_loading));

        new RefreshDataTask(mReloadTask).execute();
    }

    /**
     * Only use this method to stop the loading effect of built-in
     * PullDownRefresher if you was passed <code>false</code> to
     * {@link #setupPullDownRefresher(Runnable, boolean)}
     */
    public void stopLoading() {
        mHandler.removeMessages(MSG_STOP_LOADING);
        if (mRefreshState != REFRESHING) {
            return;
        }
        mRefreshState = PULL_TO_REFRESH;
        mRefreshLoading.setVisibility(View.GONE);
        mRefreshPullArrow.setVisibility(View.VISIBLE);
        mRefreshMessage.setText(getContext().getString(
                R.string.pull_to_refresh_pull));
        mGoingToStop = true;
        initFlingState();
        mHandler.sendEmptyMessage(MSG_FLING_TO_DESTINATION);

        if (mOnRefreshDoneListener != null) {
            mOnRefreshDoneListener.onRefreshDone();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        if (mAlreadySetupPullDownRefresher) {
            resetScroller();
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mRefreshViewHeight = mRefreshView == null ? 0 : mRefreshView
                .getHeight();

        if (!mRefreshViewMeasured && mRefreshViewHeight > 0) {
            resetScroller();
        }

        mRefreshViewMeasured = mRefreshViewHeight > 0;

        if (mAlreadySetupPullDownRefresher) {
            heightMeasureSpec += mRefreshViewHeight;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (mAlreadySetupPullDownRefresher && mRefreshState != REFRESHING
                && !mGoingToStop) {
            resetScroller();
        }
    }

    private float getInterpolation(float input) {
        return mInterpolator.getInterpolation(input);
    }

    /**
     * Here we determine the destination where the list view must scroll to.
     */
    private void initFlingState() {
        mStep = 0;
        mStepMax = getStepsToPosition(DURATION);
        mStartPosition = -getScrollY();
        int top = getChildCount() == 0 ? 0 : getChildAt(0).getTop();

        if (mGoingToStop) {
            mStopPosition = -mRefreshViewHeight;
            return;
        }

        if (top >= 0 && mStartPosition >= 0) {
            mStopPosition = 0;
            mGoingToStart = true;
            startLoading();
        } else {
            mStopPosition = -mRefreshViewHeight;
        }
    }

    private int getStepsToPosition(int duration) {
        int frames = duration / INTERVAL;
        return frames;
    }

    private int getNextPosition() {
        float f = mStepMax;
        float progress = mStep / f;

        int position = 0;
        if (mStopPosition < mStartPosition) {
            position = mStartPosition
                    - (int) (Math.abs(mStopPosition - mStartPosition) * getInterpolation(progress));
        } else {
            position = mStartPosition
                    + (int) (Math.abs(mStopPosition - mStartPosition) * getInterpolation(progress));
        }

        mStep++;
        return position;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_FLING_TO_DESTINATION:
                // jump to the next position
                scrollTo(0, -getNextPosition());

                if (mStep <= mStepMax) {
                    // continue jumping to the another next position
                    mHandler.sendEmptyMessageDelayed(MSG_FLING_TO_DESTINATION,
                            INTERVAL);
                } else {
                    // yeah, arrive the destination, what's next?
                    mHandler.removeMessages(MSG_FLING_TO_DESTINATION);

                    if (mGoingToStart) {
                        // the user going to start loading by pulling down the
                        // list view
                        mGoingToStart = false;
                        mHandler.sendEmptyMessage(MSG_START_LOADING);
                    }
                    if (mGoingToStop) {
                        mGoingToStop = false;
                        resetScroller();
                    }
                }
                break;
            case MSG_STOP_LOADING:
                stopLoading();
                break;
            case MSG_START_LOADING:
                startLoading();
                break;
            default:
                break;
            }
        }
    };

    private class RefreshDataTask extends AsyncTask<Void, Void, Void> {
        Runnable run;

        public RefreshDataTask(Runnable run) {
            this.run = run;
        }

        @Override
        protected Void doInBackground(Void... args) {
            if (run != null) {
                run.run();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            onFinally();
        }

        @Override
        protected void onCancelled() {
            onFinally();
        }

        private void onFinally() {
            mHandler.sendEmptyMessage(MSG_STOP_LOADING);
        }
    }

    private class DragWhenTouchListener implements OnTouchListener {
        private int lastY;
        private boolean pressed;

        public boolean onTouch(View v, MotionEvent event) {
            // remove previous fling operator if any
            mHandler.removeMessages(MSG_FLING_TO_DESTINATION);

            if (mRefreshState == REFRESHING)
                return false;

            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // mark that the user press on the screen
                lastY = (int) event.getY();
                mGoingToStop = false;
                pressed = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int currentY = (int) event.getY();
                // sometimes, MotionEvent.ACTION_DOWN is consumed and we don't
                // have lastY value
                if (!pressed) {
                    pressed = true;
                    lastY = currentY;
                    break;
                }

                scrollBy(0, -(currentY - lastY) / 3);

                if (lastY < currentY && mRefreshState != RELEASE_TO_REFRESH) {
                    checkAndAnimateArrow();
                } else if (lastY > currentY && mRefreshState != PULL_TO_REFRESH) {
                    checkAndAnimateArrow();
                }
                lastY = currentY;
                mGoingToStop = false;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                pressed = false;

                // fling to the right position
                initFlingState();
                mHandler.sendEmptyMessage(MSG_FLING_TO_DESTINATION);
                break;
            }

            return false;
        }
    }

    private void checkAndAnimateArrow() {
        if (mRefreshState != REFRESHING) {
            mRefreshPullArrow.setVisibility(View.VISIBLE);
            if (getFirstVisiblePosition() == 0) {
                if (getScrollY() < 0 && mRefreshState != RELEASE_TO_REFRESH) {
                    mRefreshMessage.setText(R.string.pull_to_refresh_release);
                    mRefreshPullArrow.clearAnimation();
                    mRefreshPullArrow.startAnimation(mFlipAnimation);
                    mRefreshState = RELEASE_TO_REFRESH;
                } else if (getScrollY() < mRefreshViewHeight
                        && mRefreshState != PULL_TO_REFRESH) {
                    mRefreshMessage.setText(R.string.pull_to_refresh_pull);
                    mRefreshPullArrow.clearAnimation();
                    mRefreshPullArrow.startAnimation(mReverseFlipAnimation);
                    mRefreshState = PULL_TO_REFRESH;
                }
            }
        }
    }

    /**
     * Interface definition for a callback to be invoked when the built-in
     * pull-to-refresh has been refreshed the content of the list view.
     */
    public static interface OnRefreshDoneListener {
        /**
         * Called when the content is refreshed.
         */
        void onRefreshDone();
    }
}