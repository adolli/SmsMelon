package adolli.smsMelon;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;


/**
 * @author 	Administrator
 * @brief 	��Ϣ�����ߣ�ͨ���绰���붨λ�����ߣ�������ϵ����û���ظ��ĵ绰��
 */
public class Receiver
{

	protected String phoneNumber = null;
	protected String name = null;
	protected int _id = 0;
	
	
	/**
	 * @brief	Receiver���캯��
	 * @param 	context Ҫ����Receiver�����activity
	 * @param 	phoneNum Receiveri�ĵ绰����
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
