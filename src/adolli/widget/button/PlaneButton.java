/**
 * 
 */
package adolli.widget.button;

import adolli.activity.UnitsUtil;
import adolli.res.Pallet;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
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
		init();
	}

	
	public PlaneButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}
	
	public PlaneButton(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		init();
	}
	
	
	private void init()
	{
		// 设置button的基本样式
		int dip_12 = UnitsUtil.dip2px(getContext(), 10);
		setPadding(dip_12, dip_12, dip_12, dip_12);
		setBackgroundColor(backColor);
		setTextColor(foreColor);
		setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		int dip_5 = UnitsUtil.dip2px(getContext(), 5);
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
			
		case MotionEvent.ACTION_MOVE :
			// move the finger out of the button then release the button
			if (event.getX() < 0 || event.getX() > this.getWidth()
					|| event.getY() < 0 || event.getY() > this.getHeight())
			{
				setBackgroundColor(backColor);
			}
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
