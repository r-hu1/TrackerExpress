package group5.trackerexpress;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

// http://androidcocktail.blogspot.it/2012/04/adding-checkboxes-to-custom-listview-in.html
/**
 * List object for the tag list. Helps keeps the complexity of the tag list fragment class down. 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class MainTagListAdapter extends ArrayAdapter<Tag> {
	
	/** The tag list. */
	private ArrayList<Tag> tagList;
	
	/** The context. */
	private Context context;
	
	/** The check box state. */
	boolean[] checkBoxState;
	
	/**
	 * Instantiates a new main tag list adapter.
	 *
	 * @param context Needed for file IO
	 * @param tags are the tags to be displayed
	 */
	public MainTagListAdapter(Context context, ArrayList<Tag> tags){
		super(context, R.layout.fragment_tags_list_item, tags);
		this.tagList = tags;
		this.context = context;

		checkBoxState = new boolean[tags.size()];

		for ( int i = 0; i < tags.size(); i++ ){
			checkBoxState[i] = tags.get(i).isSelected();
		}
		
	}
	
	
	/**
	 * The Class TagHolder holds views for custom listview
	 */
	private static class TagHolder{
		
		/** The tag name. */
		public TextView tagName;
		
		/** The chk box. */
		public CheckBox chkBox;
	}
	
	/**
	 * Updates the respective listview item in the custom listview
	 * 
	 * @param position: index of item
	 * @param convertView: view of item
	 * @param parent: parent of item ( the listView )
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		View v = convertView;
		TagHolder holder = new TagHolder();
		
		if ( convertView == null ){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.fragment_tags_list_item, null);
			
			holder.tagName = (TextView) v.findViewById(R.id.tv_tags_list_item);
			holder.chkBox = (CheckBox) v.findViewById(R.id.cb_tags_list_item);
		
			v.setTag(holder);
		} else {
			holder = (TagHolder) v.getTag();
		}
		
		Tag t = tagList.get(position);
		holder.tagName.setText(t.toString());
		holder.chkBox.setChecked(t.isSelected());
		holder.chkBox.setTag(t);
		
		holder.chkBox.setChecked(checkBoxState[position]);

		holder.chkBox.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((CheckBox)v).isChecked()){
					checkBoxState[position] = true;
					tagList.get(position).setSelected(context, true);
				} else {
					checkBoxState[position] = false;
					tagList.get(position).setSelected(context, false);
				}
			}	
		});
		
		return v;
	}
}
