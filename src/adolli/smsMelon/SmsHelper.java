
package adolli.smsMelon;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * @brief	短信收发辅助类
 * @author 	adolli
 */
public class SmsHelper
{
	
	public static Sms getSms(Intent intent)
	{
		String msgContent = "";
		String msgSenderAddress = ""; 
		
		// SMS通过bundle.get("pdus")获取
		Bundle bundle = intent.getExtras();
		Object messages[] = (Object[]) bundle.get("pdus");
		
		// 整个短信可能会被分割成多条，第一条包含发送者电话号码和消息内容，超出长度的消息会被放到第二条，以此类推
		// 短信内容
		for (int i = 0; i < messages.length; i++)
		{
			msgContent += SmsMessage.createFromPdu((byte[]) messages[i]).getDisplayMessageBody();
		}
		
		// 短信发送者电话号码
		msgSenderAddress = SmsMessage.createFromPdu((byte[]) messages[0]).getDisplayOriginatingAddress();
		
		// 一般会带有+86这样的开头,要处理掉
		if (msgSenderAddress.length() > 11)
		{
			msgSenderAddress = msgSenderAddress.substring(msgSenderAddress.length() - 11);
		}
		
		return new Sms(msgSenderAddress, msgContent);
	}
	
		
	
	
	
	/**
	 * @brief	向某号码发送文字短消息
	 * @param	receiverAddr 接收者号码
	 * @param	content 消息内容
	 */
	public static void sendSms(String receiverAddr, String content)
	{
		if (receiverAddr.length() > 11)
		{
			receiverAddr = receiverAddr.substring(receiverAddr.length() - 11);
		}
		
		SmsManager smsManager = SmsManager.getDefault();
		List<String> messageContentList = smsManager.divideMessage(content);
		for (String smsPiece : messageContentList)
		{
			smsManager.sendTextMessage(receiverAddr, null, smsPiece, null, null);
		}	
		
		Log.i("adolli:SmsHelper", content); 
	}
	
	
	
	public static void sendSms(Sms sms)
	{
		sendSms(sms.address, sms.messageContent);
	}
}
