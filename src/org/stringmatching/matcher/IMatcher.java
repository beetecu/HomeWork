package org.stringmatching.matcher;

/**
 * This interface define the basic
 * function for a Matcher algorithm
 * <p> 
 * For an input text the doMatch function
 * must to parse the text (stream) in order to find 
 * all the positions in the text for a pattern/s
 * To allow concurrence by splitting the text in segments,
 * the start position is used to set correctly
 * the pattern position in the full text.
 *
 * @author beetecu
 */
public interface IMatcher {

	/**
	 * Engine to develop the pattern matching
	 * 
	 * @param stream The text or segment to be processed
	 * @param start_position In case of test segmentation, the segment offset
	 * @throws Exception 
	 * 
	 */
	public void doMach(byte[] stream, long start_position) throws Exception;
	
	
	
}
