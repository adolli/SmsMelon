/**
 * 
 */
package adolli.characters;

import net.sourceforge.pinyin4j.PinyinHelper;  
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;  
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;  
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;  
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;  
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;  

public class Pinyin
{
	
	public static String Chinese2Pinyin(String str)
	{
		StringBuilder result = new StringBuilder(32);
		String[] pinyin_of_each_char = new String[str.length()];

		//char[] hanzi = new char[str.length()];
		//str.getChars(0, str.length(), hanzi, 0);

		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		try
		{
			int strlen = str.length();
			for (int i = 0; i < strlen; i++)
			{
				char ch = str.charAt(i);
				
				if ((ch >= 'a' && ch < 'z')
						|| (ch >= 'A' && ch <= 'Z')
						|| (ch >= '0' && ch <= '9'))
				{
					result.append(ch);
				}
				else
				{
					pinyin_of_each_char = PinyinHelper.toHanyuPinyinStringArray(ch, format);
					if (pinyin_of_each_char == null || pinyin_of_each_char.length == 0)
					{
						result.append(ch);
					}
					else
					{
						result.append(pinyin_of_each_char[0]);
					}
				}
			}
		}
		catch (BadHanyuPinyinOutputFormatCombination e)
		{
			e.printStackTrace();
		}

		return result.toString();
	}

	
	public static void yyymain()
	{
		System.out.println(Pinyin.Chinese2Pinyin("ºº×Ö×ªÆ´Òô"));
	}
	
	
}
