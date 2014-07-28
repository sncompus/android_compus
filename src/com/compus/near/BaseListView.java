package com.compus.near;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

public class BaseListView extends ListView
{
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 120;

	private Context mContext;
	private int mMaxYOverscrollDistance;

	public BaseListView(Context context)
	{
		super(context);
		mContext = context;
		initBounceListView();
	}

	public BaseListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		initBounceListView();
	}

	public BaseListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext = context;
		initBounceListView();
	}

	private void initBounceListView()
	{

		final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		final float density = metrics.density;

		mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
	}

	@SuppressLint("NewApi")
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent)
	{
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
	}
	
}
