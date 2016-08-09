package org.stringmatching.matcher;


import java.util.ArrayList;
import java.util.List;

/**
 * Store for one pattern their list of matching positions in the input text
 * 
 * 
 */
public  class PatternResults {

	/**
	 * Pattern
	 * 
	 */
	byte [] _pattern;
	
	/**
	 * List of pattern positions in the input 
	 * 
	 */
	public List<Long> _positions;


	/**
	 * Constructor
	 * @param pattern 
	 * @throws Exception 
	 *  
	 * 
	 */
	public PatternResults(byte[] pattern) throws Exception{
		
		if (pattern.length == 0)
			throw new Exception("Pattern list is empty");
			
		_pattern = pattern;

		this._positions = new ArrayList<Long>();
		
	}


	/**
	 * Delete all pattern positions
	 * 
	 * 
	 */
	public void clean(){

		this._positions.clear();

	}

	/**
	 * Count the number of matches for the pattern
	 * 
	 * @return Number of matches
	 * 
	 */
	public int getNumberOfMatchs() {

		return this._positions.size();

	}

	/**
	 * Add a new position
	 * 
	 * @param pos New matching position
	 * 
	 * 
	 */
	public void addPosition(long pos){

		this._positions.add(pos);

	}


	/**
	 * 
	 * @return The pattern
	 * 
	 */
	public byte[] getPattern() {
		// TODO Auto-generated method stub
		return _pattern;
	}


	/*

	public String toJson(){
		JSONObject jsonObject= new JSONObject();
	    try {

	        jsonObject.put("pattern", new String(this._pattern));
	        jsonObject.put("counter", this.get_numberoOfMatchs());


	      //jsonObject.put("f_logo_source", this.getLogo());
	        //jsonObject.put("f_advertise", this.getAds());


	        return jsonObject.toString();
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return "";
	    }


	}
	 */



}
