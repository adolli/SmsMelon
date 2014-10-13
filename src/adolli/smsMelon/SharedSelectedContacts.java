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
 * @brief	������ѡ�����ϵ����Ϣ����
 */
public class SharedSelectedContacts
{
	// ������ѡ�б�
	public static List<ContactInfo> selectedContactsList = new LinkedList<ContactInfo>();
	
	
	
	/**
	 * @brief	����ѡ�����ϵ����ӵ�����ѡ����ϵ���б��У�������activity�����Զ�ȡ���б��Ը�������
	 * @param 	sc ����ѡ�����ϵ���б���
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
	 * @brief	�ҳ�ָ����ϵ�����б��е�������
	 * @param 	sc ��ϵ���б�ѡ����
	 * @return	��list�е�λ�ã��Ҳ����򷵻�-1
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
