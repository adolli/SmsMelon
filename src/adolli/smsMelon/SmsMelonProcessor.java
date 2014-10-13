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
		 * �ظ���Ϣ�ͻظ��߽ṹ
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
	 * �½�һ��Ⱥ�����񣬲������ݿ��д����±�
	 * @param receivers �������뷢����Ϣ�б�
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
	 * �������������ݵ��ڴ���
	 * @param task_id ����id��
	 * @param msgAbstract ��ϢժҪ
	 */
	public void loadPostMessageInfo(String task_id, String msgAbstract)
	{
		postMessageTask = new PostMessage(task_id, msgAbstract);
		
		// �����ݿ�����������
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
	 * ���ڴ��е���������ȫ�����µ����ݿ���
	 */
	public void updatePostMessageInfo()
	{
		// �������ű�ɾ��Ȼ�����¸��Ǹ��µĲ���
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
	 * ����ѻظ�����
	 * @param addr �����ߵ�ַ
	 * @param content �ظ�����Ϣ����
	 * @return ����ɹ�����򷵻�true�����򷵻�false
	 */
	public boolean markReplied(String addr, String repliedContent)
	{
		boolean ret = false;
		boolean allReplied = true;
		for (Iterator<ReplyStruct> it = postMessageTask.receivers.iterator(); it.hasNext(); )
		{
			ReplyStruct rs = it.next();
			// ֻ��δ�ظ��Һ���ƥ��Ĳſ��Ա��
			if (rs.address.equals(addr) && rs.repliedStatus == PostMessage.MSG_SENT)
			{
				// ���ûظ������ݺ�״̬
				rs.setRepliedContent(repliedContent);
				
				// ��ͬʱ�����ݿ��и���
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
					"���֪ͨ�����˶��ظ��ˣ�", 
					"����֪ͨ��" + postMessageTask.messageAbstract + " �Ѿ�ȫ���ظ�", 
					intent);
		}
		return ret;
	}
	
}
