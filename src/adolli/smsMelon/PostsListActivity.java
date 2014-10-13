/**
 * 
 */
package adolli.smsMelon;

import adolli.utility.DatabaseHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class PostsListActivity extends Activity 
{
	
	private Button clearAllPostMessageTask = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.posts_list_activity_layout);
		
		clearAllPostMessageTask = (Button) findViewById(R.id.clearAllPostMessageTaskButton);
		clearAllPostMessageTask.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				AlertDialog.Builder dlg = new AlertDialog.Builder(PostsListActivity.this);
				dlg.setTitle("SmsMelon");
				if (SmsMelonActivity.smsMelonService != null)
				{
					dlg.setMessage("���Ҫɾ��ȫ������");
					dlg.setPositiveButton("���", new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface arg0, int arg1) 
						{
							SmsMelonActivity.smsMelonService.clearAllPostMessageTask();
							PostsListActivity.this.updateViews();
						}
					});
					dlg.setNegativeButton("��Ҫɾ��", null);
					dlg.show();
				}
				else
				{
					dlg.setMessage("�۹Ϸ���(MelonService)��û����������ĳ������г������ˣ�������������һ��");
					dlg.setNegativeButton("�õģ����Լ�����", null);
					dlg.show();
				}
			}
			
		});
		
		
	}
	
	
	@Override
	protected void onResume()
	{
		super.onResume();
		updateViews();
	}
	
	
	/**
	 * @brief	���¸�activity�е��б���ͼ
	 */
	private void updateViews()
	{
		LinearLayout layout = (LinearLayout) findViewById(R.id.postsListLinearLayout);
		layout.removeAllViews();
		
		DatabaseHelper database = new DatabaseHelper(this, "SmsMelonDB");
		Cursor c = database.getReadableDatabase().query(
				"PostsList", 
				new String[]{"PostMessageTableName", "PostMessageAbstract", "PostMessageStatus"}, 
				null, null, null, null, null);
		while (c.moveToNext()) 
		{
			String fullTaskName = c.getString(0);
			String taskName = fullTaskName;
			String msgAbstract = c.getString(1);
			String taskStatus = c.getInt(2) == 1 ? "ok" : "--";
			
			msgAbstract = DatabaseHelper.deEscape(msgAbstract);
			
			TextView tv = new TextView(this);
			tv.setHint(fullTaskName);
			tv.setText(
					"taskId:            " + taskName + "\r\n" + 
					"msgAbstract:  " + msgAbstract + "\r\n" + 
					"taskStatus:      " + taskStatus + "\r\n");
			if (c.getInt(2) == 1)
			{
				tv.setTextColor(getResources().getColor(android.R.color.secondary_text_dark));
			}
			layout.addView(tv);
			
			tv.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					String taskId = ((TextView) v).getHint().toString();
					Intent intent = new Intent(PostsListActivity.this, PostMessageDetailStatusActivity.class);
					intent.putExtra("postMessageTaskId", taskId);
					PostsListActivity.this.startActivity(intent);
				}
			});
		}
		c.close();
		database.close();
	}
	
	
}
