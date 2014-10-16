/**
 * 
 */
package adolli.widget.listView;

import adolli.contacts.ContentNonContinuousSequenceMatchable;
import android.content.Context;
import android.widget.RelativeLayout;

/**
 * @author Administrator
 *
 */
public abstract class ListItem extends RelativeLayout implements ContentNonContinuousSequenceMatchable
{

	public ListItem(Context context)
	{
		super(context);
	}

	
}
