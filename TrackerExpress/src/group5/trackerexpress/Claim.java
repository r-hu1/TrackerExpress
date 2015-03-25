package group5.trackerexpress;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;


/**
 * The Class Claim.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class Claim extends TModel implements Comparable<Claim>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The name of the claim creator*/
	private String userName;
	
	/** The claim name. */
	private String claimName;
	
	/** The Description of the claim. */
	private String Description;
	
	/** The list of expenses for the claim. */
	private ExpenseList expenseList;
	
	/** The destinations visited in claim. */
	private ArrayList<String[]> destination = new ArrayList<String[]>();
	
	/** The status of the claim (in_progress, submitted, returned, or approved. */
	private int status; 
	
	/** The start date of the claim. */
	private Date startDate;
	
	/** The end date of the claim. */
	private Date endDate;
	
	/** The incomplete indicator. */
	private boolean incomplete;
	

	/** The ArrayList of tagIds.*/
	private ArrayList<UUID> tagIds;
	
    
	/** The Constant IN_PROGRESS. */
	public static final int IN_PROGRESS = 0;
	
	/** The Constant SUBMITTED. */
	public static final int SUBMITTED = 1;
	
	/** The Constant RETURNED. */
	public static final int RETURNED = 2;
	
	/** The Constant APPROVED. */
	public static final int APPROVED = 3;
	
	
	/** Creates a random id for the claim */
	private UUID uuid;
	
	
	/**
	 * Instantiates a new claim.
	 *
	 * @param claimName the claim name
	 */
	public Claim(String claimName) {
		// TODO Auto-generated constructor stub
		this.uuid = UUID.randomUUID();
		this.claimName = claimName;
		this.expenseList = new ExpenseList();
		this.status = IN_PROGRESS;
		this.incomplete = true;
		this.tagIds = new ArrayList<UUID>();
	}
	
	/**
	 * Checks if is incomplete.
	 *
	 * @return true, if is incomplete
	 */
	public boolean isIncomplete() {
		return incomplete;
	}

	/**
	 * Sets the incomplete.
	 *
	 * @param context Needed for file IO
	 * @param incomplete the incompleteness indicator
	 */
	public void setIncomplete(Context context, boolean incomplete) {
		this.incomplete = incomplete;
		notifyViews(context);
	}

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}
	
	/**
	 * Sets the uuid.
	 *
	 * @param context Needed for file IO
	 * @param uuid the uuid
	 */
	public void setUuid(Context context, UUID uuid) {
		this.uuid = uuid;
		notifyViews(context);
	}	
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getuserName(){
		return userName;
	}
	
	/**
	 * Set User name.
	 *
	 * @param context Needed for file IO
	 * @param userName the user name
	 */
	public void setuserName(Context context, String userName){
		this.userName = userName;
		notifyViews(context);
	}
	
	/**
	 * Gets the claim name.
	 *
	 * @return the claim name
	 */
	public String getClaimName() {
		return claimName;
	}

	/**
	 * Sets the claim name.
	 *
	 * @param context Needed for file IO
	 * @param claimName the claim name
	 */
	public void setClaimName(Context context, String claimName) {
		this.claimName = claimName;
		notifyViews(context);
	}
	


	/**
	 * Gets the expense list.
	 *
	 * @return the expense list
	 */
	public ExpenseList getExpenseList() {
		return expenseList;
	}
	
	
	/**
	 * Sets the expense list.
	 *
	 * @param context Needed for file IO
	 * @param expenseList the expense list
	 */
	public void setExpenseList(Context context, ExpenseList expenseList) {
		this.expenseList = expenseList;
		notifyViews(context);
	}
	


	/**
	 * Sets the start date.
	 *
	 * @param context Needed for file IO
	 * @param d1 the date to use as the start date
	 */
	public void setStartDate(Context context, Date d1) {
		// TODO Auto-generated method stub
		this.startDate = d1;
		notifyViews(context);
	}
	
	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public Date getStartDate() {
		return startDate;
	}	
	
	/**
	 * Sets the end date.
	 *
	 * @param context Needed for file IO
	 * @param d2 the date to use as the end date
	 */
	public void setEndDate(Context context, Date d2){
		this.endDate = d2;
		notifyViews(context);
	}
	
	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public Date getEndDate() {
		return endDate;
	}
		
	/**
	 * Adds the destination.
	 *
	 * @param context Needed for file IO
	 * @param place the location of destination
	 * @param Reason the reason for travel to destination
	 */
	public void addDestination(Context context, String place, String Reason){
		String[] travelInfo = new String[2];
		travelInfo[0] = place;
		travelInfo[1] = Reason;
		destination.add(travelInfo);
		notifyViews(context);
	}
	
	
	/**
	 * Sets the destination.
	 *
	 * @param context Needed for file IO
	 * @param destination the destination
	 */
	public void setDestination(Context context, ArrayList<String[]> destination) {
		this.destination = destination;
		notifyViews(context);
	}
	
	/**
	 * Creates string of all destinations.
	 *
	 * @return the string
	 */
	public String toStringDestinations(){
		// Get the destinations in a list format
		String str_destinations = "";
		for ( int i = 0; i < destination.size() - 1; i++ ){
			str_destinations += destination.get(i)[0] + ", ";
		}
		if (destination.size()>0)
			str_destinations += destination.get(destination.size() - 1)[0];
		return str_destinations;
	}
	
	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public ArrayList<String[]> getDestination() {
		return destination;
	}	
	
	/**
	 * Sets the description.
	 *
	 * @param context Needed for file IO
	 * @param Description the description
	 */
	public void setDescription(Context context, String Description){
		this.Description = Description;
		notifyViews(context);
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription(){
		return Description; 
	}
	

	/**
	 * Sets the status.
	 *
	 * @param context Needed for file IO
	 * @param status the status
	 */
	public void setStatus(Context context, int status) {
		this.status = status;
		notifyViews(context);
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/** 
	 * compares the claim start date with the instance's start date
	 * @param arg0 The Claim to be compared
	 * @return the result of the comparison as an int
	 */
	@Override
	public int compareTo(Claim arg0) {
		if (startDate == null){
			return -1;
		}
		return startDate.compareTo(arg0.getStartDate());
	}

	/**
	 * Gets the list of TagIds.
	 * 
	 * @return the list of tag Ids
	 */
	public ArrayList<UUID> getTagsIds() {
		return tagIds;
	}
	
	/**
	 * formats and gets a string list of the tags
	 * 
	 * @return a string of tags in list form
	 */
	public String toStringTags(Context context){
		String stringTags = "";
		TagMap tm = Controller.getTagMap(context);
		
		for ( int i = 0; i < tagIds.size() - 1; i++ ) {
			stringTags += tm.getTag(tagIds.get(i)).toString() + ", "; 
		}
		
		if ( tagIds.size() > 0 ){
			stringTags += tm.getTag(tagIds.get(tagIds.size() - 1)).toString();
		}
		
		return stringTags;
	}

	/**
	 * Sets the list of tagIds.
	 * 
	 * @param tagsIds the list of tagIds
	 */
	public void setTagsIds(ArrayList<UUID> tagsIds) {
		this.tagIds = tagsIds;
	}
	
	/**
	 * adds view to be updated
	 * @param view TView to be updated
	 */
	@Override
	public void addView(TView view){
		super.addView(view);
		expenseList.addView(view);
	}
	
}
