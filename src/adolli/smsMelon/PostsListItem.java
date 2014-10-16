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
import android.widget.LinearLayout;
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
	protected LinearLayout llStatusLayout = null;
	protected TextView tvTotalCount = null;
	protected TextView tvRepliedCount = null;
	protected TextView tvRemainedCount = null;
	
	protected ImageView imCompleted = null;
	
	private final int _id_tvDataTime = IdGenerator.getNewId();
	private final int _id_tvAbstract = IdGenerator.getNewId();
	
	private static final String tagTotal = "总共:";
	private static final String tagReplied = "已回:";
	private static final String tagRemained = "未回:";

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
		
		llStatusLayout = new LinearLayout(getContext());
		llStatusLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		llp.weight = 1;
		
		tvTotalCount = new TextView(getContext());
		tvTotalCount.setLayoutParams(llp);
		tvTotalCount.setTextColor(Color.GRAY);
		
		tvRepliedCount = new TextView(getContext());
		tvRepliedCount.setLayoutParams(llp);
		tvRepliedCount.setTextColor(Color.GRAY);
		
		tvRemainedCount = new TextView(getContext());
		tvRemainedCount.setLayoutParams(llp);
		tvRemainedCount.setTextColor(Color.GRAY);
		
		llStatusLayout.addView(tvTotalCount);
		llStatusLayout.addView(tvRepliedCount);
		llStatusLayout.addView(tvRemainedCount);
		
		addView(tvDataTime);
		addView(tvAbstract);
		addView(llStatusLayout);
		
		final int dip_3 = UnitsUtil.dip2px(getContext(), 3);
		RelativeLayout.LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.setMargins(0, dip_3, 0, 0);
		rlp.addRule(RelativeLayout.BELOW, tvDataTime.getId());
		tvAbstract.setLayoutParams(rlp);
		
		RelativeLayout.LayoutParams rlp1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		rlp1.setMargins(0, dip_3, 0, 0);
		rlp1.addRule(RelativeLayout.BELOW, tvAbstract.getId());
		llStatusLayout.setLayoutParams(rlp1);
	}

	
	
	public void setContent(String timeStamp, String msgAbstract, TaskStatisticalStateInfo tsi)
	{
		tvDataTime.setText(timeStamp);
		tvAbstract.setText(msgAbstract);
		tvTotalCount.setText(tagTotal + tsi.total);
		tvRepliedCount.setText(tagReplied + tsi.replied);
		tvRemainedCount.setText(tagRemained + (tsi.total - tsi.replied));
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
			
			RelativeLayout.LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rlp.setMargins(0, 0, 0, 0);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			imCompleted.setLayoutParams(rlp);
		}
		addView(imCompleted);
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
