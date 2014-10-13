package adolli.smsMelon;

import java.util.ArrayList;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class ReceiverPickerActivity extends Activity 
{
	
	private int selectedCount = 0;
	private ArrayList<String> receiverList = null;	// һ�����ڱ�����ѡ��ϵ�˵�list
	protected Button okButton = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_receiver_activity_layout);
		
		okButton = (Button) findViewById(R.id.buttonOk);
		
		// ������ϵ���б�
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.contactList);
		
		receiverList = new ArrayList<String>();
		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (cursor.moveToNext())
		{
			// ��ȡ����
			String name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			
			// ��ȡid
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			
			// ���� id��ȡ���е绰�����¼
			Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					null, 
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, 
					null, null);
			
			// ��ʾÿ�����뵽�б���
			while (phone.moveToNext())
			{
				String strPhoneNum = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				
				// ��ӵ�checkbox��
				CheckBox checkbox = new CheckBox(this);
				checkbox.setText(name + " " + strPhoneNum);
				checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				{
					
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean checked) 
					{
						if (checked)
						{
							ReceiverPickerActivity.this.selectedCount++;
						}
						else
						{
							ReceiverPickerActivity.this.selectedCount--;
						}
						ReceiverPickerActivity.this.okButton.setText("Select(" + ReceiverPickerActivity.this.selectedCount + ")");
					}
					
				});
				linearLayout.addView(checkbox);
			}
			
			phone.close();
			
			
		}
		cursor.close();
		
		
		okButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// ��ѡ��õ�receiver���ص�һ��activity
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DEFAULT);
				intent.putExtra("how much selected", ReceiverPickerActivity.this.selectedCount);
				ReceiverPickerActivity.this.sendBroadcast(intent);
				ReceiverPickerActivity.this.finish();
			}
			
		});
		
	}
	
}







