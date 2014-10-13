/**
 * 
 */
package adolli.smsMelon;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import adolli.contacts.ContactsHelper.ContactInfo;

/**
 * @author 	Administrator
 * @brief	保存以选择的联系人信息的类
 */
public class SharedSelectedContacts
{
	// 共享已选列表
	public static List<ContactInfo> selectedContactsList = new LinkedList<ContactInfo>();
	
	
	
	/**
	 * @brief	把所选择的联系人添加到共享选择联系人列表中，在其他activity将可以读取该列表以更新内容
	 * @param 	sc 可以选择的联系人列表项
	 */
	public static void sharedSelectedContactListAddSelection(ContactInfo ci)
	{
		selectedContactsList.add(ci);
	}
	
	public static void sharedSelectedContactListRemoveSelection(ContactInfo ci)
	{
		int i = 0;
		for (Iterator<ContactInfo> it = selectedContactsList.iterator(); it.hasNext(); )
		{
			ContactInfo c = (ContactInfo) it.next();
			if (c.name.equals(ci.name) && c.phoneNumber.equals(ci.phoneNumber))
			{
				selectedContactsList.remove(i);
				return;
			}
			++i;
		}
	}
	
	
	
	/**
	 * @brief	找出指定联系人在列表中的索引号
	 * @param 	sc 联系人列表选择项
	 * @return	在list中的位置，找不到则返回-1
	 */
	public static int sharedSelectedContactListIndexOf(ContactInfo ci)
	{
		int i = 0;
		for (Iterator<ContactInfo> it = selectedContactsList.iterator(); it.hasNext(); )
		{
			ContactInfo c = (ContactInfo) it.next();
			if (c.name.equals(ci.name) && c.phoneNumber.equals(ci.phoneNumber))
			{
				return i;
			}
			++i;
		}
		return -1;
	}
	
	
}
