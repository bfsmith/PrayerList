package bs.howdy.PrayerList.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class DragDropListView extends ListView {
	private int currentItemPosition;
	private ListDragDropListener ddListener;
	private ListFlingListener fListener;
	private boolean isDragging = false;
	private boolean isDraggable = false;
	private int mStartX;
	private int mStartY;
	private ImageView mDragImage;
	private int mDragOffsetY;
	
	public DragDropListView(Context context) {
		super(context);
	}
	
	public DragDropListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DragDropListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void addDragDropListener(ListDragDropListener listener) {
		ddListener = listener;
	}

	public void addFlingListener(ListFlingListener listener) {
		fListener = listener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		final int action = me.getAction();
		final int x = (int)me.getX();
		final int y = (int)me.getY();

		// Need to call super.onTouchEvent sometimes...
		if (!shouldHandle(x, y))
			return super.onTouchEvent(me);
		
		switch(action) {
			case MotionEvent.ACTION_DOWN:
				currentItemPosition = getPositionOfPoint(x, y);
				mStartX = x;
				mStartY = y;
				View v = getChild(currentItemPosition);
				
				mDragOffsetY = y - v.getTop();
				mDragOffsetY -= ((int)me.getRawY()) - y;
				isDraggable = x < v.getWidth()/4;
				isDragging = false;
				break;
			case MotionEvent.ACTION_MOVE:
				if(currentItemPosition >= 0) {
					v = getChild(currentItemPosition);
					if(isDraggable && !isDragging) {
						if(Math.abs(mStartY - y) > v.getHeight()/2) {
							isDragging = true;
							startDrag(v, x, y);
						}
					}
					if(isDragging)
						dragView(x, y);
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
			default:
				int newPosition = getPositionOfPoint(x, y);
				Log.i("Touch", "MOUSE UP: " + currentItemPosition + " : " + newPosition);
				if(currentItemPosition != newPosition) {
					if(isDraggable && isDragging)
						ddListener.drop(currentItemPosition, newPosition);
				}
				else {
					v = getChild(currentItemPosition);
					if(Math.abs(x - mStartX) > v.getWidth() / 2) {
						fListener.flung(currentItemPosition - getFirstVisiblePosition(), DIRECTION_RIGHT);
						Toast.makeText(getContext(), "Flung!", Toast.LENGTH_SHORT).show();
					}
				}
				stopDrag();
				isDragging = false;
				isDraggable = false;
				currentItemPosition = -1;
				break;
		}
		
		return true;
	}
	
	private int getPositionOfPoint(int x, int y) {
		int position = pointToPosition(x, y);
		return position;
	}
	
	private View getChild(int position) {
		return getChildAt(currentItemPosition - getFirstVisiblePosition());
	}
	
	private boolean shouldHandle(int x, int y) {
		if (currentItemPosition >= 0)
			return true;
		int position = pointToPosition(x, y) - getFirstVisiblePosition();
		if(position != INVALID_POSITION && getChildAt(position) != null && x < getChildAt(position).getWidth() / 4)
			return true;
		return false;
	}
	
	private void startDrag(View v, int x, int y) {
		stopDrag();
		
		v.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
		
		WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.gravity = Gravity.TOP;
        mWindowParams.x = 0;
        mWindowParams.y = y - mDragOffsetY;

        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.alpha = .8f;
        mWindowParams.windowAnimations = 0;
        
        Context context = getContext();
        mDragImage = new ImageView(context);
        mDragImage.setImageBitmap(bitmap);      

        WindowManager mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(mDragImage, mWindowParams);
        v.setVisibility(View.INVISIBLE);
	}
	
	private void dragView(int x, int y) {
		if(mDragImage == null)
			return;
		WindowManager.LayoutParams params = (WindowManager.LayoutParams)mDragImage.getLayoutParams();
		params.y = y - mDragOffsetY;
		WindowManager mWindowManager = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.updateViewLayout(mDragImage, params);
	}
	
	private void stopDrag() {
		if(currentItemPosition >= 0) {
			View v = getChild(currentItemPosition);
			v.setVisibility(View.VISIBLE);
		}
		
		if (mDragImage == null)
			return;
		
		mDragImage.setVisibility(GONE);
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.removeView(mDragImage);
        mDragImage.setImageDrawable(null);
        mDragImage = null;
	}
	
	public interface ListDragDropListener {
		public void drop(int startPosition, int endPosition);
	}
	
	public interface ListFlingListener {
		public void flung(int position, int direction);
	}
	
	public static final int DIRECTION_LEFT = 1;
	public static final int DIRECTION_RIGHT = 2;
}


