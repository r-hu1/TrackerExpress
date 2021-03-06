/**
 * 
 */
package group5.trackerexpress;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

/**
 * Performs elastic search operations needed by this app.
 * 
 * 
 * @author crinklaw
 *
 */
public class ElasticSearchEngine {

	
	private final ElasticSearchEngineUnthreaded elasicSearchEngineUnthreaded = new ElasticSearchEngineUnthreaded();
	
	
	
	private UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {
		
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			threadException = ex;
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	volatile Throwable threadException;
	
	public Claim[] getClaims(Context context) throws IOException {
		
		if (!Controller.isInternetConnected(context))
			throw new IOException();
		
		final Claim[][] claims = new Claim[1][];

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				claims[0] = elasicSearchEngineUnthreaded.getClaims();
			}
		});

		
		threadException = null;
		
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new IOException();
		}
		
		if (threadException != null){
			throw new IOException();
		}
		
		return claims[0];
	}
	
	/**
	 * Gets the claims that user can approve, i.e. claims that will appear in Global claims list. 
	 * As of now, that means claims that have no attached approver besides the current user,
	 * were not submitted by the current user, and are not in returned or approved state.
	 * @return filtered claims list
	 */
	public Claim[] getClaimsForGlobalClaimList(Context context) throws IOException {
		
		if (!Controller.isInternetConnected(context))
			throw new IOException();
		
		Claim[] claimsUnfiltered = getClaims(context);
		List<Claim> claims = new ArrayList<Claim>();
		Log.e("USER", Controller.getUser(context).getEmail().toString());
		for (Claim claim : claimsUnfiltered){
			if (    !claim.getSubmitterEmail().equals(Controller.getUser(context).getEmail()) &&
					(claim.getApproverEmail() == null || claim.getApproverEmail().equals(Controller.getUser(context).getEmail())) &&
					 claim.getStatus() != Claim.IN_PROGRESS){
				
				claims.add(claim);
			}
		}
		
		return claims.toArray(new Claim[claims.size()]);
	}
	
	
	/**
	 * Get claim from Elastic Search server
	 * 
	 * @param claimUUID: desired claim's uuid
	 * @return claim if it is in server, else will return null
	 */
	public Claim getClaim(Context context, UUID id) throws IOException {
		
		if (!Controller.isInternetConnected(context))
			throw new IOException();
		
		final Claim[] claim = new Claim[1];
		final UUID idFinal = id;

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					claim[0] = elasicSearchEngineUnthreaded.getClaim(idFinal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
		return claim[0];
	}	




	public void submitClaim(Context context, Claim claim) throws IOException {
		
		if (!Controller.isInternetConnected(context))
			throw new IOException();
		
		claim.setSubmitterName(context, Controller.getUser(context).getName());
		claim.setSubmitterEmail(context, Controller.getUser(context).getEmail());
		
		//convert UriBitmaps to actual bitmaps
		for (Expense expense : claim.getExpenseList().toList()){

			try {
				expense.getReceipt().switchToStoringActualBitmap();
			} catch (NullPointerException e) {}

		}
		final Claim claimFinal = claim;
		
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.submitClaim(claimFinal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
		
		//switch back for saving:
		for (Expense expense : claim.getExpenseList().toList()){
			try {
				expense.getReceipt().stopStoringActualBitmap();
			} catch (NullPointerException e) {}
		}		

	}

	// Warning! This deletes all user claims
	public void deleteClaims() {
		Claim[] claims = elasicSearchEngineUnthreaded.getClaims();
		
		for (Claim claim : claims) {
			elasicSearchEngineUnthreaded.deleteClaim(claim.getUuid());
		}
	}


	public void deleteClaim(Context context, UUID id) throws IOException {
		final UUID idFinal = id;
		
		if (!Controller.isInternetConnected(context))
			throw new IOException();

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.deleteClaim(idFinal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}



	public void reviewClaim(Context context, UUID id, String comments, final int status) throws IOException {
		
		if (!Controller.isInternetConnected(context))
			throw new IOException();
		
		final UUID idFinal = id;
		final String commentsFinal = comments;
		final String approverName = Controller.getUser(context).getName();
		final String approverEmail = Controller.getUser(context).getEmail();

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.reviewClaim(idFinal, commentsFinal, approverName, approverEmail, status);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		/*try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}*/
	}


	/*public void returnClaim(Context context, UUID id, String comments) throws IOException {
		
		if (!Controller.isInternetConnected(context))
			throw new IOException();
		
		final UUID idFinal = id;
		final String commentsFinal = comments;
		final String approverName = Controller.getUser(context).getName();
		final String approverEmail = Controller.getUser(context).getEmail();

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.returnClaim(idFinal, commentsFinal, approverName, approverEmail);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
	}*/


}
