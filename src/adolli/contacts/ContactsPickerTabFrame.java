package adolli.contacts;

import java.util.ArrayList;
import java.util.List;

import adolli.activity.TabFrame;
import adolli.smsMelon.PostsListActivity;
import adolli.smsMelon.R;

public class ContactsPickerTabFrame extends TabFrame
{
	
	public ContactsPickerTabFrame()
	{
		super(TAB_AT_TOP);
	}
	
	@Override
	public List<MyTab> getMyTabList()
	{
		List<MyTab> myTabList = new ArrayList<MyTab>();
        myTabList.add(new MyTab(R.drawable.ic_launcher, "��ϵ��", ContactsPickerActivity.class)); 
        myTabList.add(new MyTab(R.drawable.ic_launcher, "Ⱥ��", PostsListActivity.class)); 
        myTabList.add(new MyTab(R.drawable.ic_launcher, "��ѡ��", ContactsSelectedActivity.class));

        return myTabList; 
	}
	
	
}
