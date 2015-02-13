import java.util.ArrayList;

import group5.trackerexpress.Claim;
import group5.trackerexpress.R;
import group5.trackerexpress.ViewClaimActivity;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class ViewClaimActivityTest extends
		ActivityInstrumentationTestCase2<ViewClaimActivity> {
	
	Instrumentation instrumentation;
	
	public ViewClaimActivityTest() {
		super(ViewClaimActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void deleteExpenseTest (){
		ListView lv_before = (ListView) findViewById(R.id.lv_expenses);
		ArrayAdapter<Expense> before = (ArrayAdapter<Claim>) lv_before.getAdapter();
		
		Activity activity = getActivity();
		
		String title = "Expense title";
		addExpense(title);
		
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		instrumentation.waitForIdleSync();
	}
	
	public void editExpenseTest() {
		
	}
	
	private void addExpense(final String name){
		final Button b_addExpense = (Button) findViewById(R.id.b_add_expense);
		final EditText et_expenseTitle = (EditText) findViewById(R.id.et_expense_title);
		final Button b_saveExpense = (Button) findViewById(R.id.b_save_expense);
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				b_addExpense.performClick();
				et_expenseTitle.setText( name );
				b_saveExpense.performClick();
				// Assuming adding an incomplete claim will not prompt a new message
			}
			
		});
		
		instrumentation.waitForIdleSync();
	}
}
