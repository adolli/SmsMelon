/**
 * 
 */
package adolli.utility;

import java.util.Locale;




/**
 * @author Administrator
 *
 */
public class StringUtil
{
	
	public static boolean containNonContiguousSubSequence(String str, String subseq)
	{
		str = str.toLowerCase(Locale.US);
		subseq = subseq.toLowerCase(Locale.US);
		int offsetIndexOfStr = str.length();
		for (int i = subseq.length() - 1; i >= 0; --i)
		{
			char c = subseq.charAt(i);
			offsetIndexOfStr = str.lastIndexOf(c, offsetIndexOfStr - 1);	// 从找到的字符开始向前移动一个字符，所以要减一
			if (offsetIndexOfStr == -1)
			{
				return false;
			}
		}
		return true;
	}
	
	
	
	
	
}
