/**
 * 
 */
package adolli.contacts;

import java.util.Iterator;
import adolli.contacts.ContactsHelper.ContactInfo;
import adolli.contacts.RemovableContact.OnRemovedListener;
import adolli.smsMelon.R;
import adolli.smsMelon.SharedSelectedContacts;
import adolli.widget.button.PlaneButton;
import adolli.widget.button.PlaneButtonWithCounter;
import adolli.widget.listView.ScrollListViewWithFilter;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author Administrator
 *
 */
public class ContactsSelectedActivity extends Activity
{
	
	private PlaneButtonWithCounter okButton = null;
	private ScrollListViewWithFilter<RemovableContact> contactsScrollListView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contacts_multi_picker_activity_layout);
		
		LinearLayout contactsPickerButtonsArea = (LinearLayout) findViewById(R.id.contactsPickerButtonsArea);
		
		okButton = new PlaneButtonWithCounter(this);
		okButton.setTextContent("确定");
		okButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		contactsPickerButtonsArea.addView(okButton);
		
		PlaneButton clearButton = new PlaneButton(this);
		clearButton.setText("清除");
		clearButton.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				deselectedAll();
			}
		});
		contactsPickerButtonsArea.addView(clearButton);
		
		RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.contactPickerRootLayout);
		contactsScrollListView = new ScrollListViewWithFilter<RemovableContact>(this, "已选联系人");
		contactsScrollListView.addLayoutRules(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		contactsScrollListView.addLayoutRules(RelativeLayout.ABOVE, R.id.contactsPickerButtonsArea);
		rootLayout.addView(contactsScrollListView);
	}
	
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		contactsScrollListView.clearList();		
		
		okButton.resetToZero();
		for (Iterator<ContactInfo> it = SharedSelectedContacts.selectedContactsList.iterator(); it.hasNext(); )
		{
			ContactInfo contactInfo = (ContactInfo) it.next();
			String phoneNumber = contactInfo.phoneNumber.replace("-", "");
			String name = contactInfo.name;
			String pinyin = contactInfo.pinyinName;
						
			RemovableContact removableContact = new RemovableContact(this, contactsScrollListView);
			removableContact.setContent(name, phoneNumber, pinyin);
			removableContact.setOnRemovedListener(new OnRemovedListener()
			{
				@Override
				public void onRemoved(RemovableContact rc)
				{
					SharedSelectedContacts.sharedSelectedContactListRemoveSelection(rc.getContactInfo());
					okButton.decrease();
				}
			});
			
			contactsScrollListView.addListItem(removableContact);
			okButton.increase();
		}
		
	}
	
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// 按下返回按键时表示不需要选择
		// TODO 最好能将这次新选择的给取消掉，以前选的保留
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
	    {   
	    	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	
	
	/**
	 * @brief	清除所有所选的联系人
	 */
	public void deselectedAll()
	{
		contactsScrollListView.clearList();
		SharedSelectedContacts.selectedContactsList.clear();
		okButton.resetToZero();
	}
	
	

	
	
}
