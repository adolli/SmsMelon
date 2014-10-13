/**
 * 
 */
package adolli.smsMelon;

import adolli.contacts.ContactsPickerTabFrame;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Administrator
 *
 */
public class SmsMelonActivity extends Activity implements View.OnClickListener
{
	
	private Button addReceiverButton = null;
	private Button createNewPostMessageTaskButton = null;
	private Button viewPostsListButton = null;
	private Button exitProgrammeButton = null;
	private EditText editTextSendTo = null;
	private EditText editTextMsgContent = null;
	
	public static SmsMelonService smsMelonService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_melon_activity_layout);
		
		// 启动时注册melon service
		Intent bindIntent = new Intent(SmsMelonActivity.this, SmsMelonService.class);
		bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
		
		editTextSendTo = (EditText) findViewById(R.id.editTextSendTo);
		editTextMsgContent = (EditText) findViewById(R.id.editTextMsgContent);
		addReceiverButton = (Button) findViewById(R.id.addReceiverButton);
		createNewPostMessageTaskButton = (Button) findViewById(R.id.createNewPostMessageTaskButton);
		viewPostsListButton = (Button) findViewById(R.id.viewPostsListButton);
		exitProgrammeButton = (Button) findViewById(R.id.exitProgrammeButton);
		
		addReceiverButton.setOnClickListener(this);
		createNewPostMessageTaskButton.setOnClickListener(this);
		viewPostsListButton.setOnClickListener(this);
		exitProgrammeButton.setOnClickListener(this);
	}
	
	
	/*
	 * 服务连接器
	 */
	private ServiceConnection mConnection = new ServiceConnection()
	{
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			// Called when the connection is made.
			smsMelonService = ((SmsMelonService.SmsMelonServiceBinder) service).getService();
		}

		public void onServiceDisconnected(ComponentName className)
		{
			// Received when the service unexpectedly disconnects.
			smsMelonService = null;
		}
	};


	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.addReceiverButton :
			Intent intent = new Intent(this, ContactsPickerTabFrame.class);
			startActivity(intent);
			break;
			
		case R.id.createNewPostMessageTaskButton :
			break;
			
		case R.id.viewPostsListButton :
			Intent intent1 = new Intent(this, PostsListActivity.class);
			startActivity(intent1);
			break;
			
		case R.id.exitProgrammeButton :
			break;
		
		default :
			;
		}
	}
	
}
