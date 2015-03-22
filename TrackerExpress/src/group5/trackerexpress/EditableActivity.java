package group5.trackerexpress;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class EditableActivity extends Activity{
	
	private Map<EditText, TextWatcher> viewMap = new HashMap<EditText, TextWatcher>();
	
	//TODO: write up the javadocs - Jesse
	//TODO: Editable classes possibly extend this class?
	public void limitLength(EditText view, int length) {
		limitLength(view, length, "Only up to " + length + " chars");
	}
	
	public void limitLength(final EditText view, final int length, final String message) {
		
		TextWatcher lengthWatcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == length+1) {
					view.setError(message);
					s.delete(length, length+1);
				}
			}
		};
		
		view.addTextChangedListener(lengthWatcher);
		final CharSequence hintText = view.getHint();
		final CharSequence extraHint = " (at most " + length + " chars)";
		
		view.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					view.setHint(hintText);
				} else {
					view.setHint(Html.fromHtml(hintText + "<small>" + extraHint + "</small>"));
				}
				
			}
		});
		viewMap.put(view, lengthWatcher);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		for (Map.Entry<EditText, TextWatcher> entry : viewMap.entrySet())
		{
		    entry.getKey().removeTextChangedListener(entry.getValue());
		}
		
		viewMap.clear();
	}
}