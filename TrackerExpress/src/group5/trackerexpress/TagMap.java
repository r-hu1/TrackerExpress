package group5.trackerexpress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import android.content.Context;

/**
 * Holds all the tags of the system, and handles file IO,
 * loading the TagMap upon construction.
 * @author Peter Crinklaw, Randy Hu, Parash Rahman, Jesse Emery, Sean Baergen, Rishi Barnwal
 * @version Part 4
 * @see Tag
 */
public class TagMap extends TModel{
	
	/**
	 * generate serial number for Serializable type
	 */
	private static final long serialVersionUID = 1L;

	/** The Constant FILENAME. */
	private static final String FILENAME = "tags.sav";
	
	/** The tags. */
	private Map<UUID, Tag> tags;

	/**
	 * Instantiates a new tag map.
	 *
	 * @param context Needed for file IO
	 */
	public TagMap(Context context){
		super();
		tags = new HashMap<UUID, Tag>();
		loadData(context);
	}

	/**
	 * Save data.
	 *
	 * @param context Needed for file IO
	 */
	public void saveData(Context context) {
		try {
			new FileCourrier<TagMap>(this).saveFile(context, FILENAME, this);
		} catch (IOException e) {
			System.err.println ("Could not save tags.");
			throw new RuntimeException();
		}
	}

	/**
	 * Load data.
	 *
	 * @param context Needed for file IO
	 */
	public void loadData(Context context) {
		try {
			TagMap savedTagMap = new FileCourrier<TagMap>(this).loadFile(context, FILENAME);
			if (savedTagMap.tags == null)
				throw new FileNotFoundException();
			this.tags = savedTagMap.tags;
		} catch (FileNotFoundException e) {
			System.err.println ("Tags file not found, making a fresh tags list.");
			this.tags = new HashMap<UUID, Tag>();
		} catch (IOException e){
			throw new RuntimeException();
		}
	}
	
	
	/** Checks if the tag exist 
	 * 
	 * @param id of the tag
	 * @return boolean of whether the tag is in the TagMap
	 */
	public boolean contains(UUID id){
		return tags.containsKey(id);			
	}
	
	/**
	 * Gets the tag.
	 *
	 * @param id of the id
	 * @return the tag
	 */
	public Tag getTag(UUID id){
		return tags.get(id);
	}

	/**
	 * Clears tags map.
	 *
	 * @param context Needed for file IO
	 */
	public void clear(Context context){
		tags.clear();
		notifyViews(context);
	}

	/**
	 * Checks if tag map is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return tags.isEmpty();
	}

	/**
	 * Adds the tag to the map.
	 *
	 * @param context Needed for file IO
	 * @param tag the tag
	 */
	public void addTag(Context context, Tag tag) {
		tags.put(tag.getUuid(), tag);
		makeSureViewsIsntNull();
		tag.addViews(this.views);
		notifyViews(context);
	}

	/**
	 * Delete tag from map.
	 *
	 * @param context Needed for file IO
	 * @param id the id
	 */
	public void deleteTag(Context context, UUID id) {
		tags.remove(id);
		notifyViews(context);
	}

	/**
	 * Return the size of the tags map
	 *
	 * @return size the size of the tags map
	 */
	public int size() {
		return tags.size();
	}

	/**
	 * Gets the tags in an arraylist.
	 *
	 * @return the tags as an array list
	 */
	public ArrayList<Tag> toList() {
		return new ArrayList<Tag>(tags.values());
	}
	
	/**
	 * adds view to be updated
	 * @param view TView to be updated
	 */
	@Override
	public void addView(TView view){
		super.addView(view);
		Iterator<Entry<UUID, Tag>> it = tags.entrySet().iterator();
		while (it.hasNext()) {
			it.next().getValue().addView(view);
		}
	}

	// Makes an array list of tag strings not including the tag strings passed in
	public ArrayList<String> getTagStrings(ArrayList<Tag> tagList) {
		
		ArrayList<String> tagStrings = new ArrayList<String>();
		for (Tag tag : tagList) {
			tagStrings.add(tag.toString());
		}
		
		ArrayList<String> newTagStrings = new ArrayList<String>();
		
		Iterator<Entry<UUID, Tag>> it = tags.entrySet().iterator();
		String tag;
		while (it.hasNext()) {
			tag = it.next().getValue().toString();
			if (tagStrings.contains(tag)) {
				tagStrings.remove(tag);
			} else {
				newTagStrings.add(tag);
			}
		}
		
		return newTagStrings;
	}
	
	/**
	 * Searches for a tag by string value
	 * 
	 * @param string
	 * @return
	 * @throws IllegalAccessException 
	 */
	public Tag searchForTagByString(String string) throws IllegalAccessException {
		Iterator<Entry<UUID, Tag>> it = tags.entrySet().iterator();
		Tag tag;
		while (it.hasNext()){
			tag = it.next().getValue();
			if (tag.toString().equals(string))
				return tag;
		}
		throw new IllegalAccessException("Tried to search for a tag that does not exist.");
	}
}
