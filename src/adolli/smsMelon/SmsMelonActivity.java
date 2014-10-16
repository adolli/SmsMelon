/**
 * 
 */
package adolli.smsMelon;

import java.util.LinkedList;

import adolli.contacts.ContactsPickerTabFrame;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
		
		editTextSendTo.setText("15018775164, 13628674163");
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
			String msgContent = editTextMsgContent.getText().toString();
			if (msgContent.isEmpty())
			{
				Toast.makeText(this, "消息是空白的，所以没有发送。", Toast.LENGTH_SHORT);
			}
			else
			{
				String receiverAddr = editTextSendTo.getText().toString();
				String[] receiverAddrs = receiverAddr.split(",");
				LinkedList<Sms> msgList = new LinkedList<Sms>();
				for (String addr : receiverAddrs)
				{
					msgList.add(new Sms(addr.trim(), msgContent));
				}
				SmsMelonProcessor smp = smsMelonService.createNewSmsMelonProcessor();
				smp.createNewPostMessageTask(msgList, msgContent);
				
				Toast.makeText(this, "new task creataed", Toast.LENGTH_LONG).show();
			}
			break;
			
		case R.id.viewPostsListButton :
			Intent intent1 = new Intent(this, PostsListActivity.class);
			startActivity(intent1);
			break;
			
		case R.id.exitProgrammeButton :
			AlertDialog.Builder dlg = new AlertDialog.Builder(this);
			dlg.setTitle("SmsMelon");
			dlg.setMessage("蜜瓜短信(SmsMelon)退出后将不会占用任何资源，同时也无法自动登记回复者，真的要退出吗？");
			dlg.setPositiveButton("退出程序", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1) 
				{
					SmsMelonActivity.this.finish();
				}
			});
			dlg.setNegativeButton("后台运行", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					SmsMelonActivity.this.minimize();
				}
			});
			dlg.show();
			break;
		
		default :
			;
		}
	}
	
	
	public void minimize()
	{
		// 程序最小化
    	Intent home = new Intent(Intent.ACTION_MAIN);  
    	home.addCategory(Intent.CATEGORY_HOME);   
    	startActivity(home);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// 按下返回按键
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
	    {   
	    	minimize();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
