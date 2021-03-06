package group5.trackerexpress;

import com.google.android.gms.maps.model.LatLng;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Allows the user to create an account.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class CreateAccountActivity extends AccountFormActivity {
	
	/**
	 * Constants for create account result.
	 */
	public static final int ACCOUNT_SUCCESS = 0;
	
	/** The Constant EMAIL_TAKEN. */
	public static final int EMAIL_TAKEN = 1;
	
	/** The Constant NETWORK_ERROR. */
	public static final int NETWORK_ERROR = 2;
	
	/** The authorizing task. */
	private UserAccountTask mAuthTask = null;
	// UI references.
	/** The email text field. */
	private EditText mEmailView;
	
	/** The password text field. */
	private EditText mPasswordView;
	
	/** The confirmation password text field. */
	private EditText mConfirmPasswordView;
	
	/** The name text view. */
	private EditText mNameView;
	
	/**
	 * OnCreate initializes all the views and their hints
	 * 		and also the button onClickListeners that are 
	 * 		necessary when creating an account
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		// retrieves the email and password text views
		mEmailView = (EditText) findViewById(R.id.createAccountEmailText);
		mPasswordView = (EditText) findViewById(R.id.createAccountPasswordText);
		
		// http://stackoverflow.com/questions/5203477/android-edittext-hint 11/03/15
		// Makes parts of the hint text different sizes
		String[] hintText = getString(R.string.prompt_create_account_password).split(" ", 2);
		mPasswordView.setHint(Html.fromHtml("<font size=\"16\">" + hintText[0] 
				+ " " + "</font>" + "<small>" + hintText[1] + "</small>" ));
		
		// retrieves the confirmation password and name text views
		mConfirmPasswordView = (EditText) findViewById(R.id.createAccountConfirmPasswordText);
		mNameView = (EditText) findViewById(R.id.createAccountNameText);
		
		// The create account button will attempt to create an account when clicked
		Button mConfirmCreateAccountButton = (Button) findViewById(R.id.confirmCreateAccountButton);
		mConfirmCreateAccountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptCreateAccount();
			}
		});
		
		// the cancel create account button will just end the activity
		Button mCancelCreateAccountButton = (Button) findViewById(R.id.cancelCreateAccountButton);
		mCancelCreateAccountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
		    	startActivity(intent);
		    	finish();
			}
		});

		// These are used in showProgress()
		setFormView(findViewById(R.id.email_create_account_form));
		setProgressView(findViewById(R.id.create_account_progress));
	}
	
	/**
	 * Attempts to create the account specified by the form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptCreateAccount() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mConfirmPasswordView.setError(null);

		// Store values at the time of the create account attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		String confirmPassword = mConfirmPasswordView.getText().toString();
		String name = mNameView.getText().toString();
		
		// Check that the user has entered a name
		if (TextUtils.isEmpty(name)) {
			setError(mNameView, R.string.error_field_required);
			return;
		} else if (name.split(" ").length < 2) {
			setError(mNameView, R.string.error_invalid_name);
			return;
		}
		
		if (emailErrors(email, mEmailView) || 
				passwordErrors(password, mPasswordView)) {
			return;
		}
				
		// Check that the password fields match.
		if (TextUtils.isEmpty(confirmPassword) || 
				!TextUtils.equals(password, confirmPassword)) {
			setError(mConfirmPasswordView, R.string.error_mismatched_passwords);
			return;
		}

		// Show a progress spinner, and kick off a background task to
		// perform the user create account attempt.
		showProgress(true);
		mAuthTask = new UserAccountTask(email, password, name);
		mAuthTask.execute((Void) null);
	}
	
	/**
	 * Represents an asynchronous login task used to authenticate
	 * the user.
	 */
	public class UserAccountTask extends AsyncTask<Void, Void, Integer> {

		/** The m email. */
		private final String mEmail;
		
		/** The m password. */
		private final String mPassword;
		
		/** The m name. */
		private final String mName;

		/**
		 * Instantiates a new user account task.
		 *
		 * @param email the email
		 * @param password the password
		 * @param name the name
		 */
		UserAccountTask(String email, String password, String name) {
			mEmail = email;
			mPassword = password;
			mName = name;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Integer doInBackground(Void... params) {

//			String[] credentials = new EmailElasticSearchEngine().getCredentials();
			String[] credentials = new String[0];

			for (String credential : credentials) {
				String[] pieces = credential.split(":");
				
				// Account already exists
				if (pieces[0].equals(mEmail)) {
					return EMAIL_TAKEN;
				}
			}

//			new EmailElasticSearchEngine().addCredential(mEmail + ":" + mPassword);
			

			User user = Controller.getUser(CreateAccountActivity.this);
			user.setName(CreateAccountActivity.this, mName);
			user.setEmail(CreateAccountActivity.this, mEmail);
			user.setPassword(CreateAccountActivity.this, mPassword);
			
			return ACCOUNT_SUCCESS;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(final Integer success) {
			mAuthTask = null;

			// Had to do if/else block because switch statements wouldn't break
			// properly with finish()
			if (success == ACCOUNT_SUCCESS) {
				User user = Controller.getUser(CreateAccountActivity.this);
				user.setSignedIn(CreateAccountActivity.this, true);
				mapPopUp();
			} else if (success == EMAIL_TAKEN) {
				showProgress(false);
				setError(mEmailView, R.string.error_email_taken);
			} else if (success == NETWORK_ERROR) {
				showProgress(false);
				Toast.makeText(CreateAccountActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
			} else {
				// Nothing should bring the user here, but just in case
				showProgress(false);
				Toast.makeText(CreateAccountActivity.this, "Error", Toast.LENGTH_SHORT).show();
			}
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	private void mapPopUp() {
		AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
		builder.setTitle("Home Location");
		builder.setMessage("Would you like to add a location to your profile?");
		
		builder.setPositiveButton("Add location", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(CreateAccountActivity.this, MapActivity.class);
				startActivityForResult(intent, 1);
			}
			
		});
		
		builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
		    	startActivity(intent);
				finish();
			}
			
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 1) {
	        if (resultCode == RESULT_OK){
	            LatLng latLng = data.getParcelableExtra("resultLatLng");
	            String title = data.getStringExtra("resultTitle");
	            
	            System.out.println("GOT " + title);
	            System.out.println("GOT" + latLng.latitude + " " + latLng.longitude);
	            
	            Location location = new Location(title);
	            
	            location.setProvider(title);
	            location.setLatitude(latLng.latitude);
	            location.setLongitude(latLng.longitude);

				User user = Controller.getUser(CreateAccountActivity.this);
	            user.setLocation(location);
	            
	        } else if (resultCode == RESULT_CANCELED) {
	        	// Do nothing
	        }

			Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
	    	startActivity(intent);
			finish();
	    }
	}

}
