/**
 * 
 */
package adolli.smsMelon;

import android.content.Context;

/**
 * @author Administrator
 *
 */
public class SmsMelonProcessor
{

	
	public static class PostMessage
	{
		public static final int HAS_REPLIED = 0;
		public static final int MSG_SENT = 1;
		public static final int FAIL_TO_SEND = 2;
		public static final int READY_TO_SEND = 3;
	}
	
	public SmsMelonProcessor(Context context)
	{
		
	}
	
	public void loadPostMessageInfo(String tableName, String msgAbstract)
	{
		
	}
	
	public void updatePostMessageInfo()
	{
		
	}
	
	public boolean markReplied(String addr, String content)
	{
		return true;
	}
}
