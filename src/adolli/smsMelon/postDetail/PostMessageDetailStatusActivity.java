/**
 * 
 */
package adolli.smsMelon.postDetail;

import adolli.smsMelon.R;
import adolli.smsMelon.SmsMelonProcessor;
import adolli.utility.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author 	Administrator
 * @notice	启动此activity时需要传递一个包含getStringExtra("postMessageTaskId")的intent
 * 			内容为任务的id号
 */
public class PostMessageDetailStatusActivity extends Activity 
{

	private String taskId = null;
	private TextView tvMsgAbstract = null;
	private TextView tvMsgTimeStamp = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.posts_message_detail_status_activity_layout);
		
		Intent intent = getIntent();
		taskId = intent.getStringExtra("postMessageTaskId");
		
		tvMsgAbstract = (TextView) findViewById(R.id.detailView_messageAbstract);
		tvMsgTimeStamp = (TextView) findViewById(R.id.detailView_messageTimeStamp);
		tvMsgTimeStamp.setTextColor(Color.LTGRAY);
		
		DatabaseHelper database = new DatabaseHelper(this, "SmsMelonDB");
		Cursor c = database.getReadableDatabase().query("PostsList", 
				new String[]{ "PostMessageAbstract", "PostMessageTimeStamp" }, 
				"PostMessageTableName = '" + taskId + "'", 
				null, null, null, null);
		if (c.moveToFirst())
		{
			String msgAbstract = c.getString(0);
			String timeStamp = c.getString(1);
			
			tvMsgAbstract.setText(msgAbstract);
			tvMsgTimeStamp.setText("于 " + timeStamp + " 发出");
		}
		
	}
	
	
	
	
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.postMessageDetailLinearLayout);
		layout.removeAllViews();
		
		Log.d("test taskid != null", taskId);
		
		DatabaseHelper database = new DatabaseHelper(this, "SmsMelonDB");
		Cursor c = database.getReadableDatabase().query(
				taskId, 
				new String[]{"msgAddress", "msgStatus", "msgReplied"}, 
				null, null, 
				null, 				// GroupBy
				null, 
				"msgStatus ASC");	// order by	
		while (c.moveToNext()) 
		{
			String receiverAddress = c.getString(0);
			int iReplyStatus = c.getInt(1);
			String replyStatus = iReplyStatus == SmsMelonProcessor.PostMessage.HAS_REPLIED ? "[replied]" : 
								 iReplyStatus == SmsMelonProcessor.PostMessage.MSG_SENT ? "[not replied]" : 
								 iReplyStatus == SmsMelonProcessor.PostMessage.FAIL_TO_SEND ? "[fail to send]" : "[ready to send]";
			String replyMessage = c.getString(2);
			replyMessage = DatabaseHelper.deEscape(replyMessage);
			
			if (replyMessage.length() > 13)
			{
				replyMessage = replyMessage.substring(0, 12) + "...";
			}

			TextView tv = new TextView(this);
			tv.setText(receiverAddress + "   " + replyStatus + "  " + replyMessage);
			if (c.getInt(1) == 1)
			{
				//tv.setTextColor(getResources().getColor(android.R.color.secondary_text_dark));
			}
			layout.addView(tv);
		}
		c.close();
		database.close();	
		
		ReceiverReplyStatusListItem rrs = new ReceiverReplyStatusListItem(getBaseContext());
		rrs.setContent("Lixiang", "this is what i replied!");
		layout.addView(rrs);
	}
	
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	
}
