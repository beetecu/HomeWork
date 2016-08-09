package org.stringmatching.heuristic.wu_manber;


import java.nio.ByteBuffer;
import org.stringmatching.matcher.Matcher;
import org.stringmatching.matcher.MatcherResults;


/**
 * Wu Manber matcher method
 * 
 * @author beetecu
 *
 */
public class WM_Matcher extends Matcher {

	
	private static WM_Pattern _wm_Pattern;

	/**
	 * Constructor
	 * 
	 * @param wm_Pattern
	 * @throws Exception
	 * @throws IllegalArgumentException
	 */
	public WM_Matcher(final WM_Pattern wm_Pattern) throws Exception, IllegalArgumentException {

		if (wm_Pattern == null)
			throw new IllegalArgumentException("Error, Pattern model is null"); 
		
			_wm_Pattern = wm_Pattern;
			this.setMacherResults(_wm_Pattern.getPatterns());		
	}

	@Override
	public void doMach(final byte[] stream, long start_position) throws Exception {
		// TODO Auto-generated method stub
 
		try{
			//System.out.println("doMach WM " + new String (stream));
			_macherResults.update(matcher(stream, start_position));
			
		}catch(Exception e){
			
			_macherResults = null;
			throw e;
		}

	}

	/**
	 * Find the matches 
	 * 
	 * @param stream Input text
	 * @param start_position Text offset 
	 * @return the matcher results
	 * @throws Exception
	 */
	private  MatcherResults matcher(final byte[] stream, final long start_position) throws Exception
	{

		MatcherResults macherResults = null;
		
		try{
			//System.out.println("Processing WM Token: " + new String (stream) );
			
			macherResults = new  MatcherResults(_wm_Pattern.getPatterns());
			
			int pos = _wm_Pattern.shortestPatternLength - 1;

			byte[] textBlock = new byte[]{};

			while (pos < stream.length) {

				//WM_Pattern.getBytes(stream,pos-this._wm_Pattern.blockSize+1, pos+1, textBlock, 0);

				textBlock =  new byte[pos+1 - (pos- _wm_Pattern.blockSize+1)];

				System.arraycopy(stream, pos-_wm_Pattern.blockSize+1, textBlock, 0, pos+1 - (pos-_wm_Pattern.blockSize+1));



				final int shiftValue = _wm_Pattern.shifts[_wm_Pattern.getBlockHash(textBlock)];

				

				if (shiftValue == 0) { // if there is no possible shift, the text to the left may be a match for a pattern.
					//verify Matches

					boolean flag = false;
					byte[] match;
					for (int index=0; index < _wm_Pattern.verifiers.get(ByteBuffer.wrap(textBlock)).size(); index++) {
						match  = _wm_Pattern.getPatterns().get(_wm_Pattern.verifiers.get(ByteBuffer.wrap(textBlock)).get(index));

						final int matchLength = match.length;
						final int matchStart = pos - matchLength + 1;
						flag = true;
						//text.regionMatches(matchStart, match, 0, matchLength)


						int current_pos = matchStart;
						int ind = 0;

						//System.out.println("TEXT: " + new String(text) +"  text.length: " + text.length + "TEXT: " + new String(match) + " match.length: " + match.length);

						while((flag) && (ind  < matchLength) && (current_pos < stream.length)){

							if (stream[current_pos] - match[ind] != 0 )
								flag = false;
							current_pos ++;
							ind++;
						}

						if ( flag) {

							//System.out.println(new String (stream) + " WM matchet at: " + start_position + matchStart);
							
							macherResults.update(_wm_Pattern.verifiers.get(ByteBuffer.wrap(textBlock)).get(index), start_position + matchStart);

						}
					}
					
					pos += 1;
				} else {
					pos += shiftValue;
				}
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			
			return null;
			
		}
		
		return macherResults;
	}


	



}
