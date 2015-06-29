/*
 * Code taken from jasta's five project:
 *   http://code.google.com/p/five/
 *
 * Much of this logic was taken from Romain Guy's Shelves project:
 *   http://code.google.com/p/shelves/
 */

package com.todayinfo.ui.component.fastscroll;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import com.todayinfo.ui.component.fastscroll.IdleListDetector.OnListIdleListener;

/**
 * Useful common implementation of OnListIdleListener which handles loading
 * images that temporarily defaulted during a fling. Utilizes a mem cache to
 * further enhance performance.
 */
public class IdleListener implements OnListIdleListener {

	private final static String TAG = "IdleListener";
	private final AbsListView mList;
	@SuppressWarnings("unused")
	private final int mThumbSize;

	public IdleListener(AbsListView list, int thumbSize) {
		mList = list;
		mThumbSize = thumbSize;
	}

	public void onListIdle() {
		final AbsListView list = mList;
		int n = list.getChildCount();
		Log.i(TAG, "IDLEING, downloading covers");
		// try to garbage collect before and after idling.
		System.gc();
		for (int i = 0; i < n; i++) {
			try {
				final View itemView = list.getChildAt(i);
				Object object = itemView.getTag();
				 if( object instanceof IViewHolder){
					 IViewHolder holder = (IViewHolder) object;
		               
					if (holder != null){
						holder.load();
					}
						
				}
				
			} catch (ClassCastException e) {
				Log.e(TAG, "Cannot cast view at index " + i
						+ " to AbstractItemView, class is "
						+ list.getChildAt(i).getClass().getSimpleName() + ".");
			}
		}
		System.gc();
	}
}