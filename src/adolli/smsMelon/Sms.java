package adolli.smsMelon;

/**
 * @brief	短消息的包装
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
	 * @brief	获取消息接收者电话号码
	 * @return	接收者电话号码字符串
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
	 * @brief	获取消息内容
	 * @return	消息内容字符串
	 */
	public String getMessageContent()
	{
		return messageContent;
	}
	
}
