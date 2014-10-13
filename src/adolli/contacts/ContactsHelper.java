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
 * @brief	维护一个以电话号码为主键的联系人数据库
 */
public class ContactsHelper
{

	/**
	 * @brief	保存联系人信息的结构体
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
	 * @brief	从系统中读取联系人数据并创建一个以电话号码为Key的联系人数据库，
	 * 			每次删除旧表，重新读取系统联系人数据
	 */
	public void updateContactsDB()
	{
		DatabaseHelper db = new DatabaseHelper(context, "SmsMelonDB");
		db.dropTable("MelonContacts");
		
		// 群发任务列表（如果已经存在则不受影响）
     	// 列PhoneNumber 为联系人电话号码
     	// 列Name 为联系人名字
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
			// 联系人id号
			String contactId = cursor.getString(0);
			
			// 联系人姓名
			String name = cursor.getString(1);
			
			// 根据id遍历电话号码
			Cursor phone = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					new String[]{ ContactsContract.CommonDataKinds.Phone.NUMBER }, 
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, 
					null, null);
			while (phone.moveToNext())
			{
				String phoneNumber = phone.getString(0);
				
				// 将每一条联系人记录插入到表中
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
	 * @brief	从Melon联系人中获取联系人数据到hashMap中
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
