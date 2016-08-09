/**
 * 
 */
package org.stringmatching.heuristic.wu_manber;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.stringmatching.pattern.PatternModel;

/**
 * Build  the Pattern Model for the Wu Manber method.
 * To be finished because it is only with the purpose 
 * to build the the hybrid method.
 * Requires to be optimized (a lot).
 * 
 */
public final class WM_Pattern extends PatternModel implements Serializable{
	 /**
     * Control the model status
     */
	public transient static boolean isReady;
	
	public static int shortestPatternLength = 0;
    public static int shiftTableSize = 0;
    public static int shiftTableHashBitMask = 0;
    public static int verifierTableSize = 0;
    public static int blockSize = 1;
    public static int[] shifts;
    public  Map<ByteBuffer, List<Integer>> verifiers;

	/**
	 * Constructor 
	 * 
	 * @param patterns : Patterns List
	 * @throws Exception 
	 */
	public WM_Pattern(final List<byte[]> patterns) throws Exception{

		super(patterns);
		
		buildModel();

	}


	/**
	 * Constructor using a stored set of patterns
	 * 
	 * @param patternfilepath
	 */
	public WM_Pattern(String patternfilepath){

		try {
			
			this.loadPatterns(patternfilepath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	@Override
	public void savePatterns(String filepath) throws IOException {
		// TODO Auto-generated method stub
		
		if (!this.isReady) {
			buildModel();
			
			
		}
		
		try
		{

			FileOutputStream fout = new FileOutputStream(filepath);
			ObjectOutputStream output = new ObjectOutputStream(fout);
			
			try{
				output.writeObject(this);
		
			}
			finally{
				output.close();
			}

		}catch(IOException i)
		{
			i.printStackTrace();
		}
		
	}
	
	

	/**
	 * Build the Wu Manber patterns model
	 * 
	 */
	private void buildModel() {
		
		try{
			
			calculateParameters();
	        buildTables();
	        
	        this.isReady = true;
			
		}catch(Exception e){
			throw  e;
		}
		
	}


	/**
	 * 
	 */
	private void calculateParameters() {
		// TODO Auto-generated method stub
		// calculate basic parameters:
        shortestPatternLength = computeShortestPatternLength();
        blockSize = calculateBlockSize();
        shiftTableSize = 1 << calculateShiftTablePowerTwoSize();
        shiftTableHashBitMask = shiftTableSize - 1;
        verifierTableSize = calculateVerifierTableSize();
	}
	
	/**
	 * 
	 * @return shortestLength
	 */
	private int computeShortestPatternLength() {
        int shortestLength = Integer.MAX_VALUE;
        for (byte[] pattern : _patterns) {
            shortestLength = Math.min(shortestLength, pattern.length);
        }
        return shortestLength;
    }
	
	/**
	 * 
	 * @return shortestLength
	 */
	public int getShortestPatternLength() {
        
		return this.shortestPatternLength;
    }
	
	
	
	/**
	 * 
	 * @return
	 */
	private int calculateBlockSize() {
        //TODO: use wu-manber heuristic for block size.
        return shortestPatternLength > 1 ? 2 : 1;
    }
	
	/**
	 * 
	 * @return
	 */
	private int calculateShiftTablePowerTwoSize() {
        return 12; //TODO: calculate size based on number of patterns
    }

	/**
	 * 
	 * 
	 */
    private int calculateVerifierTableSize() {
        return 1024; //TODO: calculate verifier table size based on number of patterns.
    }
    
    /**
	 * 
	 * 
	 */
    private void buildTables() {
        shifts = new int[shiftTableSize];
        verifiers = new HashMap<ByteBuffer,List<Integer>>(verifierTableSize);

        initialiseDefaultShift();

        byte[] pattern;
        // For each pattern, calculate pattern-specific shifts and add a verifier:
        for (int index=0; index<_patterns.size(); index++) {
        	
        	 pattern = _patterns.get(index);
             final int patternLength = pattern.length;
             byte[] block = new byte[]{}; 

             // for each block in a pattern, calculate its shift:
             for (int blockIndex = blockSize; blockIndex <= patternLength; blockIndex++) {
            	
            	 block =  new byte[blockIndex - (blockIndex-blockSize)]; 
            	 System.arraycopy(pattern, blockIndex-blockSize, block, 0, blockIndex - (blockIndex-blockSize));
            	
            	 
            	 
                 final int hashValue = getBlockHash(block);
                 final int shiftValue = patternLength - blockIndex;
                 shifts[hashValue] = Math.min(shifts[hashValue], shiftValue);
                
             }

             addVerifier(block,index);
        }
    }
    
    /**
     * 
     */
    private void initialiseDefaultShift() {
        final int defaultShift = shortestPatternLength - blockSize + 1;
        Arrays.fill(shifts, defaultShift);
    }
    
    /**
     * @param block 
     * @return getBlockHash
     * 
     */
    public int getBlockHash(byte[] block) {
        return Arrays.hashCode(block) & shiftTableHashBitMask;
    }

    /**
     * 
     */
    private void addVerifier(byte[] block, int patternIndex) {
        List<Integer> verifiersForBlock = verifiers.get(block);
        if (verifiersForBlock == null) {
            verifiersForBlock = new ArrayList<Integer>();
            verifiersForBlock.add(patternIndex);
            verifiers.put(ByteBuffer.wrap(block), verifiersForBlock);
            
            //System.out.println(new String(verifiers.get(ByteBuffer.wrap(block))).get(0)));
            
        }
        else{
        	//verifiersForBlock.add(pattern);
        	verifiers.get(block).add(patternIndex);
        	
        }
        
   }

	@Override
	/*************UNFINISHED****************/
	/**
	 *
	 * To be developed in the next version
	 */
	public void loadPatterns(String filepath) throws IOException {
		// TODO Auto-generated method stub
		
		
		//isReady = true;
		
	}


	/**
	 * Creates a matcher that will match the given input against this pattern.
	 *
	 * @return  A new matcher for this pattern.
	 * It is null in case of error
	 */
	@Override
	public final WM_Matcher matcher() {
		// TODO Auto-generated method stub
		
		WM_Matcher m = null;;
		try {
			m = new WM_Matcher(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return m;
	}
	
	

}
