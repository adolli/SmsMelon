/**
 * 
 */
package adolli.utility;

/**
 * @author 	Administrator
 * @brief	ÿ������һ��id�����һ���µ�id�ţ���֤����ÿ����Ҫid�Ų��ظ�
 */
public class IdGenerator
{
	private static int start_id = 0x12340000;
	
	public static int getNewId()
	{
		return start_id++;
	}
	
}
