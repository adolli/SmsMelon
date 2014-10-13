/**
 * 
 */
package adolli.contacts;

import adolli.res.Pallet;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;

/**
 * @author 	Administrator
 * @brief	可选的联系人选项类，封装了一个联系人的样式并且单击可选
 */
public class SelectableContact extends ContactListItem
{

	private boolean selected = false;

	
	public SelectableContact(Context context)
	{
		super(context);
		
		// 只有设置了可点击后才可以捕获到ACTION_UP等事件
		setClickable(true);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		
		switch (event.getAction())
		{
		case MotionEvent.ACTION_UP :
			if (!selected)
			{
				select();
			}
			else
			{
				deSelect();
			}
			break;
			
		default :;
		}
		return super.onTouchEvent(event);
	}
	
	
	
	/**
	 * @brief	测试该联系人项目是否被选中
	 */
	public boolean isSelected()
	{
		return selected;
	}
	
	
	public void select()
	{
		selected = true;
		setBackgroundColor(Pallet.LTLTGREEN);
	}
	
	public void deSelect()
	{
		selected = false;
		setBackgroundColor(Color.TRANSPARENT);
	}
	
	
	
	
	
	
}
