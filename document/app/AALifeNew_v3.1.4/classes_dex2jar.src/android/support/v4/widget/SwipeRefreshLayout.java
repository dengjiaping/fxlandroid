package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;

public class SwipeRefreshLayout
  extends ViewGroup
{
  private static final float ACCELERATE_INTERPOLATION_FACTOR = 1.5F;
  private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0F;
  private static final int INVALID_POINTER = -1;
  private static final int[] LAYOUT_ATTRS = { 16842766 };
  private static final String LOG_TAG = SwipeRefreshLayout.class.getSimpleName();
  private static final float MAX_SWIPE_DISTANCE_FACTOR = 0.6F;
  private static final float PROGRESS_BAR_HEIGHT = 4.0F;
  private static final int REFRESH_TRIGGER_DISTANCE = 120;
  private static final long RETURN_TO_ORIGINAL_POSITION_TIMEOUT = 300L;
  private final AccelerateInterpolator mAccelerateInterpolator;
  private int mActivePointerId = -1;
  private final Animation mAnimateToStartPosition = new Animation()
  {
    public void applyTransformation(float paramAnonymousFloat, Transformation paramAnonymousTransformation)
    {
      int i = SwipeRefreshLayout.this.mFrom;
      int j = SwipeRefreshLayout.this.mOriginalOffsetTop;
      int k = 0;
      if (i != j) {
        k = SwipeRefreshLayout.this.mFrom + (int)(paramAnonymousFloat * (SwipeRefreshLayout.this.mOriginalOffsetTop - SwipeRefreshLayout.this.mFrom));
      }
      int m = k - SwipeRefreshLayout.this.mTarget.getTop();
      int n = SwipeRefreshLayout.this.mTarget.getTop();
      if (m + n < 0) {
        m = 0 - n;
      }
      SwipeRefreshLayout.this.setTargetOffsetTopAndBottom(m);
    }
  };
  private final Runnable mCancel = new Runnable()
  {
    public void run()
    {
      SwipeRefreshLayout.access$902(SwipeRefreshLayout.this, true);
      if (SwipeRefreshLayout.this.mProgressBar != null)
      {
        SwipeRefreshLayout.access$402(SwipeRefreshLayout.this, SwipeRefreshLayout.this.mCurrPercentage);
        SwipeRefreshLayout.this.mShrinkTrigger.setDuration(SwipeRefreshLayout.this.mMediumAnimationDuration);
        SwipeRefreshLayout.this.mShrinkTrigger.setAnimationListener(SwipeRefreshLayout.this.mShrinkAnimationListener);
        SwipeRefreshLayout.this.mShrinkTrigger.reset();
        SwipeRefreshLayout.this.mShrinkTrigger.setInterpolator(SwipeRefreshLayout.this.mDecelerateInterpolator);
        SwipeRefreshLayout.this.startAnimation(SwipeRefreshLayout.this.mShrinkTrigger);
      }
      SwipeRefreshLayout.this.animateOffsetToStartPosition(SwipeRefreshLayout.this.mCurrentTargetOffsetTop + SwipeRefreshLayout.this.getPaddingTop(), SwipeRefreshLayout.this.mReturnToStartPositionListener);
    }
  };
  private float mCurrPercentage = 0.0F;
  private int mCurrentTargetOffsetTop;
  private final DecelerateInterpolator mDecelerateInterpolator;
  private float mDistanceToTriggerSync = -1.0F;
  private int mFrom;
  private float mFromPercentage = 0.0F;
  private float mInitialMotionY;
  private boolean mIsBeingDragged;
  private float mLastMotionY;
  private OnRefreshListener mListener;
  private int mMediumAnimationDuration;
  private int mOriginalOffsetTop;
  private SwipeProgressBar mProgressBar;
  private int mProgressBarHeight;
  private boolean mRefreshing = false;
  private final Runnable mReturnToStartPosition = new Runnable()
  {
    public void run()
    {
      SwipeRefreshLayout.access$902(SwipeRefreshLayout.this, true);
      SwipeRefreshLayout.this.animateOffsetToStartPosition(SwipeRefreshLayout.this.mCurrentTargetOffsetTop + SwipeRefreshLayout.this.getPaddingTop(), SwipeRefreshLayout.this.mReturnToStartPositionListener);
    }
  };
  private final Animation.AnimationListener mReturnToStartPositionListener = new BaseAnimationListener(// INTERNAL ERROR //

/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.SwipeRefreshLayout
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */