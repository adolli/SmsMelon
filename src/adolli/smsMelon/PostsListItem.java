/**
 * 
 */
package adolli.smsMelon;

import adolli.activity.UnitsUtil;
import adolli.utility.IdGenerator;
import adolli.widget.listView.ListItem;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Administrator
 * PostList里显示的每一个任务项
 */
public class PostsListItem extends ListItem
{
	
	public static class TaskStatisticalStateInfo
	{
		public int total;
		public int replied;
		
		TaskStatisticalStateInfo(int total, int replied)
		{
			this.total = total;
			this.replied = replied;
		}
	}
	
	
	protected TextView tvDataTime = null;
	protected TextView tvAbstract = null;
	protected TextView tvProgressStatus = null;
	
	protected ImageView imCompleted = null;
	
	private final int _id_tvDataTime = IdGenerator.getNewId();
	private final int _id_tvAbstract = IdGenerator.getNewId();
	private final int _id_tvProgressStatus = IdGenerator.getNewId();
	

	public PostsListItem(Context context)
	{
		super(context);

		final int dip_10 = UnitsUtil.dip2px(getContext(), 10);
		final int dip_6 = UnitsUtil.dip2px(getContext(), 6);
		setPadding(dip_6, dip_10, dip_6, dip_10);
		setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		tvDataTime = new TextView(getContext());
		tvDataTime.setId(_id_tvDataTime);
		tvDataTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		tvDataTime.setTextColor(Color.DKGRAY);
		
		tvAbstract = new TextView(getContext());
		tvAbstract.setId(_id_tvAbstract);
		tvAbstract.setLines(1);
		tvAbstract.setTextColor(Color.GRAY);
		
		tvProgressStatus = new TextView(getContext());
		tvProgressStatus.setId(_id_tvProgressStatus);
		tvProgressStatus.setTextColor(Color.GRAY);
		
		
		
		final int dip_3 = UnitsUtil.dip2px(getContext(), 3);
		RelativeLayout.LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.setMargins(0, dip_3, 0, 0);
		rlp.addRule(RelativeLayout.BELOW, tvDataTime.getId());
		tvAbstract.setLayoutParams(rlp);
				
		RelativeLayout.LayoutParams rlp2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp2.setMargins(0, 2 * dip_3, 0, 0);
		rlp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rlp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvProgressStatus.setLayoutParams(rlp2);
		
		addView(tvDataTime);
		addView(tvAbstract);
		addView(tvProgressStatus);
	}

	
	
	public void setContent(String timeStamp, String msgAbstract, TaskStatisticalStateInfo tsi)
	{
		tvDataTime.setText(timeStamp);
		tvAbstract.setText(msgAbstract);
		tvProgressStatus.setText(tsi.replied + "/" + tsi.total);
	}
	
	
	
	
	public void markCompleted()
	{
		tvDataTime.setTextColor(Color.GRAY);
		
		if (imCompleted == null)
		{
			final int dip_24 = UnitsUtil.dip2px(getContext(), 24);
			Bitmap btnBackground = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.tick); 
			int sourceH = btnBackground.getHeight();
			int targetH = dip_24;
			float scale = (float) targetH / sourceH;
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);  
			Bitmap scaledBmp = Bitmap.createBitmap(btnBackground, 0, 0, btnBackground.getWidth(), btnBackground.getHeight(), matrix, true);
			imCompleted = new ImageView(getContext());
			imCompleted.setImageBitmap(scaledBmp);
			
			final int dip_25 = UnitsUtil.dip2px(getContext(), 25);
			RelativeLayout.LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rlp.setMargins(0, 0, dip_25, 0);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			imCompleted.setLayoutParams(rlp);
		}
		addView(imCompleted, 0);
	}
	
	
	
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
