/**
 * 
 */
package adolli.utility;

/**
 * @author 	Administrator
 * @brief	每次请求一个id则产生一个新的id号，保证程序每次需要id号不重复
 */
public class IdGenerator
{
	private static int start_id = 0x12340000;
	
	public static int getNewId()
	{
		return start_id++;
	}
	
}
