/**
 * 
 */
package adolli.contacts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import adolli.characters.Pinyin;
import adolli.utility.DatabaseHelper;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

/**
 * @author 	Administrator
 * @brief	ά��һ���Ե绰����Ϊ��������ϵ�����ݿ�
 */
public class ContactsHelper
{

	/**
	 * @brief	������ϵ����Ϣ�Ľṹ��
	 * @author 	Administrator
	 *
	 */
	public static class ContactInfo 
	{
		public String name;
		public String phoneNumber;
		public String pinyinName;
		
		public ContactInfo(String name, String phone, String pinyin)
		{
			this.phoneNumber = phone;
			this.name = name;
			this.pinyinName = pinyin;
		}
	}
	
	
	private Context context = null;
	
	public ContactsHelper(Context c)
	{
		context = c;
	}
	
	
	/**
	 * @brief	��ϵͳ�ж�ȡ��ϵ�����ݲ�����һ���Ե绰����ΪKey����ϵ�����ݿ⣬
	 * 			ÿ��ɾ���ɱ����¶�ȡϵͳ��ϵ������
	 */
	public void updateContactsDB()
	{
		DatabaseHelper db = new DatabaseHelper(context, "SmsMelonDB");
		db.dropTable("MelonContacts");
		
		// Ⱥ�������б�����Ѿ���������Ӱ�죩
     	// ��PhoneNumber Ϊ��ϵ�˵绰����
     	// ��Name Ϊ��ϵ������
		db.createTable("MelonContacts", new String[]{
        		"PhoneNumber TEXT NOT NULL", 
        		"Name TEXT",
        		"NamePinyin TEXT"});
		
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(
				ContactsContract.Contacts.CONTENT_URI, 
				new String[]{ ContactsContract.Contacts._ID, PhoneLookup.DISPLAY_NAME }, 
				null, null, null);
		while (cursor.moveToNext())
		{
			// ��ϵ��id��
			String contactId = cursor.getString(0);
			
			// ��ϵ������
			String name = cursor.getString(1);
			
			// ����id�����绰����
			Cursor phone = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					new String[]{ ContactsContract.CommonDataKinds.Phone.NUMBER }, 
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, 
					null, null);
			while (phone.moveToNext())
			{
				String phoneNumber = phone.getString(0);
				
				// ��ÿһ����ϵ�˼�¼���뵽����
				if (!phoneNumber.isEmpty())
				{
					db.insertInto("MelonContacts", new String[]{ 
							"'" + phoneNumber + "'", 
							"'" + name + "'",
							"'" + Pinyin.Chinese2Pinyin(name) + "'" });
				}
			}
			phone.close();
		}
		cursor.close();
		
		db.close();
	}
	
	
	/**
	 * @brief	��Melon��ϵ���л�ȡ��ϵ�����ݵ�hashMap��
	 * @return
	 */
	public Map<String, String> getAllContacts()
	{
		Map<String, String> contactsMap = new HashMap<String, String>();
		DatabaseHelper db = new DatabaseHelper(context, "SmsMelonDB");
		Cursor c = db.getReadableDatabase().query(
				"MelonContacts", 
				new String[]{ "PhoneNumber", "Name" }, 
				null, null, null, null, 
				"NamePinyin");
		while (c.moveToNext())
		{
			String phoneNumber = c.getString(0);
			String name = c.getString(1);
			contactsMap.put(phoneNumber, name);
		}
		c.close();
		db.close();
		
		return contactsMap;
	}
	
	
	public List<ContactInfo> getAllContactsList()
	{
		List<ContactInfo> contactsList = new LinkedList<ContactInfo>();
		DatabaseHelper db = new DatabaseHelper(context, "SmsMelonDB");
		Cursor c = db.getReadableDatabase().query(
				"MelonContacts", 
				new String[]{ "PhoneNumber", "Name", "NamePinyin" }, 
				null, null, null, null, 
				"NamePinyin");
		while (c.moveToNext())
		{
			String phoneNumber = c.getString(0);
			String name = c.getString(1);
			String pinyin = c.getString(2);
			contactsList.add(new ContactInfo(name, phoneNumber, pinyin));
		}
		c.close();
		db.close();
		
		return contactsList;
	}
	
}
