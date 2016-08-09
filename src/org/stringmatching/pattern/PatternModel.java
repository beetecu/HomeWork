package org.stringmatching.pattern;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract pattern class implementing basic methods
 * for a list of patterns 
 *
 * @author beetecu
 */
public abstract class PatternModel implements IPatternModel, Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * List of pattern to be matched 
	 * 
	 */
	protected final List<byte[]> _patterns;

	/**
	 * Constructor
	 * @param patterns 
	 * @throws Exception 
	 * 
	 */
	public PatternModel(final List<byte[]> patterns) throws Exception{

		if (patterns.isEmpty())
			throw new IllegalArgumentException("The patterns list is empty");
		else
			_patterns = patterns;
	}

	/**
	 * Constructor
	 * 
	 */
	public PatternModel() {
		// TODO Auto-generated constructor stub
		_patterns = new ArrayList<byte[]>();
	}

	/**
	 * Return the patterns list
	 * 
	 * @return A list of patterns in byte format
	 *            
	 */
	public List<byte[]> getPatterns(){

		return this._patterns;

	}
	
	
	/**
	 * Generic function to save an Object
	 * 
	 * @param filepath 
	 * @throws IOException 
	 *            
	 */
	public void saveObject(String filepath) throws IOException  {

		try
		{

			FileOutputStream fout = new FileOutputStream(filepath);
			ObjectOutputStream output = new ObjectOutputStream(fout);
			
			try{
				output.writeObject(this);
				//System.out.println("Number_saced:" + this._acDictionary._numberOfItems);
			}
			finally{
				output.close();
			}

		}catch(IOException i)
		{
			//i.printStackTrace();
			System.out.println("WU Manber: Error saving the Pattern Model");
			throw new IOException();
		}
	}


	
}
