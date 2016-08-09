package org.stringmatching.pattern;

import java.io.IOException;
import org.stringmatching.matcher.Matcher;

/**
 * This interface define a set of basic functions
 * for patterns model in order to 
 * perform Multi-Pattern String Matching
 * 
 *
 * @author beetecu
 */
public interface IPatternModel {

		
	
	/**
	 * Save the patterns to filepath
	 * 
	 * @param filepath
	 * @throws IOException 
	 * @throws Exception 
	 */
	public void savePatterns(String filepath) throws IOException, Exception;
	
	
	/**
	 * Load the patterns model from a file
	 * 
	 * @param filepath 
	 * @throws IOException 
	 */
	public void loadPatterns(String filepath) throws IOException;
	
	
	
    /**
     *  Creates a matcher that will match the given input against this pattern.
	 * 
     * @return  A new matcher for this pattern model
     *          null in case of error
     * @throws Exception 
     *
     */
	public Matcher matcher() throws Exception;
	
	 
}
