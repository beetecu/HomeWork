package org.stringmatching.hybrid.ahocorasick_wumanber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

import org.stringmatching.determinist.aho_corasick.AC_Pattern;
import org.stringmatching.heuristic.wu_manber.WM_Pattern;
import org.stringmatching.pattern.PatternModel;

/**
 * Build the the Pattern Model for the hybrid method.
 * <p>
 * According to the predefined max length for Aho-Corasick patterns model, 
 * the patterns are splitter to build the Aho-Corasick and the Wu Manber
 * patterns model
 * 
 */
public class AC_WM_Pattern extends PatternModel implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -519287813200768207L;

	private static final int NUMBER_OF_AC_THREADS = 1; //By default

	private static final int NUMBER_OF_WM_THREADS = 3; //By default

	private static AC_Pattern _acPatternModel;
	
	private static WM_Pattern _wmPatternModel;
	
	private static int _maxAcLenght;
	
	
	/*
	 *  a map for the characters in the patterns
	 */
	public static int[] _patternsAlphabet = new int[256];
	
    /**
     * Control the model status
     */
	private transient boolean isReady;
	
	/**
	 * Constructor
	 * @param patterns Patterns List
	 * @param maxAcLenght Max length for Aho-Corasick patterns model
	 * @throws Exception 
	 * 
	 * 
	 */
	public AC_WM_Pattern(final List<byte[]> patterns, int maxAcLenght) throws Exception{
		
		super(patterns);
		
		//if (maxAcLenght == 0)
			//throw new Exception("The max Aho Corasicks pattern size must be bigger than 0");
		
		
		_maxAcLenght = maxAcLenght;
		
		this.isReady = false;
			
		try{
			
			buildModel();
		
		}catch(Exception e){
			throw new Exception("Error building the Hybrid patterns model");
			
		}
		
	}
	

	/**
	 * @return Max Aho-Corasick pattern length
	 * 
	 */
     public int getMaxAcLength(){
    	 
    	
    	 return _maxAcLenght;
     }
     
     /**
 	 * @return Minimal length of WM Patterns
 	 * 
 	 */
      public int getMinLengthWMPatterns(){
    	
    	int ret = -1; 
      	
    	if (_wmPatternModel != null)
    		ret= _wmPatternModel.getShortestPatternLength();
      	
      	return ret;
      }

      /**
   	 * @return Minimal length of WM Patterns
   	 * 
   	 */
        public int getMinLengthACPatterns(){
       	 
        	int ret = -1; 
          	
        	if (_acPatternModel != null)
        		ret= _acPatternModel.getShortestPatternLength();
          	
          	return ret;
       	 
        }
	
	

 	/**
 	 * Constructor using a stored set of patterns
 	 * 
 	 * @param patternfilepath
 	 */
	public AC_WM_Pattern(String patternfilepath){ 
		super();
	
		try {
			this.loadPatterns(patternfilepath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	/**
 	 * Build the patterns model for the hybrid methods
 	 * Split the patterns to their 
 	 * corresponding list and build the 
 	 * Aho-Corasick and the Wu Manber patterns models
 	 * 
 	 */
	private void buildModel() throws Exception{
	
		_acPatternModel = null;
		_wmPatternModel = null;
		
		for (int i=0; i<_patternsAlphabet.length;i++)
			_patternsAlphabet[i] = 0;
		
		 List<byte[]> acPatterns = new ArrayList<byte[]>();
		 List<byte[]> wmPatterns = new ArrayList<byte[]>();
		
		for (byte[] pattern : _patterns){
			
			if (pattern.length <= _maxAcLenght){
				acPatterns.add(pattern);
			}else{
				wmPatterns.add(pattern);
			}
			
			
			
		}
		
	
		if (!acPatterns.isEmpty())
			_acPatternModel = new AC_Pattern(acPatterns);
		
		if (!wmPatterns.isEmpty())
			_wmPatternModel = new WM_Pattern(wmPatterns);
		
		this.isReady = true;
	
	}
	
	/**
	 * 
	 * @return The Aho-Corasick patterns model
	 */
	public AC_Pattern getACPatternModel(){
		
		
		return this._acPatternModel;
	}
	
	/**
	 * 
	 * @return The Wu Manber patterns model
	 */
	public WM_Pattern getWMPatternModel(){
		
		return this._wmPatternModel;
	}
	
	
	@Override
	public void savePatterns(String filepath) throws Exception {
		// TODO Auto-generated method stub
		
		//check for both models
		if (!this._acPatternModel._acDictionary.isReady) {
			buildModel();
		}
		if (!this._wmPatternModel.isReady) {
			buildModel();
			
			
		}
		
		this.saveObject(filepath);
		
	}

	@Override
	//*********************UNFINISHED**************************
	public void loadPatterns(String filepath) throws IOException {
		// TODO Auto-generated method stub
		AC_WM_Pattern ac_wm = null;
		try
		{
			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
	
			ac_wm = (AC_WM_Pattern) in.readObject();
			_patterns.clear();
			_patterns.addAll(ac_wm._patterns);
			
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

	/**
	 * Creates the hybrid matcher.
	 * @param numberOfACThreads 
	 * @param numberOfWMThreads 
	 *
	 * @return  A new matcher 
	 * @throws Exception 
	 */
	public AC_WM_Matcher matcher(int numberOfACThreads, int numberOfWMThreads) throws Exception {
		if (!this.isReady) {
			buildModel();

		}
		
		AC_WM_Matcher m;
		
		try{
			 m = new AC_WM_Matcher(this, numberOfACThreads, numberOfWMThreads);
			
		}catch(Exception e){
			
			e.printStackTrace();
			return null;
		}
		
		return m;
		
	}
	
	

	@Override
	public AC_WM_Matcher matcher() throws Exception {
		if (!this.isReady) {
			buildModel();
		}
		
		AC_WM_Matcher m;
		
		try{
			
			m = new AC_WM_Matcher(this, NUMBER_OF_AC_THREADS, NUMBER_OF_WM_THREADS);
			
		}catch(Exception e){
			
			e.printStackTrace();
			return null;
		}
				
				
		return m;
	}

}
