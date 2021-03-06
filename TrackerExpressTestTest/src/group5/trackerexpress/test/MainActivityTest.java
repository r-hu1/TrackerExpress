/*
package group5.trackerexpress.test;

import group5.trackerexpress.Claim;
import group5.trackerexpress.Controller;
import group5.trackerexpress.Expense;
import group5.trackerexpress.MainActivity;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import group5.trackerexpress.R;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	
	Instrumentation instrumentation;

	public MainActivityTest(String name) {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	// this is a test case for 08.07.01
	public void testReturnClaim(){
		final ListView claimlist = (ListView) findViewById(R.id.approve_claim_list);
		final Adapter adapter = claimlist.getAdapter(); 
		final TextView claim_name;
		final Button b_returnClaim = (Button) findViewById(R.id.b_return_claim);
		final EditText et_comments = (EditText) findViewById(R.id.et_comments)
		final TextView returned;
		
		final int claimId;
		
		instrumentation.runOnMainSync(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				clickTab(MainActivity.INDEX_OF_APPROVE_CLAIMS_TAB);
				// main activity will have an approve_claims_tab
				try{
					// Assumes there is a claim already submitted
					claimId = adapter.getItem(0).getId();
				} catch (ArrayIndexOutOfBoundsException e) {
					return;
				}
				
				returned = (TextView) adapter.getView(0, claim_name, claimlist);
				returned.performClick();
				// View claim and there will be a visible input box to leave comments
				et_comments.setText("whatever");
				b_returnClaim.performClick();
				// Return to list of approved claims
			}
			
				
		});
		
		instrumentation.waitForIdleSync();
		
		// Check if list is now empty
		try{
			adapter.getItem(0);
		} catch (ArrayIndexOutOfBoundsException e) {
			assertTrue( "claim removed from list?", true); // Claim is successfully removed from the list 
		}
		
		// Checks if the first claim is different from the original first claim ( i.e. claim is removed after being returned )
		int firstClaimId = adapter.getItem(0).getId();
		assertTrue( "claim removed from list?", firstClaimId != claimId );
		
	}
	
	// this is test case for 01.05.01
	public void testDeleteClaim (){
		final ListView claimlist = (ListView) findViewById(R.id.claimlist);
		final ArrayAdapter<Claim> adapter = (ArrayAdapter<Claim>) claimlist.getAdapter();
		final TextView claim_name;
		final Button b_deleteClaim = (Button) findViewById(R.id.b_deleteClaim);
		
		Activity activity = getActivity();
		
		String title = "A Claim";
		addClaim(title);
		
		final TextView returned;
		
		String retrieved = adapter.getItem(0).getTitle();
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				returned = (TextView) adapter.getView(0, claim_name, claimlist);
				returned.performLongClick();
				b_deleteClaim.performClick();
			}
			
		});
				
		instrumentation.waitForIdleSync();

		// check if the first item is still the first item (i.e. claim deleted)
		assertTrue( !(adapter.getItem(0).getTitle().equals(title)) );
	}
	
	// this is test case for 01.04.01
	public void testEditclaim() {
		final String rename = "new claim name";
		
		final ListView claimlist = (ListView) findViewById(R.id.claimlist);
		final Button b_editclaim = (Button) findViewById(R.id.b_editclaim);
		final EditText et_claim_new_name = (EditText) findViewById(R.id.et_claim_new_name);
		final Button b_saveedit = (Button) findViewById(R.id.b_saveedit);
		final Button b_cancleedit = (Button) findViewById(R.id.b_cancleedit);
		final TextView claimlistItem;
		final ArrayAdapter<Claim> adapter = (ArrayAdapter<Claim>) claimlist.getAdapter();
		
		Activity activity = getActivity();
		addClaim(rename);
		
		String retrived = adapter.getItem(0).getTitle();
		
		final TextView returned;
		
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				returned = (TextView) adapter.getView(0, claimlistItem, claimlist);
				returned.performLongClick();
				b_editclaim.performClick();
				returned.setText(rename);
				b_saveedit.performClick();
			}
		});
				
		instrumentation.waitForIdleSync();

		// check if the first item is edited.
		assertTrue( adapter.getItem(0).getTitle().equals(rename) );
		
	}
	
	private void addClaim(final String name){
		final Button b_creatclaim = (Button) findViewById(R.id.b_creatclaim);
		final EditText et_claimTitle = (EditText) findViewById(R.id.et_claimTitle);
		final Button b_saveclaim = (Button) findViewById(R.id.b_saveclaim);
		instrumentation.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				b_creatclaim.performClick();
				et_claimTitle.setText( name );
				b_saveclaim.performClick();
			}
			
		});
		
		instrumentation.waitForIdleSync();
	}
	
	
	
	
	public void testCreateTag(){
		
		clickTab(MainActivity.INDEX_OF_TAGS_TAB);
		
		View newTag = createTagAndReturnTagTextView();
		
		assertTrue(((EditText) newTag.findViewById(R.id.tag_text)).getText().equals("Business"));
		
		deleteTag(newTag);
	}


	public void testRenameTag(){
		
		View newTag = createTagAndReturnTagTextView();
		newTag.performLongClick();
		clickButton(R.id.rename_tag_button);
		setText(R.id.rename_tag_text_view, "Pleasure");
		clickButton(R.id.confirm_rename_tag_button);
		
		assertTrue(((EditText) newTag.findViewById(R.id.tag_text)).getText().equals("Pleasure"));
		
		deleteTag(newTag);
	}
	
	public void testDeleteTag(){
		
		int beforeCreatingTagCount = getTagArrayAdapter().getCount();
		View newTag = createTagAndReturnTagTextView();
		
		assertEquals("Can't do delete tag test, since create tag failed.", beforeCreatingTagCount + 1, getTagArrayAdapter().getCount());
		
		deleteTag(newTag);
		
		assertEquals("Delete tag did not work.", beforeCreatingTagCount, getTagArrayAdapter().getCount());
	}

	
	//not really complete, since it assumes claims already exist.
	public void testFilterTag(){
		View businessTag = createTagAndReturnTagTextView();
		View pleasureTag = createTagAndReturnTagTextView();
		
		businessTag.findViewById(R.id.tag_checkbox).performClick();
		
		clickTab(MainActivity.INDEX_OF_MY_CLAIMS_TAB);
		
		
		//FIXME Not really sure how to make these claims. This probably doesn't work:
		Claim businessTrip = new Claim("Business Trip");
		Claim pleasureCruise = new Claim("Pleasure Cruise");
		
		businessTrip.addTag(businessTag);
		businessTrip.addTag(pleasureTag);
		
		Controller.addClaim(businessTrip);
		Controller.addClaim(pleasureCruise);

		//check if only one claim is displayed:
		ListAdapter adapter = ((ListView) getActivity().findViewById(R.id.claims_list_view)).getAdapter();
		assertEquals("Not right amount of claims displayed after filtering.", adapter.getCount(), 1);

		
		assertTrue("The right claims didn't filter.", 
				((Claim) adapter.getItem(0)).getName().equals("Business Trip"));
		
		clickTab(MainActivity.INDEX_OF_TAGS_TAB);
		
		deleteTag(businessTag);
	}
	
/*
	/*
	 * Takes the id of a text view and sets the text to a string.
	 */ 
/*	private void setText(int id, String text) {
		((EditText) getActivity().findViewById(id)).setText(text);
	}
	
	/*
	 * Takes the id of a button view and clicks on it.
	 */ 
/*	private void clickButton(int buttonId) {
		getActivity().findViewById(buttonId).performClick();
	}
	
	
	private View createTagAndReturnTagTextView() {
		
		clickTab(MainActivity.INDEX_OF_TAGS_TAB);
		
		int newItemPosition = getTagArrayAdapter().getCount();
		
		clickButton(R.id.create_tag_button);
		setText(R.id.create_tag_window, "Business");
		clickButton(R.id.confirm_create_tag_button);
		return (View) getTagArrayAdapter().getItem(newItemPosition);
	}


	
	private void deleteTag(View tag) {

		tag.performLongClick();
		clickButton(R.id.delete_tag_button);
		
		//maybe a confirm window?
		//clickButton(R.id.confirm_delete_tag_button);
	}

	private ListAdapter getTagArrayAdapter() {
		return ((ListView) getActivity().findViewById(R.id.tag_list_view)).getAdapter();
	}

	
	private void clickTab(int index){
		getActivity().getActionBar().getTabAt(index).select();
	}
}*/
