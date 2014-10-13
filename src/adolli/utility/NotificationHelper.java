/**
 * 
 */
package adolli.utility;

import adolli.smsMelon.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author Administrator
 *
 */
public class NotificationHelper
{
	
	
	public static void pushNotification(Context context, String title, String content, Intent intent)
	{  
		// �������������һ��Id�ţ�ʹ�ý��������͵�֪ͨ���Խ��뵽��ͬ��������ϸ����
		int notificationId = (int)(Math.random() * 1000);
		
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_NO_HISTORY
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
        // �õ�NotificationManager  
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
        
        // ʵ����һ��֪ͨ��ָ��ͼ�ꡢ��Ҫ��ʱ��  
        Notification n = new Notification(R.drawable.ic_launcher, content, System.currentTimeMillis());  
        n.flags = Notification.FLAG_AUTO_CANCEL;
        
        // ָ��֪ͨ�ı��⡢���ݺ�intent  
        PendingIntent pi = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);  
        n.setLatestEventInfo(context, title, content, pi);  
        
        String taskid = intent.getStringExtra("postMessageTaskId");
        Log.d("i push intent", taskid);

        // ����֪ͨ  
        nm.notify(notificationId, n);  
    }
	
	
}
