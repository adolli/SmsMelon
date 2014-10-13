/**
 * 
 */
package adolli.widget;

import java.util.List;

import adolli.activity.UnitsUtil;
import adolli.utility.IdGenerator;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * @author Administrator
 *
 */
public class ScrollListViewWithFilter<ItemType extends ListItem> extends RelativeLayout
{

	private EditText filterInput = null;
	private ScrollListView<ItemType> scrollListView = null;
	
	
	public ScrollListViewWithFilter(Context context)
	{
		super(context);
		scrollListView = new ScrollListView<ItemType>(context);
		init();
	}
	
	public ScrollListViewWithFilter(Context context, String listName)
	{
		super(context);
		scrollListView = new ScrollListView<ItemType>(context, listName);
		init();
	}
	
	
	private void init()
	{
		setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 
				LayoutParams.WRAP_CONTENT));
		
		filterInput = new EditText(getContext());
		filterInput.setId(IdGenerator.getNewId());
		final int dip_5 = UnitsUtil.dip2px(getContext(), 5);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(dip_5, dip_5, dip_5, 0);
		filterInput.setLayoutParams(lp);
		filterInput.setHint("搜索");
		filterInput.setEms(8);
		filterInput.setSingleLine(true);
		filterInput.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				filter(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
			}
		});
		addView(filterInput);
		
		scrollListView.addLayoutRules(RelativeLayout.BELOW, filterInput.getId());
		addView(scrollListView);
	}
	
	
	
	
	/**
	 * @brief	在显示的列表中过滤掉不满足matched的联系人信息
	 * @param 	matched 过滤的内容，即显示包含matched串的字符
	 */
	private void filter(String matched)
	{
		ViewGroup vg = (ViewGroup) getContactList();
		for (int i = 0; i < vg.getChildCount() - 1; ++i)
		{
			try
			{
				ListItem sc = (ListItem) vg.getChildAt(i);
				if (!sc.match(matched))
				{
					hideItemIndexOf(i);
				}
				else
				{
					unhideItemIndexOf(i);
				}
			}
			catch (ClassCastException e)
			{
				// just do nothing
			}
		}
	}
	
	

	/**
	 * @brief	添加该View的布局规则
	 * @param 	verb 规则名
	 * @param 	view_id 规则参数 
	 */
	public void addLayoutRules(int verb, int view_id)
	{
		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) getLayoutParams();
		rlp.addRule(verb, view_id);
		setLayoutParams(rlp);
	}
	
	
	
	
	public void setListAdapter(List<ItemType> list)
	{
		scrollListView.setListAdapter(list);
	}
	
	
	/**
	 * @brief	在列表中添加项目
	 * @param 	item 要添加的项目
	 */
	public void addListItem(ItemType item)
	{
		scrollListView.addListItem(item);
	}
	
	
	
	/**
	 * @brief	在列表的最后一项添加一个总计textView
	 */
	protected void updateListCountTextView()
	{
		scrollListView.updateListCountTextView();
	}
	
	
	
	
	/**
	 * @brief	移除列表中与v相同的那一项，此方法转发给内部实际的LinearLayout
	 * @param	v_rm 要移除的View
	 */
	@Override
	public void removeView(View v_rm)
	{
		scrollListView.removeView(v_rm);
	}
	
	
	
	
	/**
	 * @brief	获取列表当前所有项目的个数
	 * @return	所有项目的个数，包括显示和没显示出来的
	 */
	public int getItemCount()
	{
		return scrollListView.getContactList().getChildCount() - 1;
	}
	
	
	
	public ViewGroup getContactList()
	{
		return scrollListView.getContactList();
	}
	
	
	public void hideItemIndexOf(int index)
	{
		scrollListView.hideItemIndexOf(index);
	}
	
	
	public void unhideItemIndexOf(int index)
	{
		scrollListView.unhideItemIndexOf(index);
	}
	
	
	/**
	 * @brief	清空整个列表
	 */
	public void clearList()
	{
		scrollListView.clearList();
	}
	
}
