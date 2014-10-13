/**
 * 
 */
package adolli.smsMelon;

import java.util.Iterator;
import java.util.LinkedList;
import adolli.smsMelon.SmsMelonProcessor.PostMessage.ReplyStruct;
import adolli.utility.DatabaseHelper;
import adolli.utility.NotificationHelper;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

/**
 * @author Administrator
 *
 */
public class SmsMelonProcessor
{


	public static class PostMessage
	{
		
		public static final int READY_TO_SEND = 0;
		public static final int MSG_SENT = 1;
		public static final int HAS_REPLIED = 2;
		public static final int FAIL_TO_SEND = 3;
		
	
		
		/**
		 * 回复消息和回复者结构
		 * @author Administrator
		 */
		public static class ReplyStruct
		{
			public String address;
			public String repliedContent;
			public int repliedStatus;
			
			public ReplyStruct(String addr)
			{
				address = addr;
				repliedContent = null;
				repliedStatus = READY_TO_SEND;
			}
			
			public ReplyStruct(String addr, int status, String content)
			{
				address = addr;
				repliedContent = content;
				repliedStatus = status;
			}
			
			public void setRepliedContent(String content)
			{
				repliedContent = content;
				repliedStatus = HAS_REPLIED;
			}
			
			public void setRepliedStatus(int status)
			{
				repliedStatus = status;
			}
		}
		
		public String task_id;
		public String messageAbstract;
		public LinkedList<ReplyStruct> receivers;
		
		public PostMessage(String _id, String msgAbstract)
		{
			task_id =_id;
			messageAbstract = msgAbstract;
			receivers = new LinkedList<ReplyStruct>();
		}
		
		public void addReceiver(ReplyStruct rs)
		{
			receivers.add(rs);
		}
	}
	
	private Context context;
	private PostMessage postMessageTask;
	
	public SmsMelonProcessor(Context context)
	{
		this.context = context;
	}
	
	
	/**
	 * 新建一个群发任务，并在数据库中创建新表
	 * @param receivers 接收者与发送消息列表
	 */
	public void createNewPostMessageTask(LinkedList<Sms> receivers, String msgAbstract)
	{
		String task_id = "PM" + Long.toString(System.currentTimeMillis()) 
				+ Integer.toString((int) (Math.random() * 10000));
		postMessageTask = new PostMessage(task_id, msgAbstract);
		for (Iterator<Sms> it = receivers.iterator(); it.hasNext(); )
		{
			Sms sms = it.next();
			postMessageTask.addReceiver(new ReplyStruct(sms.address, PostMessage.MSG_SENT, ""));
			SmsHelper.sendSms(sms);
		}
		DatabaseHelper database = new DatabaseHelper(context, "SmsMelonDB");
		database.insertInto("PostsList", new String[]{ 
				"'" + postMessageTask.task_id + "'",
				"0",
				"'" + postMessageTask.messageAbstract + "'"});
		database.close();
		updatePostMessageInfo();
	}
	
	
	
	/**
	 * 载入该任务的数据到内存中
	 * @param task_id 任务id号
	 * @param msgAbstract 消息摘要
	 */
	public void loadPostMessageInfo(String task_id, String msgAbstract)
	{
		postMessageTask = new PostMessage(task_id, msgAbstract);
		
		// 从数据库中载入数据
		DatabaseHelper database = new DatabaseHelper(context, "SmsMelonDB");
		Cursor c = database.getReadableDatabase().query(
				postMessageTask.task_id, 
				new String[]{"msgAddress", "msgStatus", "msgReplied"}, 
				null, null, 
				null, 				// GroupBy
				null, 
				"msgStatus ASC");	// order by	
		while (c.moveToNext()) 
		{
			String receiverAddress = c.getString(0);
			int iReplyStatus = c.getInt(1);
			String replyMessage = c.getString(2);
			replyMessage = DatabaseHelper.deEscape(replyMessage);
			postMessageTask.addReceiver(new ReplyStruct(receiverAddress, iReplyStatus, replyMessage));
		}
		c.close();
		database.close();
	}
	
	
	/**
	 * 将内存中的任务数据全部更新到数据库中
	 */
	public void updatePostMessageInfo()
	{
		// 采用整张表删除然后重新覆盖更新的策略
		DatabaseHelper database = new DatabaseHelper(context, "SmsMelonDB");
		database.dropTable(postMessageTask.task_id);
		database.createTable(postMessageTask.task_id, new String[]{
        		"msgAddress TEXT", 
        		"msgStatus INTEGER",
        		"msgReplied TEXT" });
		for (Iterator<ReplyStruct> it = postMessageTask.receivers.iterator(); it.hasNext(); )
		{
			ReplyStruct rs = it.next();
			database.insertInto(postMessageTask.task_id, new String[]{ 
					"'" + rs.address + "'", 
					Integer.toString(rs.repliedStatus),
					"'" + DatabaseHelper.makeEscape(rs.repliedContent) + "'" });
		}
		
		database.close();
	}
	
	
	/**
	 * 标记已回复的人
	 * @param addr 接收者地址
	 * @param content 回复的消息内容
	 * @return 如果成功标记则返回true，否则返回false
	 */
	public boolean markReplied(String addr, String repliedContent)
	{
		boolean ret = false;
		boolean allReplied = true;
		for (Iterator<ReplyStruct> it = postMessageTask.receivers.iterator(); it.hasNext(); )
		{
			ReplyStruct rs = it.next();
			// 只有未回复且号码匹配的才可以标记
			if (rs.address.equals(addr) && rs.repliedStatus == PostMessage.MSG_SENT)
			{
				// 设置回复的内容和状态
				rs.setRepliedContent(repliedContent);
				
				// 并同时在数据库中更新
				DatabaseHelper database = new DatabaseHelper(context, "SmsMelonDB");
				database.update(postMessageTask.task_id,
						new String[]{ "msgStatus", "msgReplied" }, 
						new String[]{  Integer.toString(rs.repliedStatus), "'" + rs.repliedContent + "'" },
						"msgAddress = '" + addr + "'");
				database.close();
				ret = true;
			}
			
			if (rs.repliedStatus != PostMessage.HAS_REPLIED)
			{
				allReplied = false;
			}
		}
		
		if (allReplied)
		{
			// mark this process as all replied!
			DatabaseHelper database = new DatabaseHelper(context, "SmsMelonDB");
			database.update("PostsList", "PostMessageStatus = 1", "PostMessageTableName = '" + postMessageTask.task_id + "'");
			database.close();
			
			// send a notification
			Intent intent = new Intent(context, PostMessageDetailStatusActivity.class);
			intent.putExtra("postMessageTaskId", postMessageTask.task_id);
			NotificationHelper.pushNotification(context,
					"你的通知所有人都回复了！", 
					"您的通知：" + postMessageTask.messageAbstract + " 已经全部回复", 
					intent);
		}
		return ret;
	}
	
}
