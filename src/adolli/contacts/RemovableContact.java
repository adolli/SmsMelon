/**
 * 
 */
package adolli.contacts;

import adolli.activity.UnitsUtil;
import adolli.res.Pallet;
import adolli.smsMelon.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author Administrator
 *
 */
public class RemovableContact extends ContactListItem
{

	private ViewGroup parent = null;
	private ImageView removeButton = null;
	private OnRemovedListener removeListener;
	
	
	public RemovableContact(Context context)
	{
		super(context);
		init();
	}
	
	public RemovableContact(Context context, ViewGroup parent)
	{
		super(context);
		this.parent = parent;
		init();
	}
	
	
	private void init()
	{
		final int dip_24 = UnitsUtil.dip2px(getContext(), 24);
		final int dip_6 = UnitsUtil.dip2px(getContext(), 6);
		Bitmap btnBackground = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cross); 
		int sourceH = btnBackground.getHeight();
		int targetH = dip_24;
		float scale = (float) targetH / sourceH;
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);  
		Bitmap scaledBmp = Bitmap.createBitmap(btnBackground, 0, 0, btnBackground.getWidth(), btnBackground.getHeight(), matrix, true);
		removeButton = new ImageView(getContext());
		removeButton.setImageBitmap(scaledBmp);
		
		addView(removeButton);
		RelativeLayout.LayoutParams rlp1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rlp1.addRule(RelativeLayout.CENTER_VERTICAL);
		rlp1.setMargins(0, 0, dip_6, 0);
		removeButton.setLayoutParams(rlp1);
		removeButton.setClickable(true);
		removeButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 传递到该parentView中移除自己这个View
				removeSelf();
			}
		});
		removeButton.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN :
					RemovableContact.this.setBackgroundColor(Pallet.LTPINK);
					break;
				
				case MotionEvent.ACTION_MOVE :
				case MotionEvent.ACTION_UP :
					RemovableContact.this.setBackgroundColor(Color.TRANSPARENT);
					break;
					
				default :
					break;
				}
				return false;
			}
		});
	}


	
	
	
	/**
	 * @brief	如果有parent，则从parentView中移除自己
	 */
	public void removeSelf()
	{
		if (removeListener != null)
		{
			removeListener.onRemoved(this);
		}
		if (parent != null)
		{
			parent.removeView(this);
		}
	}
	
	
	
	
	public interface OnRemovedListener
	{
		 void onRemoved(RemovableContact rc);
	}
	
	
	public void setOnRemovedListener(OnRemovedListener l)
	{
		removeListener = l;
	}
	
	
}
