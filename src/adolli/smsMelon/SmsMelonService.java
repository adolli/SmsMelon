/**
 * 
 */
package adolli.smsMelon;

import java.util.Iterator;
import java.util.LinkedList;

import adolli.utility.DatabaseHelper;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;



/**
 * @brief	�۹���Ϣ�ĺ�̨����
 * 			����������һ��BroadCastReceiver,����ϵͳ���¶��Ź㲥��
 * 			ά��һ��SmsMelonProcessor�б�ÿ��SmsMelonProcessor���ݴ��ļ�����
 * @author 	adolli
 *
 */
public class SmsMelonService extends Service 
{

	private LinkedList<SmsMelonProcessor> smsMelonProcessors = null;
	private SmsBroadCastReceiver smsBroadCastReceiver = null;
	
	 
	protected LinkedList<SmsMelonProcessor> getSmsMelonProcessors()
	{
		return smsMelonProcessors;
	}
	
	
	
	/**
	 * @brief	SmsMelon�ķ������
	 * 			�����񱻰󶨵�ʱ�򣬻᷵��һ���÷���󶨽ӿڵ�һ��ʵ��������
	 * 			��������ڰ󶨽ӿ��ﶨ����ع��ܣ����߿���ʹ���������󶨽ӿ��еķ���
	 * @author 	Administrator
	 *
	 */
	public class SmsMelonServiceBinder extends Binder 
	{
		public SmsMelonService getService()
		{
			return SmsMelonService.this;
		}
	}
	
	private IBinder binder = new SmsMelonServiceBinder();
	 
	@Override
	public IBinder onBind(Intent intent) 
	{
		return binder;
	}
	

	
	

	
	
	/**
     * @brief	������������ʱ������ִ�еķ�������������п��Խ���һЩ��Դ�ĳ�ʼ��
     */
    @Override
    public void onCreate()
    {
        Log.i("SmsMelonService", "SmsMelonService created");
        super.onCreate();
        
        smsMelonProcessors = new LinkedList<SmsMelonProcessor>();
		smsBroadCastReceiver = new SmsBroadCastReceiver();
        
     	
     	// ׼�������ݿ�
     	DatabaseHelper database = new DatabaseHelper(this, "SmsMelonDB");
     	
     	// Ⱥ�������б�����Ѿ���������Ӱ�죩
     	// ��PostMessageTableName ΪȺ���������
     	// ��PostMessageStatus ΪȺ������״̬��0Ϊδ��ɣ�1Ϊ�����
     	// ��PostMessageAbstract Ϊ��ϢժҪ
     	//database.dropTable("PostsList");
     	database.createTable("PostsList", new String[]{
        		"PostMessageTableName TEXT", 
        		"PostMessageStatus INTEGER",
        		"PostMessageAbstract TEXT",
        		"PostMessageTimeStamp TEXT",
        		"ReceiverCount INTEGER" });

     	// ��ѯ��PostsList����δ��ɵ�PostMessage���ص�SmsMelon����������
     	Cursor c = database.getReadableDatabase().query(
     			"PostsList", 
				new String[]{ "PostMessageTableName", "PostMessageStatus", "PostMessageAbstract", "PostMessageTimeStamp" }, 
				null, null, null, null, null);
		while (c.moveToNext())
		{
			if (c.getInt(1) == 0)
			{
				String processorTableName = c.getString(0);
				String processorMsgAbstract = c.getString(2);
				String processorMsgTimeStamp = c.getString(3);
				SmsMelonProcessor smp = new SmsMelonProcessor(this);
				smp.loadPostMessageInfo(processorTableName, processorMsgAbstract, processorMsgTimeStamp);
				smsMelonProcessors.add(smp);
			}
		}
		c.close();
		
     	database.close();
     	
     	// ע��һ���¶��ŵĹ㲥������������Ϊ������ȼ�
     	IntentFilter intentFilter = new IntentFilter();
     	intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
     	intentFilter.setPriority(Integer.MAX_VALUE);
     	registerReceiver(smsBroadCastReceiver, intentFilter);
     	
    }
 
    
    /**
     * @brief	�½�һ��SmsMelon���������ں�̨����һ��Ⱥ������
     * @return	�½��Ĵ�����
     */
    protected SmsMelonProcessor createNewSmsMelonProcessor()
    {
    	SmsMelonProcessor smp = new SmsMelonProcessor(this);
    	smsMelonProcessors.add(smp);
    	return smp;
    }
    
    
    
    /**
     * @brief	�����������
     */
    public void clearAllPostMessageTask()
    {
    	// ������ݿ��е�����
    	DatabaseHelper database = new DatabaseHelper(this, "SmsMelonDB");
    	Cursor c = database.getReadableDatabase().query(
    			"PostsList", 
    			new String[]{"PostMessageTableName"}, 
    			null, null, null, null, null);
    	while (c.moveToNext())
    	{
    		String tableName = c.getString(0);
    		database.dropTable(tableName);
    	}
    	c.close();
    	
    	database.clearTable("PostsList");
    	database.close();
    	
    	// ���MelonProcessors�е�����
    	smsMelonProcessors.clear();
    }
    
    
    
    /**
     * @brief	����ֹͣ��ʱ��ִ�еķ�����ͨ������������ʹ�õ���Դ
     */
    @Override
    public void onDestroy()
    {
        // ע���㲥������
        unregisterReceiver(smsBroadCastReceiver);
        
        // ��������SmsMelon��������������״̬�����ݿ��У��´����������ʱ���ټ�������
        for (Iterator<SmsMelonProcessor> it = smsMelonProcessors.iterator(); it.hasNext(); )
        {
        	SmsMelonProcessor smp = (SmsMelonProcessor) it.next();
        	smp.updatePostMessageInfo();
        }
        
        super.onDestroy();
        Log.i("SmsMelonService", "SmsMelonService destroyed");
    }
 
    
    
    /**
     * @brief	�����������ʱ��Ҫ���õķ�������������������ǿ��Եõ���Activity�д�������Intent���Ӷ�������صĲ���
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("SmsMelonService", "SmsMelonService command");
        return super.onStartCommand(intent, flags, startId);
    }
    
    
    public void debugHeHe()
    {
    	Log.d("adolli", "hehe!!");
    }
    
}
