
package adolli.smsMelon;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * @brief	�����շ�������
 * @author 	adolli
 */
public class SmsHelper
{
	
	public static Sms getSms(Intent intent)
	{
		String msgContent = "";
		String msgSenderAddress = ""; 
		
		// SMSͨ��bundle.get("pdus")��ȡ
		Bundle bundle = intent.getExtras();
		Object messages[] = (Object[]) bundle.get("pdus");
		
		// �������ſ��ܻᱻ�ָ�ɶ�������һ�����������ߵ绰�������Ϣ���ݣ��������ȵ���Ϣ�ᱻ�ŵ��ڶ������Դ�����
		// ��������
		for (int i = 0; i < messages.length; i++)
		{
			msgContent += SmsMessage.createFromPdu((byte[]) messages[i]).getDisplayMessageBody();
		}
		
		// ���ŷ����ߵ绰����
		msgSenderAddress = SmsMessage.createFromPdu((byte[]) messages[0]).getDisplayOriginatingAddress();
		
		// һ������+86�����Ŀ�ͷ,Ҫ�����
		if (msgSenderAddress.length() > 11)
		{
			msgSenderAddress = msgSenderAddress.substring(msgSenderAddress.length() - 11);
		}
		
		return new Sms(msgSenderAddress, msgContent);
	}
	
		
	
	
	
	/**
	 * @brief	��ĳ���뷢�����ֶ���Ϣ
	 * @param	receiverAddr �����ߺ���
	 * @param	content ��Ϣ����
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
