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
 * @brief	蜜瓜消息的后台服务
 * 			启动并管理一个BroadCastReceiver,接收系统的新短信广播，
 * 			维护一个SmsMelonProcessor列表，每个SmsMelonProcessor数据从文件载入
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
	 * @brief	SmsMelon的服务绑定器
	 * 			当服务被绑定的时候，会返回一个该服务绑定接口的一个实例给绑定者
	 * 			服务可以在绑定接口里定义相关功能，绑定者可以使用这个服务绑定接口中的方法
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
     * @brief	当服务被启动的时候首先执行的方法在这个方法中可以进行一些资源的初始化
     */
    @Override
    public void onCreate()
    {
        Log.i("SmsMelonService", "SmsMelonService created");
        super.onCreate();
        
        smsMelonProcessors = new LinkedList<SmsMelonProcessor>();
		smsBroadCastReceiver = new SmsBroadCastReceiver();
        
     	
     	// 准备好数据库
     	DatabaseHelper database = new DatabaseHelper(this, "SmsMelonDB");
     	
     	// 群发任务列表（如果已经存在则不受影响）
     	// 列PostMessageTableName 为群发任务表名
     	// 列PostMessageStatus 为群发任务状态，0为未完成，1为已完成
     	// 列PostMessageAbstract 为消息摘要
     	//database.dropTable("PostsList");
     	database.createTable("PostsList", new String[]{
        		"PostMessageTableName TEXT", 
        		"PostMessageStatus INTEGER",
        		"PostMessageAbstract TEXT",
        		"PostMessageTimeStamp TEXT",
        		"ReceiverCount INTEGER" });

     	// 查询表PostsList，将未完成的PostMessage加载到SmsMelon任务处理器中
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
     	
     	// 注册一个新短信的广播接收器，并设为最高优先级
     	IntentFilter intentFilter = new IntentFilter();
     	intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
     	intentFilter.setPriority(Integer.MAX_VALUE);
     	registerReceiver(smsBroadCastReceiver, intentFilter);
     	
    }
 
    
    /**
     * @brief	新建一个SmsMelon处理器，在后台处理一个群发任务
     * @return	新建的处理器
     */
    protected SmsMelonProcessor createNewSmsMelonProcessor()
    {
    	SmsMelonProcessor smp = new SmsMelonProcessor(this);
    	smsMelonProcessors.add(smp);
    	return smp;
    }
    
    
    
    /**
     * @brief	清除所有任务
     */
    public void clearAllPostMessageTask()
    {
    	// 清除数据库中的数据
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
    	
    	// 清除MelonProcessors中的数据
    	smsMelonProcessors.clear();
    }
    
    
    
    /**
     * @brief	服务被停止的时候执行的方法，通常在这里销毁使用的资源
     */
    @Override
    public void onDestroy()
    {
        // 注销广播接收器
        unregisterReceiver(smsBroadCastReceiver);
        
        // 更新所有SmsMelon任务处理器的任务状态到数据库中，下次启动服务的时候再加载它们
        for (Iterator<SmsMelonProcessor> it = smsMelonProcessors.iterator(); it.hasNext(); )
        {
        	SmsMelonProcessor smp = (SmsMelonProcessor) it.next();
        	smp.updatePostMessageInfo();
        }
        
        super.onDestroy();
        Log.i("SmsMelonService", "SmsMelonService destroyed");
    }
 
    
    
    /**
     * @brief	当启动服务的时候要调用的方法，在这个方法中我们可以得到从Activity中传过来的Intent，从而进行相关的操作
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
