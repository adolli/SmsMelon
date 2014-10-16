/**
 * 
 */
package adolli.contacts;

import java.util.Iterator;
import java.util.List;

import adolli.contacts.ContactsHelper.ContactInfo;
import adolli.smsMelon.R;
import adolli.smsMelon.SharedSelectedContacts;
import adolli.widget.button.PlaneButton;
import adolli.widget.button.PlaneButtonWithCounter;
import adolli.widget.listView.ScrollListViewWithFilter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author 	Administrator
 * @brief	联系人选择activity
 */
public class ContactsPickerActivity extends Activity
{

	private ContactsHelper contactsHelper = null;
	private PlaneButtonWithCounter okButton = null;
	private ScrollListViewWithFilter<SelectableContact> contactsScrollListView = null;


	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contacts_multi_picker_activity_layout);
		
		
		contactsHelper = new ContactsHelper(this);
		//contactsHelper.updateContactsDB();
		
		
		LinearLayout contactsPickerButtonsArea = (LinearLayout) findViewById(R.id.contactsPickerButtonsArea);
		
		okButton = new PlaneButtonWithCounter(this);
		okButton.setTextContent("确定");
		contactsPickerButtonsArea.addView(okButton);
		
		PlaneButton clearButton = new PlaneButton(this);
		clearButton.setText("重选");
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
		contactsScrollListView = new ScrollListViewWithFilter<SelectableContact>(this, "联系人");
		contactsScrollListView.addLayoutRules(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		contactsScrollListView.addLayoutRules(RelativeLayout.ABOVE, R.id.contactsPickerButtonsArea);
		rootLayout.addView(contactsScrollListView);
		
			
		List<ContactInfo> contactsList = contactsHelper.getAllContactsList();
		for (Iterator<ContactInfo> it = contactsList.iterator(); it.hasNext(); )
		{
			ContactInfo contactInfo = it.next();
			String phoneNumber = contactInfo.phoneNumber.replace("-", "");
			String name = contactInfo.name;
			String pinyin = contactInfo.pinyinName;
						
			SelectableContact selectableContact = new SelectableContact(this);
			selectableContact.setContent(name, phoneNumber, pinyin);
			selectableContact.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					SelectableContact sc = (SelectableContact) v;
					if (sc.isSelected())
					{
						okButton.increase();
						SharedSelectedContacts.sharedSelectedContactListAddSelection(sc.getContactInfo());
					}
					else
					{
						okButton.decrease();
						SharedSelectedContacts.sharedSelectedContactListRemoveSelection(sc.getContactInfo());
					}
				}
			});
			contactsScrollListView.addListItem(selectableContact);
		}
	}
	
	
	
	/**
	 * activity恢复时重新mark以选择的联系人
	 */
	protected void onResume()
	{
		super.onResume();
		
		okButton.resetToZero();
		ViewGroup vg = (ViewGroup) contactsScrollListView.getContactList();
		for (int i = 0; i < vg.getChildCount() - 1; ++i)
		{
			try
			{
				SelectableContact sc = (SelectableContact) vg.getChildAt(i);
				int index = SharedSelectedContacts.sharedSelectedContactListIndexOf(sc.getContactInfo());
				if (index != -1)
				{
					okButton.increase();
					sc.select();
				}
				else
				{
					sc.deSelect();
				}
			}
			catch (ClassCastException e)
			{
				// just do nothing
			}			
		}
	}
	

	
	
	
	public void deselectedAll()
	{
		ViewGroup vg = (ViewGroup) contactsScrollListView.getContactList();
		for (int i = 0; i < vg.getChildCount() - 1; ++i)
		{
			try
			{
				SelectableContact sc = (SelectableContact) vg.getChildAt(i);
				sc.deSelect();
			}
			catch (ClassCastException e)
			{
				// just do nothing
			}
			
		}
		SharedSelectedContacts.selectedContactsList.clear();
		okButton.resetToZero();	
	}
	
	
	
	
}
