package org.stringmatching.determinist.aho_corasick;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

import org.stringmatching.pattern.PatternModel;

/**
 * Build the the Pattern Model for the Aho-Corasick method.
 * 
 *
 * 
 */
public final class AC_Pattern  extends PatternModel implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static AC_Trie _acDictionary;

	/**
	 * Constructor 
	 * 
	 * @param patterns : Patterns List
	 * @throws Exception 
	 */
	public AC_Pattern(final List<byte[]> patterns) throws Exception{

		super(patterns);

		_acDictionary = new AC_Trie();

		buildModel();


	}


	/**
	 * Constructor using a stored set of patterns
	 * 
	 * @param patternfilepath
	 */
	public AC_Pattern(String patternfilepath){

		super();

		this.loadPatterns(patternfilepath);

	}




	/**
	 * Creates a matcher that will match the given input against this pattern.
	 *
	 * @return  A new matcher for this pattern.
	 * It is null in case of error
	 */
	@Override
	public AC_Matcher matcher() {

		AC_Matcher m = null;

		try {

			if (this._acDictionary != null)
			{
				if (!this._acDictionary.isReady) {
					try {
						buildModel();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}


				}

				m = new AC_Matcher(this);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return m;
	}

	/**
	 * Preprocess the patterns to build the model
	 * @throws Exception
	 */
	private void buildModel() throws Exception{

		synchronized(this) {
			if (!this._acDictionary.isReady)
				this._acDictionary.build(this._patterns);
		}

	}


	@Override
	public void loadPatterns(String filepath) {


		try
		{
			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream in = new ObjectInputStream(fileIn);

			AC_Pattern ac = (AC_Pattern) in.readObject();
			_patterns.clear();
			_patterns.addAll(ac._patterns);
			_acDictionary = ac._acDictionary;
			_acDictionary.isReady = true;
			in.close();
			fileIn.close();
		}catch(IOException i)
		{
			i.printStackTrace();
			return;
		}catch(ClassNotFoundException c)
		{
			System.out.println("AC_Pattern class not found");
			c.printStackTrace();
			return;
		}


	}

	@Override
	public void savePatterns(String filepath) throws Exception  {

		if (!this._acDictionary.isReady) {
			buildModel();


		}

		saveObject(filepath);
	}



	/**
	 * 
	 * @return Shortest patterns length
	 */
	public int getShortestPatternLength() {
        int shortestLength = Integer.MAX_VALUE;
        for (byte[] pattern : _patterns) {
            shortestLength = Math.min(shortestLength, pattern.length);
        }
        return shortestLength;
    }



}
