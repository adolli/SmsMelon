/**
 * 
 */
package adolli.contacts;

import adolli.activity.UnitsUtil;
import adolli.utility.IdGenerator;
import adolli.utility.StringUtil;
import adolli.widget.listView.ListItem;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class ContactListItem extends ListItem
{
	
	protected TextView tvName = null;
	protected TextView tvPhoneNumber = null;
	protected String pinyinName = null;
	
	private final int _id_tvName = IdGenerator.getNewId();
	
	
	public ContactListItem(Context context)
	{
		super(context);
		
		final int dip_10 = UnitsUtil.dip2px(getContext(), 10);
		final int dip_6 = UnitsUtil.dip2px(getContext(), 6);
		setPadding(dip_10, dip_6, dip_10, dip_6);
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		tvName = new TextView(getContext());
		tvName.setId(_id_tvName);
		tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		tvName.setTextColor(Color.DKGRAY);
		tvPhoneNumber = new TextView(getContext());
		
		addView(tvName);
		addView(tvPhoneNumber);
		
		RelativeLayout.LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.BELOW, tvName.getId());
		tvPhoneNumber.setLayoutParams(rlp);
	}



	
	/**
	 * @brief	����������ʾ�������͵绰
	 * @param 	name ����
	 * @param 	phoneNumber �绰����
	 */
	public void setContent(String name, String phoneNumber, String pinyin)
	{
		tvName.setText(name);
		tvPhoneNumber.setText(phoneNumber);
		pinyinName = pinyin;
	}
	
	
	

	/**
	 * @brief	ȡ�ø���ϵ���б����Ӧ����ϵ����Ϣ
	 * @return	��ϵ����Ϣ
	 */
	public ContactsHelper.ContactInfo getContactInfo()
	{
		return new ContactsHelper.ContactInfo(
				tvName.getText().toString(), 
				tvPhoneNumber.getText().toString(), 
				pinyinName);
	}
	
	
	public String getName()
	{
		return tvName.getText().toString();
	}


	public String getPhoneNumber()
	{
		return tvPhoneNumber.getText().toString();
	}
	
	




	/**
	 * @brief	������ϵ���ֶ����Ƿ�����ַ����������������������
	 * @param 	seq ������
	 * @return	���������������з���true�����򷵻�false
	 */
	@Override
	public boolean match(String seq)
	{
		String name = tvName.getText().toString();
		String phone = tvPhoneNumber.getText().toString();
		if (StringUtil.containNonContiguousSubSequence(name, seq)
				|| StringUtil.containNonContiguousSubSequence(phone, seq) 
				|| StringUtil.containNonContiguousSubSequence(pinyinName, seq))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	
	
}
