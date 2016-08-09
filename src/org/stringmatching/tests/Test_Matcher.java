package org.stringmatching.tests;

import java.io.File;
import java.io.IOException;

import org.stringmatching.utils.IOUtils;
import org.stringmatching.matcher.Matcher;

/**
 * Class utility to test a matcher
 * 
 * 
 * @author beetecu
 *
 */
public final class Test_Matcher implements Runnable{
	

	
	private  final Matcher _matcher;
	
	private  final byte[] _stream;
	
	private  final int _start_position;
	
	private  final String _id;
	
	
	/**
	 * Constructor
	 * 
	 * @param id An id to identify the test
	 * @param matcher The matcher to be tested
	 * @param stream The input text
	 * @param start_position the input text segment offset (for application segmenting the input text)
	 * @throws Exception 
	 */
	public  Test_Matcher(final String id, final Matcher matcher, final String stream, final int start_position) throws Exception{
		
		_id = id;
		
		
		_matcher = matcher;
		
		_start_position = start_position;
		_stream = stream.getBytes();
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param id An id to identify the test
	 * @param matcher The matcher to be tested
	 * @param file The input file path
	 * @param start_position the input text segment offset (for application segmenting the input text)
	 * @throws Exception 
	 */
	public  Test_Matcher(final String id, final Matcher matcher, final File file, final int start_position) throws Exception{
		
		
		_id = id;
		_matcher = matcher;
		
		if (_matcher == null)
			throw new Exception("Matcher is null");
		
		_start_position = start_position;
		
		
		_stream = IOUtils.readEntireFile(file);
		
		
	}
	
	/**
	 * @return test id
	 * 
	 */
	public final String getId() {
		return _id;
	}


	@Override
	public void run() {
		
		long  TInic, TEnd, exec_time;  

		TInic = System.currentTimeMillis();

		try{
			_matcher.doMach(_stream, _start_position);
			
		}catch(Exception e){
			
			System.out.println("Error during the match process");
			e.printStackTrace();
		}
		

		TEnd = System.currentTimeMillis();
		exec_time = TEnd - TInic;

		System.out.println("[ Test id: " + _id + "-----execution time: " + exec_time + ("ms") + 
							" -----From position: " + _start_position + " ]");
		
	}



	
	

}
