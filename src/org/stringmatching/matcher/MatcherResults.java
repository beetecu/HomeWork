package org.stringmatching.matcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The results related to the pattern matching process
 * Contain a list with the matching results for each pattern
 * 
 */
public  class MatcherResults {

	/**
	 * Store the pattern matching results
	 */
	public List<PatternResults> _results;


	/**
	 * Constructor
	 * 
	 * @param patterns 
	 * @throws Exception 
	 */
	public MatcherResults (List<byte[]> patterns) throws Exception{

		_results = new ArrayList<PatternResults>();
		
		for(int i = 0; i < patterns.size(); i++) {
			addPattern(patterns.get(i));
	    }

		

	}

	/**
	 * Constructor
	 * 
	 */
	public MatcherResults() {
		
		_results = new ArrayList<PatternResults>();
	}
	

	/**
	 * Add a pattern in the list 
	 * 
	 * @param pattern 
	 * @throws Exception 
	 */
	public void addPattern(byte[] pattern) throws Exception{

		_results.add(new PatternResults(pattern));
	}


	/**
	 * Update the positions for a pattern
	 * @param index 
	 * @param pos New position
	 */
	
	public void update(int index, long pos){


		try{
			_results.get(index).addPosition(pos);
			
		}catch(Exception e){
			System.out.println(" Error updating the results");
			e.printStackTrace();
		}

	}

	/**
	 * Remove all patterns and their results, 
	 * freedom memory
	 */
	public void clear(){

		this.cleanPositions();
		
		this._results.clear();

	}

	/*
	// return a Json with the matching results.
	public String getJson(){

		return null;
	}*/

	
	/**
	 * Send to the console the matching results
	 */
	public void printResults(){

		PatternResults tmpMP = null;
		String tmpOutput = "";

		int index = 0;

		try{
			
		
		Iterator<PatternResults> resultsIterator = this._results.iterator();
		while (resultsIterator.hasNext()) {

			tmpMP = resultsIterator.next();
			tmpOutput = "Pattern: " + new String(tmpMP.getPattern()) + "  ------- ";
			tmpOutput = tmpOutput + " Counter: " + tmpMP.getNumberOfMatchs() + "  ------- ";

			if (tmpMP._positions.size() > 0)
			{
				tmpOutput = tmpOutput + " At: [ ";

				Iterator<Long> locationsIterator = tmpMP._positions.iterator();
				while (locationsIterator.hasNext()) {

					tmpOutput = tmpOutput + " | " + locationsIterator.next();

				}
				tmpOutput = tmpOutput + " | ] ";


			}

			System.out.println(tmpOutput);
			index++;
			tmpMP.clean();
			
			

		}
		}catch(Exception e){
			
			System.out.println("Error, matcher results are corrupts");
			
		}
		System.out.println();
		
	}

	

	/**
	 * Free all pattern positions
	 */
	//for each pattern, clear the localizations list
	public void cleanPositions() {
		
		Iterator<PatternResults> resultsIterator = this._results.iterator();

		while (resultsIterator.hasNext())
		{
			resultsIterator.next()._positions.clear();
		}

	}

	/**
	 * Merging two MatcherResults
	 * 
	 * @param results
	 * @throws Exception 
	 */
	public void update(final MatcherResults results) throws Exception {
		
		if (results == null)
			
			throw new Exception(" The matcher result is null");
			
		int ind = 0;
		for (PatternResults p : results._results){
			this._results.get(ind)._positions.addAll(p._positions);
			ind++;
		}
	}



}
