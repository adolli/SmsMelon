/**
 * 
 */
package adolli.contacts;

/**
 * @author Administrator
 *
 */
public interface ContentNonContinuousSequenceMatchable
{
	
	/**
	 * @brief	判断是否有非连续子序列匹配
	 * @param 	seq 子序列
	 * @return	如果能匹配则返回true，否则false
	 */
	boolean match(String seq);
	
}
