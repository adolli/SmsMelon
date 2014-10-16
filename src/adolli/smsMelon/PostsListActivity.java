/**
 * 
 */
package adolli.smsMelon;

import adolli.smsMelon.PostsListItem.TaskStatisticalStateInfo;
import adolli.smsMelon.SmsMelonProcessor.PostMessage;
import adolli.utility.DatabaseHelper;
import adolli.widget.button.PlaneButton;
import adolli.widget.listView.ScrollListView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * @author Administrator
 *
 */
public class PostsListActivity extends Activity 
{
	
	private PlaneButton clearAllPostMessageTask = null;
	private ScrollListView<PostsListItem> postsScrollListView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.posts_list_activity_layout);
		
		clearAllPostMessageTask = (PlaneButton) findViewById(R.id.clearAllPostMessageTaskButton);
		clearAllPostMessageTask.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View arg0) 
			{
				AlertDialog.Builder dlg = new AlertDialog.Builder(PostsListActivity.this);
				dlg.setTitle("SmsMelon");
				if (SmsMelonActivity.smsMelonService != null)
				{
					dlg.setMessage("真的要删除全部任务？");
					dlg.setPositiveButton("真的", new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface arg0, int arg1) 
						{
							SmsMelonActivity.smsMelonService.clearAllPostMessageTask();
							PostsListActivity.this.updateViews();
						}
					});
					dlg.setNegativeButton("不要删除", null);
					dlg.show();
				}
				else
				{
					dlg.setMessage("蜜瓜服务(MelonService)还没有启动，你的程序运行出问题了，请你重新启动一下");
					dlg.setNegativeButton("好的，我自己重启", null);
					dlg.show();
				}
			}
		});
			
		RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
		postsScrollListView = new ScrollListView<PostsListItem>(this, "任务");
		postsScrollListView.addLayoutRules(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		postsScrollListView.addLayoutRules(RelativeLayout.ABOVE, R.id.clearAllPostMessageTaskButton);
		rootLayout.addView(postsScrollListView);
	}
	
	
	@Override
	protected void onResume()
	{
		super.onResume();
		updateViews();
	}
	
	
	/**
	 * @brief	更新该activity中的列表视图
	 */
	private void updateViews()
	{
		postsScrollListView.clearList();
		DatabaseHelper database = new DatabaseHelper(this, "SmsMelonDB");
		Cursor c = database.getReadableDatabase().query(
				"PostsList", 
				new String[]{ "PostMessageTableName", "PostMessageAbstract", "PostMessageStatus", "PostMessageTimeStamp", "ReceiverCount" }, 
				null, null, null, null, null);
		
		Log.d("taskstatuc", "c count" + c.getCount() + "");
		c.moveToLast();
		while (!c.isBeforeFirst()) 
		{
			int taskStatus = c.getInt(2);
			if (taskStatus != 1)
			{
				final String taskId = c.getString(0);
				String msgAbstract = DatabaseHelper.deEscape(c.getString(1));
				String msgTimeStamp = c.getString(3);
				int receiverCount = c.getInt(4);
				
				Cursor replierCursor = database.getReadableDatabase().query(
						taskId, 
						new String[]{ "msgStatus" }, 
						"msgStatus = " + Integer.toString(PostMessage.HAS_REPLIED),
						null, null, null, null);
				PostsListItem pli = new PostsListItem(this);
				pli.setContent(msgTimeStamp, msgAbstract, new TaskStatisticalStateInfo(receiverCount, replierCursor.getCount()));
				
				pli.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						Intent intent = new Intent(PostsListActivity.this, PostMessageDetailStatusActivity.class);
						intent.putExtra("postMessageTaskId", taskId);
						PostsListActivity.this.startActivity(intent);
					}
				});
				postsScrollListView.addListItem(pli);
				replierCursor.close();
			}
			c.moveToPrevious();
		}
		c.moveToLast();
		while (!c.isBeforeFirst())
		{
			int taskStatus = c.getInt(2);
			if (taskStatus == 1)
			{
				final String taskId = c.getString(0);
				String msgAbstract = DatabaseHelper.deEscape(c.getString(1));
				String msgTimeStamp = c.getString(3);
				int receiverCount = c.getInt(4);
				
				PostsListItem pli = new PostsListItem(this);
				pli.setContent(msgTimeStamp, msgAbstract, new TaskStatisticalStateInfo(receiverCount, receiverCount));
				pli.markCompleted();
				pli.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						Intent intent = new Intent(PostsListActivity.this, PostMessageDetailStatusActivity.class);
						intent.putExtra("postMessageTaskId", taskId);
						PostsListActivity.this.startActivity(intent);
					}
				});
				postsScrollListView.addListItem(pli);
			}
			c.moveToPrevious();
		}
		c.close();
		database.close();
	}
	
	
}
