package org.stringmatching.determinist.aho_corasick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Trie Model for the Aho-Corasick method
 * The trie is represented by a two dimensional array
 * where the rows are the states, the columns are
 * the pattern characters (items) and the cell value
 * index of the next state (transition)
 * 
 * Every Character in the patterns is mapped to reduce memory size
 * (number of columns in the trie)
 * but keeping a fast lookup (just an array access)
 * 
 * Could be interesting to load a dictionary of characters (ASCII) related to 
 * the input strings
 */

public final class AC_Trie implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Flag to determinate is the dictionary is ready to be used
	 */
	public transient boolean isReady;


	/**
	 *  Patterns item map.
	 *  Mapping the items in one pattern reduce the number 
	 *  columms in the AC trie representation
	 */
	public static byte[] item_map = new byte[256];




	/**
	 * Number of items for a set of patterns
	 * (Number of columns in the AC_trie)
	 */
	private int _numberOfItems = 0; 



	/**
	 * The AC trie
	 */
	public  List<State> _trie = new ArrayList<State>();


	public AC_Trie(){
		isReady = false;
	}

	/**
	 *  Search for a set the patterns their different characters and assign
	 * a new index to each one. 
	 * The number of different characters is the size of the AC_trie columns
	 * 
	 */
	private void buildMap(final List<byte[]> patterns){

		try{


			for (int i=0; i< 256; i++)
				item_map[i]= 0;

			byte ind = 1; //new item index starting from 1

			for (byte[] pattern : patterns) {

				for (byte item: pattern) {

					if (item_map[item] == 0){
						item_map[item] = ind;
						//System.out.println("item:" + item + " indexinMap:"  + ind);
						ind ++;
					}
				}
			}
			_numberOfItems = ind - 1;

		}catch(Exception e){

			System.out.println("Error building the map");
			item_map = null;
		}


	}



	/**
	 * Check if the item have been mapped
	 * 
	 * @param item 
	 * @return True if the item is in the map
	 * 
	 * 
	 */
	public boolean isInMap(byte item){

		boolean ret = true;

		if (item_map[item] == 0x00f )
			ret = false;


		return ret;
	}


	/**
	 * Build the AC trie
	 * 
	 * @param patterns List of patterns
	 * 
	 */
	public void build(final List<byte[]> patterns) {

		try{
			isReady = false;

			this.buildMap(patterns);

			//root node
			_trie.add(new State(this._numberOfItems));

			int current_state = 0;
			int next_state = 0;
			int previous_state = 0;

			int indexInMap = 0;
			int temp_transition = -1;

			//pre process trie
			for (int i=0; i<patterns.size(); i++){
				//everybody pointing to the root
				current_state = 0;
				previous_state = 0;
				next_state = 0;

				for (byte item: patterns.get(i)) {

					indexInMap = item_map[item] -1;

					temp_transition = _trie.get(current_state).getNextState(indexInMap);

					//if new node
					if (temp_transition == -1){

						if (previous_state > 0){

							current_state = previous_state;

							previous_state = 0;
						}


						next_state = _trie.size() ;

						_trie.get(current_state).setNextState(indexInMap, next_state);

						_trie.add(new State(this._numberOfItems));

						current_state = next_state;

					}
					else{

						previous_state = current_state;
						current_state = _trie.get(current_state).getNextState(indexInMap);

					}

				}
				_trie.get(current_state).setTerminalPattern(i);
			}

			// Set the suffix references
			Queue<Integer> q = new LinkedList<Integer>();
			int i = 0;
			int s = -1;

			for (i=0; i< this._numberOfItems; i++){

				// All nodes s of depth 1 have f[s] = 0
				s= _trie.get(0).getNextState(i);

				if ( (s != -1)) {
					_trie.get(s).setSuffix(0);
					q.add(s);
				}
				else
				{
					_trie.get(0).setNextState(i, 0);
				}
			}

			while(!q.isEmpty()){

				// Remove the front state from queue
				int state = q.peek();
				q.poll();

				// For the removed state, find failure function for
				// all those characters for which goto function is
				// not defined.
				for (i=0; i< this._numberOfItems; i++){


					if ( _trie.get(state).getNextState(i) != -1) {

						// Find failure state of removed state
						int failure =  _trie.get(state).getSuffix();

						// Find the deepest node labeled by proper
						// suffix of string from root to current
						// state.
						while (_trie.get(failure).getNextState(i) == -1)
							failure = _trie.get(failure).getSuffix();

						failure = _trie.get(failure).getNextState(i);


						_trie.get(_trie.get(state).getNextState(i)).setSuffix(failure);

						// add new patterns to this terminal node
						_trie.get(state).setTerminalPattern(_trie.get(failure).getOutputs());

						// Insert the next level node (of Trie) in Queue
						q.add(_trie.get(state).getNextState(i));

					}

				}

			}

			isReady = true;

		}catch(Exception e){

			_trie = null;

		}
	}



}
