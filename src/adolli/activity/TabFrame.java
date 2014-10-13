/**
 * 
 */
package adolli.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import adolli.smsMelon.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;



/**
 * Abstract TabActivity with icon+text TabSpec support for each Activity Sub
 * class need set "layout" and "selectDrawable"(tab selected background image)
 * in constructor And implement getMyTabList() to add tab configuration
 * 
 * @author adolli
 */
public abstract class TabFrame extends TabActivity
{
	private static String TAG_NAME = TabFrame.class.getSimpleName();

	private TabHost tabHost;

	private int tabLayout;
	private int selectDrawable;
	private Drawable selectBackground;

	private int textColor = Color.DKGRAY;
	private int selectTextColor = Color.BLACK;

	private Map<String, TabView> tabViewMap = new HashMap<String, TabView>();
	private String tabViewTagPrev = null;

	
	public abstract List<MyTab> getMyTabList();

	
	public static final int TAB_AT_BOTTOM = 0;
	public static final int TAB_AT_TOP = 1;
	
	
	public TabFrame()
	{
		this.tabLayout = R.layout.tab_bottom;
		this.selectDrawable = R.drawable.ic_launcher;
	}
	
	
	public TabFrame(int tabpos)
	{
		switch (tabpos)
		{
		case TAB_AT_BOTTOM :
			this.tabLayout = R.layout.tab_bottom;
			break;
			
		case TAB_AT_TOP :
			this.tabLayout = R.layout.tab_top;
			break;
			
		default :;
		}
		this.selectDrawable = R.drawable.ic_launcher;
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(tabLayout);
		tabHost = getTabHost(); // Get TabHost after setContentView()
		//
		initTabHost();
	}

	
	private void initTabHost()
	{
		selectBackground = this.getResources().getDrawable(selectDrawable);

		
		
		// Create TabSpec for each MyTab. The first tab is the default
		String defaultTag = null;
		TabView defaultTabView = null;
		List<MyTab> myTabList = getMyTabList();
		
		int index = 0;
		for (Iterator<MyTab> itMyTab = myTabList.iterator(); itMyTab.hasNext(); )
		{
			MyTab myTab = (MyTab) itMyTab.next();
			index++;
			String tag = Integer.toString(index);
			TabView view = new TabView(this, myTab.icon, myTab.text);
			TabSpec tabSpec = tabHost.newTabSpec(tag).setIndicator(view).setContent(new Intent(this, myTab.activityClass));
			tabViewMap.put(tag, view);
			tabHost.addTab(tabSpec);
			if (defaultTag == null)
			{
				defaultTag = tag;
				defaultTabView = view;
			}
		}
		
		// Listener on tab change
		tabHost.setOnTabChangedListener(new OnTabChangeListener()
		{
			@Override
			public void onTabChanged(String tabId)
			{
				Log.d(TAG_NAME, "change tab: id=" + tabId + ", prevId=" + tabViewTagPrev);
				if (tabViewTagPrev != null)
				{
					// Reset prev tab
					TabView tvPrev = tabViewMap.get(tabViewTagPrev);
					if (tvPrev != null)
					{
						tvPrev.setBackgroundColor(Color.LTGRAY);
						tvPrev.textView.setTextColor(textColor);
					}
				}
				// Set current selected tab
				TabView tv = tabViewMap.get(tabId);
				if (tv != null)
				{
					tv.setBackgroundColor(Color.WHITE);
					tv.textView.setTextColor(selectTextColor);
				}
				//
				tabViewTagPrev = tabId;
			}
		});
		
		// Set default tab
		if (defaultTag != null)
		{
			defaultTabView.setBackgroundColor(Color.WHITE);
			defaultTabView.textView.setTextColor(selectTextColor);
			tabViewTagPrev = defaultTag;
		}
	}

	
	/**
	 * @brief	Layout for each TabSpec
	 */
	private class TabView extends LinearLayout
	{
		private TextView textView;

		public TabView(Context c, int icon, String text)
		{
			super(c);
			setOrientation(VERTICAL);
			setGravity(Gravity.CENTER);
			setBackgroundColor(Color.LTGRAY);

			textView = new TextView(c);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
			textView.setText(text);
			textView.setTextColor(textColor);
			textView.setGravity(Gravity.CENTER);
			addView(textView);
		}
	}

	
	/**
	 * Options for each TabSpec. Icon + Text + Activity
	 */
	public class MyTab
	{
		private int icon;
		private String text;
		private Class<? extends Activity> activityClass;

		public MyTab(int icon, String text, Class<? extends Activity> activity)
		{
			this.icon = icon;
			this.text = text;
			this.activityClass = activity;
		}
	}
}
