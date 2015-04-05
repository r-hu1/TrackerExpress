package group5.trackerexpress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * TModel is the parent of all models in our app. It allows models to
 * have a list of views and notify them.
 * 
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 */
public class TModel implements Serializable{
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The views. Transient because these are not be saved when models are serialized.*/
	protected transient List<TView> views;

    /**
     * Instantiates a new t model.
     */
    public TModel() {
    	views = new ArrayList<TView>();
    }
    
    /**
     * Make sure views isnt null.
     */
    protected void makeSureViewsIsntNull(){
    	if (views == null)
    		views = new ArrayList<TView>();
    }

    /**
     * Adds the view.
     *
     * @param view the view
     */
    public void addView(TView view) {
    	makeSureViewsIsntNull();
        if (!views.contains(view)) {
            views.add(view);	
        }
    }
    
    /**
     * Adds the views.
     *
     * @param views the views
     */
    public void addViews(List<TView> views){
    	makeSureViewsIsntNull();
    	for (TView view : views){
    		addView(view);
    	}
    }

    /**
     * Delete view. 
     * 
     * NOTE: Not implemented correctly. Implementing correctly would mean many child classes would have to override
     * this function. Until we see the need to do that, I'm commenting this out - Peter
     *
     * @param view the view
     */
    /*
    public void deleteView(TView view) {
    	makeSureViewsIsntNull();
        views.remove(view);
    }
    */
    
    /**
     * Notify views.
     *
     * @param context Needed for file IO
     */
    public void notifyViews(Context context) {
    	makeSureViewsIsntNull();

        for (TView view : views) {
        	view.update(this);
        }

        if (context != null){
        	Controller.getTagMap(context).saveData(context);
        	Controller.getClaimList(context).saveData(context);	
        	Controller.getUser(context).saveData(context);
        }
    }
    
    /*
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.views = new ArrayList<TView>();
    }
    */
}