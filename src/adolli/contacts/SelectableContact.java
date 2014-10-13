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
 * @brief	��ѡ����ϵ��ѡ���࣬��װ��һ����ϵ�˵���ʽ���ҵ�����ѡ
 */
public class SelectableContact extends ContactListItem
{

	private boolean selected = false;

	
	public SelectableContact(Context context)
	{
		super(context);
		
		// ֻ�������˿ɵ����ſ��Բ���ACTION_UP���¼�
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
	 * @brief	���Ը���ϵ����Ŀ�Ƿ�ѡ��
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
