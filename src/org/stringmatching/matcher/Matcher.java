package org.stringmatching.matcher;

import java.util.List;

/**
 * Abstract class focused on to establish a 
 * bridge between the matching process and 
 * their outputs
 * 
 */
public abstract class Matcher implements IMatcher{

	/**
	 * Store the matching process results.
	 * For each pattern is stored a list of positions 
	 * over the input text/segment
	 * 
	 */
	protected MatcherResults _macherResults;

	/**
	 * Set up the matcher output results for new set of patterns
	 * 
	 * @return List of pattern results
	 * 
	 */
	public MatcherResults getMacherResults() {

		return _macherResults;
	}


	/**
	 * Set up the matcher output results for new set of patterns
	 * 
	 * @param patterns List of patterns
	 * @throws Exception 
	 * 
	 */
	protected void setMacherResults(List<byte[]> patterns) throws Exception {

		if (patterns.isEmpty())
			throw new Exception("Error, Empty list of pattens"); 
		
			_macherResults =  new  MatcherResults(patterns);
		
	}

	/**
	 * When is used the same patterns for different
	 * files it is necessary clear the previous results 
	 * 
	 */
	public void resetResults(){

		try{
			_macherResults.cleanPositions();
		}
		catch(Exception e){
			
			_macherResults = null;
		}

	}




}
