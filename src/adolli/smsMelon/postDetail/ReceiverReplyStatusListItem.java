/**
 * 
 */
package adolli.smsMelon.postDetail;

import adolli.activity.UnitsUtil;
import adolli.utility.IdGenerator;
import adolli.widget.listView.ExpandableListItem;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class ReceiverReplyStatusListItem extends ExpandableListItem
{

	private TextView tvReceiverName = null;
	private TextView tvRepliedContent = null;
	private TextView tvRepliedStatus = null;
	
	private final int _id_tvReceiverName = IdGenerator.getNewId();
	
	public ReceiverReplyStatusListItem(Context context)
	{
		super(context);
		setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		tvReceiverName = new TextView(getContext());
		tvReceiverName.setId(_id_tvReceiverName);
		tvReceiverName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		
		tvRepliedContent = new TextView(getContext());
		
		tvRepliedStatus = new TextView(getContext());
		tvRepliedStatus.setText("waiting");
		
		addView(tvReceiverName);
		addView(tvRepliedStatus);
		addView(tvRepliedContent);
		
		onFold();
	}

	
	public void setContent(String name, String repliedContent)
	{
		tvReceiverName.setText(name);
		tvRepliedContent.setText(repliedContent);
	}
	
	
	/* (non-Javadoc)
	 * @see adolli.widget.listView.ExpandableListItem#OnExpand()
	 */
	@Override
	public void onExpand()
	{
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.BELOW, tvReceiverName.getId());
		tvRepliedContent.setLayoutParams(rlp);
		
		RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp1.addRule(RelativeLayout.ALIGN_BOTTOM, tvReceiverName.getId());
		rlp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		tvRepliedStatus.setLayoutParams(rlp1);
		tvRepliedStatus.setVisibility(View.VISIBLE);
	}

	
	
	/* (non-Javadoc)
	 * @see adolli.widget.listView.ExpandableListItem#OnFold()
	 */
	@Override
	public void onFold()
	{
		final int dip_100 = UnitsUtil.dip2px(getContext(), 100);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rlp.addRule(RelativeLayout.ALIGN_BOTTOM, tvReceiverName.getId());
		rlp.setMargins(dip_100, 0, 0, 0);
		tvRepliedContent.setLayoutParams(rlp);
		tvRepliedContent.setLines(1);
		
		tvRepliedStatus.setVisibility(View.GONE);
	}

}
