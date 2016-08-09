package org.stringmatching.determinist.aho_corasick;


import org.stringmatching.matcher.Matcher;
import org.stringmatching.matcher.MatcherResults;

/**
 * Aho-Corasick matcher method
 * 
 * @author beetecu
 *
 */

public class AC_Matcher extends Matcher{


	/**
	 * Fail Transitions
	 */

	private static final int FAIL = -1;


	/**
	 * Root Transitions
	 */
	private static final int ROOT = 0;




	private static  AC_Pattern _ac_Pattern ;


	/**
	 * Construtor
	 * 
	 * @param ac_Pattern the Aho-Corasick pattern model
	 * 
	 * @throws Exception 
	 * 
	 * 
	 */
	public AC_Matcher(final AC_Pattern ac_Pattern) throws Exception {


		if (ac_Pattern == null)

			throw new IllegalArgumentException("Error, Pattern model is null"); 

			_ac_Pattern = ac_Pattern;

			this.setMacherResults(_ac_Pattern.getPatterns());
		

	}

	@Override
	public void doMach(final byte[] stream, long start_position) throws Exception {


		//check the patterns, the patterns model and the matcj result class

		try{
			//System.out.println("doMach AC " + new String (stream));
			_macherResults.update(matcher(stream, start_position, this._ac_Pattern._acDictionary));
			
		}catch(Exception e){
			
			this._macherResults = null;
			throw e;
		}
		

	}

	/**
	 * 
	 * 
	 * @param stream
	 * @param startPosition
	 * @param ac_trie
	 * @return The matching results, null in case of error
	 * @throws Exception 
	 */
	private MatcherResults matcher(final byte[] stream, final long startPosition,  final AC_Trie ac_trie) throws Exception
	{
		int current_state = 0;

		byte indexInMap = 0;
		int temp_transition = FAIL;

		MatcherResults macherResults = null;

		try {
			macherResults = new  MatcherResults(_ac_Pattern.getPatterns());

			long lastIndexMatched = -1;

			for (int i=0;  i < stream.length; i++) { 

				byte item = stream[i];

				indexInMap = ac_trie.item_map[item];

				if (indexInMap > 0){

					//check for a transition
					temp_transition = ac_trie._trie.get(current_state).getNextState(indexInMap-1);

					while (temp_transition == FAIL){

						//goto suffix
						current_state = ac_trie._trie.get(current_state).getSuffix();
						temp_transition = ac_trie._trie.get(current_state).getNextState(indexInMap-1);

					}


					current_state = temp_transition;

					if ( ac_trie._trie.get(current_state).isTerminal()){

						//update the match results
						for (int j = 0; j < ac_trie._trie.get(current_state).getOutputs().size(); ++j) {

							int pIndex = ac_trie._trie.get(current_state).getOutputs().get(j);
							byte[] pattern = _ac_Pattern.getPatterns().get(pIndex);

							lastIndexMatched = startPosition + (i - pattern.length + 1) ;

							//System.out.println( " AC matchet at: " + (lastIndexMatched));
							
							macherResults.update(pIndex, lastIndexMatched);

							lastIndexMatched = -1;

						}

					}

				}
				else
				{
					//everybody pointing to the root
					lastIndexMatched = -1;
					current_state = ROOT;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	
			return null;
		}


		return macherResults;

	}


}

