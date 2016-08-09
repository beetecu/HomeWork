package org.stringmatching.determinist.aho_corasick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Each node represent a state in the Aho-Corasick Trie
 * For each different character in the pattern word (referred as item)
 * is stored the index of the next state (transition) 
 */
public final class State implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * For a fail transition
	 * 
	 */
	private static final int FAIL = -1;
	
	/**
	 * For a fail transition
	 * 
	 */
	private static final int ROOT = 0;

	
	
	/**
	 * List of transtions from one state
	 * 
	 */
	final private int[] _transition; 
	
	
	/**
	 * suffix of the failure function for a state
	 * 
	 */
	private int _suffix;
	
	
	/**
	 * if the state (node) is a terminal node
	 * the output store the list of patterns 
	 * (in this case the pattern index from the patterns list) for
	 * related to this terminal node
	 */
	final private List<Integer> _output = new ArrayList<Integer>();
	
	
	/**
	 * Constructor
	 * @param items_length Number of different items in the pattern
	 * 
	 */
	public State( final int items_length) {
		
		
		if (items_length == 0)
			throw new IllegalArgumentException();
		else
		{
			_transition = new int[items_length];
			//all transitions are settled to FAIL
			//no transition between the item to other state
			for (int i=0; i< items_length; i++)
				_transition[i] = FAIL;
			this._suffix = ROOT;
			
		}
		

	}
	
	/**
	 * 
	 * @param item
	 * 
	 * @return  The next state (transition) for one item
	 */
	public int getNextState(int item) {
		
		int ret = ROOT;
		
		try{
			
			ret =  _transition[item];
			
		}catch(Exception e){
			System.out.println("item out of index:" + item);
			throw e;
		}
		
		return ret;
		
		
	}
	
	
	/**
	 *  Set the state transition for the item
	 * -
	 * @param item
	 * @param transition
	 */
	public void setNextState(int item, int transition) {
		
		 _transition[item] = transition;
		 
	}
	
	/**
	 *  Failure function suffix
	 *
	 * @param suffix (state) of the failure function  
	 * 
	 */
	public void setSuffix(final int suffix) {
		
		this._suffix = suffix;
	
	
	}
	
	
	/**
	 * Failure function, next state 
	 * 
	 * @return The suffix (state) from the failure function
	 * 
	 */
	public int getSuffix() {
		
		return _suffix;
	}
	
	/**
	 * Add a new pattern to the terminal state
	 * 
	 * 
	 * @param index The pattern index from the patterns list
	 * 
	 */
	public void setTerminalPattern(int index) {
		
		_output.add(index);
	}
	
	/**
	 * Add a list of pattern to the terminal state
	 * 
	 * @param indexs List of patterns indexs
	 * 
	 * 
	 */
	public void setTerminalPattern(List<Integer> indexs) {
		
		_output.addAll(indexs);
	}
	
	/**
	 * Check if the state node have some output pattern in their list
	 * 
	 * @return if the state is terminal
	 * 
	 */
	public boolean isTerminal() {
		
		boolean ret = true;
		
		if (_output.isEmpty())
			ret = false;
		
		return ret;
	}
	
	
	/**
	 * 
	 * @return List of pattern indexes for a terminal node
	 */
	public List<Integer> getOutputs(){
		
		return this._output;
	}
	
	
}