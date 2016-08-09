/**
 * 
 */
package org.stringmatching.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.stringmatching.determinist.aho_corasick.AC_Pattern;
import org.stringmatching.heuristic.wu_manber.WM_Matcher;
import org.stringmatching.heuristic.wu_manber.WM_Pattern;
import org.stringmatching.hybrid.ahocorasick_wumanber.AC_WM_Matcher;
import org.stringmatching.hybrid.ahocorasick_wumanber.AC_WM_Pattern;
import org.stringmatching.matcher.Matcher;

/**
 * Tests the matcher for different predefined patterns 
 *
 */
public class Hybrid_Integrity_Tests {

	//set the patterns
	static final List<String> test_patterns  = new ArrayList<String>();
	static final List<byte[]> _byte_test_patterns  = new ArrayList<byte[]>();
	static String test_text = "";
	static String test_id = "";
	private static boolean onError = false;




	public static void main(String[] args) throws Exception{

		String sfile = "/Users/beetecu/Documents/workspaceKokoahPhoneGap/StringMatchingPatterns/resources/log.rtf";

		String sfile1 ="/Users/beetecu/Documents/workspaceKokoahPhoneGap/StringMatchingPatternsv4/bin/R.txt";

		//-------------------------------------------------------------//

		// test_1: finding one character in a short text (sentence)   // 

		//-------------------------------------------------------------//
		test_id = "test_1_1";

		//test_text = "La ropa del ninitone esta tendiendola ";
		test_text = "aaaab";
		//System.out.println("test text: " + test_text);

		test_patterns.add("aa");
		//test_patterns.add("del");
		//test_patterns.add("esta");
		//test_patterns.add("tendiendola");
		//test_patterns.add("ninitone");

		//set new patterns
		setPatterns();

		//create the matcher

		//create the matcher
		AC_WM_Pattern ac_wmPattern = null;

		try {
			ac_wmPattern = new AC_WM_Pattern (_byte_test_patterns,5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AC_WM_Matcher ma = ac_wmPattern.matcher();

		if (ma == null){
			System.out.println("Error creating the matcher");
			System.exit(1);
		}


		//doing matching
		try {
			doMatchingString(test_id, ma, test_text, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			onError  = true;
		}

		//show the results.
		if (!onError)
			//ma.getMacherResults().printResults();


		//-------------------------------------------------------------//

		//test_1_4: finding one word in a file   // 

		//-------------------------------------------------------------//

		test_id = "test_1_4";

		test_patterns.clear();
		test_patterns.add("homeLayout");
		test_patterns.add("int");

		//set new patterns
		setPatterns();

		//create the matcher
		try {
			ac_wmPattern = new AC_WM_Pattern (_byte_test_patterns, 5);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ma = ac_wmPattern.matcher();
		if (ma == null){
			System.out.println("Error creating the matcher");
			System.exit(1);
		}

		//doing matching
		File f = new File(sfile);
		try {
			doMatchingFile(test_id, ma, f, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//show the results.		//show the results.
		//if (!onError)
			//ma.getMacherResults().printResults();


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
