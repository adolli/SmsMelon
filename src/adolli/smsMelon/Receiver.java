package adolli.smsMelon;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;


/**
 * @author 	Administrator
 * @brief 	消息接收者，通过电话号码定位接收者（假设联系人中没有重复的电话）
 */
public class Receiver
{

	protected String phoneNumber = null;
	protected String name = null;
	protected int _id = 0;
	
	
	/**
	 * @brief	Receiver构造函数
	 * @param 	context 要构造Receiver对象的activity
	 * @param 	phoneNum Receiveri的电话号码
	 */
	public Receiver(Context context, String phoneNum)
	{
		phoneNumber = phoneNum;
		
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] 
		{
			ContactsContract.Contacts._ID,
			ContactsContract.Contacts.DISPLAY_NAME,
			ContactsContract.Contacts.PHOTO_ID
		};
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";    
        String[] selectionArgs = null;    
        String sortOrder = null;
        
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		while (cursor.moveToNext())
		{
			// 获取名字
			String name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			
			// 获取id
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			
			// 根据 id读取所有电话号码记录
			Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					null, 
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, 
					null, null);
			
			// 显示每个号码到列表中
			while (phone.moveToNext())
			{
				String strPhoneNum = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			}
			
			phone.close();
			
			
		}
		cursor.close();
	}
	
	
	
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getID()
	{
		return _id;
	}
	
}
