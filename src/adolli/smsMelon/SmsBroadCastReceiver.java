package adolli.smsMelon;

import java.util.Iterator;
import java.util.LinkedList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmsBroadCastReceiver extends BroadcastReceiver 
{

	public SmsBroadCastReceiver()
	{
	}
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Sms sms = SmsHelper.getSms(intent);
        
        LinkedList<SmsMelonProcessor> smps = ((SmsMelonService) context).getSmsMelonProcessors();
        for (Iterator<SmsMelonProcessor> it = smps.iterator(); it.hasNext(); )
        {
        	SmsMelonProcessor smp = (SmsMelonProcessor) it.next();
        	
        	if (smp.markReplied(sms.getReceiverAddress(), sms.getMessageContent()))
            {
                Log.i("adolli:replyFromList", sms.getMessageContent() + "," + sms.getReceiverAddress()); 
            }
            else
            {
            	Log.i("adolli:other sms received", sms.getMessageContent() + "," + sms.getReceiverAddress()); 
            }
        }
	}
	
	
	 

}
