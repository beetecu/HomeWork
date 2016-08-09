package org.stringmatching.hybrid.ahocorasick_wumanber;

/**
 * 
 * Represent the information for one token
 * 
 * 
 * @author beetecu
 *
 */
public final class Token{

	private byte[] _sequence;

	private long _position;

	/**
	 * @param sequence 
	 * @param pos 
	 * @throws Exception 
	 * 
	 */
	public Token(byte[] sequence, long pos) throws Exception{

		//if (sequence.length == 0)
			//throw new Exception("Toquen is empty");
		//else{
			_sequence = sequence;
			_position = pos;
		//}
	}

	/**
	 * Get Sequence
	 * 
	 * @return sequence
	 */
	public byte[] get_sequence() {
		return _sequence;
	}

	/**
	 * Get Token Position
	 * 
	 * @return the token position in the text
	 */
	public long get_position() {
		return _position;
	}

	



}