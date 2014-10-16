/**
 * 
 */
package adolli.widget.listView;

import java.util.Iterator;
import java.util.List;

import adolli.activity.UnitsUtil;
import adolli.utility.IdGenerator;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class ScrollListView<ItemType extends ListItem> extends ScrollView
{

	private String listName = null;
	private LinearLayout listLayout = null;
	private TextView tvItemCount = null;
	private int nItem = 0;
	
	
	public ScrollListView(Context context)
	{
		super(context);
		this.listName = "Item(s)";
		init();
	}
	
	public ScrollListView(Context context, String listName)
	{
		super(context);
		this.listName = listName;
		init();
	}
	
	private void init()
	{
		setId(IdGenerator.getNewId());
		
		final int dip_5 = UnitsUtil.dip2px(getContext(), 5);
		setPadding(dip_5, dip_5, dip_5, dip_5);
		
		// 必须是RelativeLayout.LayoutParam，否则该View无法在RelativeLayout中正确布局
		setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		listLayout = new LinearLayout(getContext());
		listLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		listLayout.setOrientation(LinearLayout.VERTICAL);
		addView(listLayout);
		
		tvItemCount = new TextView(getContext());
		int dip_15 = UnitsUtil.dip2px(getContext(), 15);
		int dip_25 = UnitsUtil.dip2px(getContext(), 25);
		tvItemCount.setPadding(dip_25, dip_15, dip_25, dip_25);
		tvItemCount.setTextColor(Color.LTGRAY);
		tvItemCount.setGravity(Gravity.CENTER);
		listLayout.addView(tvItemCount);
		updateListCountTextView();
	}
 
	
	public void setListAdapter(List<ItemType> list)
	{
		listLayout.removeAllViews();
		for (Iterator<ItemType> it = list.iterator(); it.hasNext(); )
		{
			ItemType item = (ItemType) it.next();
			listLayout.addView(item);
		}
		listLayout.addView(tvItemCount);
		updateListCountTextView();
	}
	
	
	/**
	 * @brief	在列表中添加项目
	 * @param 	item 要添加的项目
	 */
	public void addListItem(ItemType item)
	{
		listLayout.addView(item, nItem);
		++nItem;
		updateListCountTextView();
	}
	
	
	/**
	 * @brief	在列表的最后一项添加一个总计textView
	 */
	protected void updateListCountTextView()
	{
		tvItemCount.setText(nItem + " 个" + listName);
	}
	
	
	
	
	/**
	 * @brief	移除列表中与v相同的那一项，此方法转发给内部实际的LinearLayout
	 * @param	v_rm 要移除的View
	 */
	@Override
	public void removeView(View v_rm)
	{
		for (int i = 0; i < listLayout.getChildCount(); ++i)
		{
			View v = listLayout.getChildAt(i);
			if (v.equals(v_rm))
			{
				listLayout.removeViewAt(i);
				--nItem;
				updateListCountTextView();
				return;
			}
		}
	}
	
	
	
	/**
	 * @brief	移除列表列表全部项目，同clearList
	 */
	@Override
	public void removeAllViews()
	{
		clearList();
	}
	
	
	
	
	/**
	 * @brief	获取列表当前所有项目的个数
	 * @return	所有项目的个数，包括显示和没显示出来的
	 */
	public int getItemCount()
	{
		return listLayout.getChildCount() - 1;
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
	
	
	
	public ViewGroup getListLayout()
	{
		return listLayout;
	}
	
	
	public void hideItemIndexOf(int index)
	{
		if (listLayout.getChildAt(index).getVisibility() != View.GONE)
		{
			listLayout.getChildAt(index).setVisibility(View.GONE);
			--nItem;
			updateListCountTextView();
		}
		
	}
	
	
	public void unhideItemIndexOf(int index)
	{
		if (listLayout.getChildAt(index).getVisibility() != View.VISIBLE)
		{
			listLayout.getChildAt(index).setVisibility(View.VISIBLE);
			++nItem;
			updateListCountTextView();
		}
	}
	
	
	/**
	 * @brief	清空整个列表
	 */
	public void clearList()
	{
		listLayout.removeAllViews();
		nItem = 0;
		listLayout.addView(tvItemCount);
		updateListCountTextView();
	}
	
}
