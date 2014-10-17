/**
 * 
 */
package adolli.widget.listView;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * @author Administrator
 *
 */
public abstract class ExpandableListItem extends ListItem
{
	
	private boolean isExpanded = false;

	public ExpandableListItem(Context context)
	{
		super(context);
		setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		setClickable(true);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_UP :
			float x = event.getX();
			float y = event.getY();
			if (x > 0 && y > 0 && x < this.getWidth() && y < this.getHeight())
			{
				if (isExpanded)
				{
					onFold();
				}
				else
				{
					onExpand();
				}
				isExpanded = !isExpanded;
			}
			break;
			
		default :;
		}
		return super.onTouchEvent(event);
	}
	
	
	/**
	 * ListItem展开回调函数
	 */
	public abstract void onExpand();
	
	
	
	/**
	 * ListItem折叠回调函数
	 */
	public abstract void onFold();
	
	
	
	/* (non-Javadoc)
	 * @see adolli.contacts.ContentNonContinuousSequenceMatchable#match(java.lang.String)
	 */
	@Override
	public boolean match(String seq)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
