/**
 * 
 */
package adolli.smsMelon;

import adolli.utility.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.posts_message_detail_status_activity_layout);
	}
	
	
	
	
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.postMessageDetailLinearLayout);
		layout.removeAllViews();
		
		
		Intent intent = getIntent();
		taskId = intent.getStringExtra("postMessageTaskId");
		Log.d("test taskid", taskId);
		
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
	}
	
	
	@Override
	protected void onDestroy()
	{
		
		super.onDestroy();
	}
	
	
}
