/**
 * 
 */
package adolli.widget;

import android.content.Context;

/**
 * @author Administrator
 *
 */
public class PlaneButtonWithCounter extends PlaneButton
{

	private int count = 0;
	private String text = "";
	
	public PlaneButtonWithCounter(Context context)
	{
		super(context);
	}

	public void increase()
	{
		++count;
		updateText();
	}
	
	public void decrease()
	{
		--count;
		updateText();
	}
	
	public void resetToZero()
	{
		count = 0;
		updateText();
	}
	
	
	public void setCounter(int cnt)
	{
		count = cnt;
		updateText();
	}
	
	
	public int getCounter()
	{
		return count;
	}
	
	
	private void updateText()
	{
		setText(text + "(" + count + ")");
	}
	
	public void setTextContent(String str)
	{
		text = str;
	}
	
}
