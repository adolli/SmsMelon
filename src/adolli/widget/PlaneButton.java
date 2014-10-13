/**
 * 
 */
package adolli.widget;

import adolli.activity.UnitsUtil;
import adolli.res.Pallet;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * @author Administrator
 * @brief	扁平的按钮，功能上与按钮相同，更改了按钮的显示效果和交互效果
 */
public class PlaneButton extends Button
{

	private int backColor = Pallet.GREEN;
	private int backColorButtonDown = Pallet.LTGREEN;
	private int foreColor = Color.WHITE;
	
	public PlaneButton(Context context)
	{
		super(context);
		
		// 设置button的基本样式
		int dip_12 = UnitsUtil.dip2px(context, 10);
		setPadding(dip_12, dip_12, dip_12, dip_12);
		setBackgroundColor(backColor);
		setTextColor(foreColor);
		setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				1.0f);
		int dip_5 = UnitsUtil.dip2px(context, 5);
		lp.setMargins(dip_5, 0, dip_5, 0);
		setLayoutParams(lp);

	}

	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN :
			setBackgroundColor(backColorButtonDown);
			break;
			
		case MotionEvent.ACTION_UP :
			setBackgroundColor(backColor);
			break;

		default :;
		}
		return super.onTouchEvent(event);
	}
	
	
	
	void setForeColor(int color)
	{
		foreColor = color;
	}
	
	
	void setBackColor(int color, int color_press)
	{
		backColor = color;
		backColorButtonDown = color_press;
	}
	
	
}
