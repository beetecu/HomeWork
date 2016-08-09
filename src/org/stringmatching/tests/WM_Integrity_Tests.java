/**
 * 
 */
package org.stringmatching.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.stringmatching.heuristic.wu_manber.WM_Matcher;
import org.stringmatching.heuristic.wu_manber.WM_Pattern;
import org.stringmatching.matcher.Matcher;


/**
 * Tests the Matcher for different predefined patterns
 *
 */
public class WM_Integrity_Tests {

	//set the patterns
	static List<String> test_patterns  = new ArrayList<String>();
	static final List<byte[]> _byte_test_patterns  = new ArrayList<byte[]>();
	static String test_text = "";
	static String test_id = "";
	static boolean onError = false;

	public static void main(String[] args){

		String sfile = "/Users/beetecu/Documents/workspaceKokoahPhoneGap/StringMatchingPatterns/resources/log.rtf";


		//-------------------------------------------------------------//

		// test_1: finding one character in a short text (sentence)   // 

		//-------------------------------------------------------------//
		test_id = "test_1_1";
		

		test_text = "aaaalegodadfsgflegodaddsasd";
		System.out.println("test text: " + test_text);

		test_patterns.add("aa");

		//set new patterns
		setPatterns();

		//create the matcher
		WM_Pattern wmPattern = null;
		
		try {
			wmPattern = new WM_Pattern (_byte_test_patterns);
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		if (wmPattern == null){
			System.out.println("Error creating the pattern model");
			System.exit(1);
		}
		
		WM_Matcher ma = wmPattern.matcher();
		
		if (ma == null){
			System.out.println("Error creating the matcher");
			System.exit(1);
		}
		

		//doing matching
		try {
			doMatchingString(test_id, ma, test_text, 0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("Error during the matching process");
			e1.printStackTrace();
			
			onError = true;
			System.exit( 1 );
		}

		//show the results.
		if (!onError)
			ma.getMacherResults().printResults();


		//-------------------------------------------------------------//

		// test_2: finding one long string in a short text (sentence)   // 

		//-------------------------------------------------------------//

		test_id = "test_1_2";
		test_patterns.clear();
		test_patterns.add("legodadfsgfl");

		//set new patterns
		setPatterns();

		//create the matcher
		try {
			wmPattern = new WM_Pattern (_byte_test_patterns);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ma = wmPattern.matcher();
		if (ma == null){
			System.out.println("Error creating the matcher");
			System.exit(1);
		}

		//doing matching
		try {
			doMatchingString(test_id, ma, test_text, 0);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			onError = true;
			System.exit( 1 );
		}

		//show the results.
		if (!onError)
			ma.getMacherResults().printResults();


		//-------------------------------------------------------------//

		//test_1_3: finding one character in a file   // 

		//-------------------------------------------------------------//

		test_id = "test_1_3";
		System.out.println("test text: file from: " + sfile);

		test_patterns.clear();
		test_patterns.add("together");

		//set new patterns
		setPatterns();

		//create the matcher
		try {
			wmPattern = new WM_Pattern (_byte_test_patterns);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ma = wmPattern.matcher();
		if (ma == null){
			System.out.println("Error creating the matcher");
			System.exit(1);
		}

		//doing matching

		File f = new File(sfile);


		//doing matching
		try {
			doMatchingFile(test_id, ma, f, 0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			onError = true;
			System.exit( 1 );
		}

		//show the results.
		if (!onError)
			ma.getMacherResults().printResults();



		//-------------------------------------------------------------//

		//test_1_4: finding one word in a file   // 

		//-------------------------------------------------------------//

		test_id = "test_1_4";


		test_patterns.clear();
		test_patterns.add("and");

		//set new patterns
		setPatterns();

		//create the matcher
		try {
			wmPattern = new WM_Pattern (_byte_test_patterns);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ma = wmPattern.matcher();
		if (ma == null){
			System.out.println("Error creating the matcher");
			System.exit(1);
		}

		//doing matching
		try {
			doMatchingFile(test_id, ma, f, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//show the results.
		ma.getMacherResults().printResults();

		
	}


	/**
	 * Read the patterns from a list an convert to byte[]
	 *
	 */
	public static void setPatterns(){

		_byte_test_patterns.clear();

		//convert to byte the string list of patterns
		Iterator<String> patternsIterator = test_patterns.iterator();
		while (patternsIterator.hasNext()) {

			String pattern = patternsIterator.next();
			_byte_test_patterns.add(pattern.getBytes());

			//System.out.println("test_patterns: " + test_patterns);

		} 

	}

	/**
	 * Get File From Resource
	 *
	 * @param filename 
	 * @return The resource file
	 *
	 */
	public File getFileFromResource(String filename){

		File file = new File(this.getClass().getResource(filename).getPath());

		return file;
	}

	/**
	 * Call the test matcher using an input text
	 * 
	 * @param id An id to identify the test
	 * @param matcher The matcher to be tested
	 * @param stream The input text
	 * @param start_position the input text segment offset (for application segmenting the input text)
	 * @throws Exception 
	 */
	private static void doMatchingString(String id, Matcher matcher, String stream, int start_position) throws Exception{

		//do Matching
		Test_Matcher htest = new Test_Matcher(id, matcher, stream, start_position);
		htest.run();

	}
	


	/**
	 * Call the test matcher using an input text file
	 * 
	 * @param id An id to identify the test
	 * @param matcher The matcher to be tested
	 * @param file The input file path
	 * @param start_position the input text segment offset (for application segmenting the input text)
	 * @throws Exception 
	 */
	private static void doMatchingFile(String id, Matcher matcher, File stream, int start_position) throws Exception{

		//do Matching
		Test_Matcher htest;
		try {
			htest = new Test_Matcher(id, matcher, stream, start_position);
			htest.run();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
