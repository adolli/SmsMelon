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
		// 仅仅是随机产生一个Id号，使得进入点击推送的通知可以进入到不同的任务详细界面
		int notificationId = (int)(Math.random() * 1000);
		
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_NO_HISTORY
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
        // 得到NotificationManager  
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
        
        // 实例化一个通知，指定图标、概要、时间  
        Notification n = new Notification(R.drawable.ic_launcher, content, System.currentTimeMillis());  
        n.flags = Notification.FLAG_AUTO_CANCEL;
        
        // 指定通知的标题、内容和intent  
        PendingIntent pi = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);  
        n.setLatestEventInfo(context, title, content, pi);  
        
        String taskid = intent.getStringExtra("postMessageTaskId");
        Log.d("i push intent", taskid);

        // 发送通知  
        nm.notify(notificationId, n);  
    }
	
	
}
