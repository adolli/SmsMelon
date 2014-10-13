package adolli.smsMelon;

/**
 * @brief	����Ϣ�İ�װ
 * @author Administrator
 *
 */
public class Sms
{
	protected String address = null;
	protected String messageContent = null;
	
	public Sms(String addr, String content)
	{
		address = addr;
		messageContent = content;
	}
	
	
	/**
	 * @brief	��ȡ��Ϣ�����ߵ绰����
	 * @return	�����ߵ绰�����ַ���
	 */
	public String getReceiverAddress()
	{
		return address;
	}
	public String getSenderAddress()
	{
		return address;
	}
	
	
	/**
	 * @brief	��ȡ��Ϣ����
	 * @return	��Ϣ�����ַ���
	 */
	public String getMessageContent()
	{
		return messageContent;
	}
	
}
