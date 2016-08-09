
package org.stringmatching.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.stringmatching.determinist.aho_corasick.AC_Matcher;
import org.stringmatching.determinist.aho_corasick.AC_Pattern;
import org.stringmatching.matcher.Matcher;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.SimpleJSAP;



/**
 * Testing the Aho-Corasick method using a comand line
 * 
 * ex. 
 * java org.stringmatching.tests.Test_Arg_AC -p run, SAU -f FlashPlayerInstallManager.log
 * 

 * output
 * [ Test id: AC-----execution time: 1ms -----From position: 0 ]
 * Pattern: run  -------  Counter: 7  -------  At: [  | 43 | 111 | 179 | 247 | 315 | 383 | 451 | ] 
 * Pattern: SAU  -------  Counter: 14  -------  At: [  | 30 | 39 | 98 | 107 | 166 | 175 | 234 | 243 | 302 | 311 | 370 | 379 | 438 | 447 | ] 
 */
public class Test_Arg_AC {

	static final List<byte[]> _byte_test_patterns  = new ArrayList<byte[]>();
	static String test_id = "AC";
	private static AC_Pattern acPattern;
	private static AC_Matcher ma;
	private static boolean onError = false;

	private final static  String LOADING_ERROR = "Error loading the pattern model, check the file path";
	private final static  String PATTERNS_ERROR = "A list of patterns must be setted";
	private final static  String INPUT_ERROR = "An input is required";
	private final static  String SAVE_ERROR = "Error savinf the patterns, check the file path";
	private static final  String MATCHING_ERROR = "Matching error";

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub


		SimpleJSAP jsap = new SimpleJSAP( 
				"Home Work", 
				"Aho Corasick Pattern matcher method",
				new Parameter[] {
						new FlaggedOption( "patterns", JSAP.STRING_PARSER, "", JSAP.NOT_REQUIRED, 'p', JSAP.NO_LONGFLAG, 
								"List of patterns to match (separated by commas) ex. the, ip. (case sensitive)" ).setList( true ).setListSeparator( ',' ),

								new FlaggedOption( "load", JSAP.STRING_PARSER, "", JSAP.NOT_REQUIRED, 'l', JSAP.NO_LONGFLAG, 
										"Load the patterns model from filepath" ),

										new FlaggedOption( "input_text", JSAP.STRING_PARSER, "", JSAP.NOT_REQUIRED, 'i', JSAP.NO_LONGFLAG, 
												"A simple text to be processed ex.  I have no special talents. I am only passionately curious. ― Albert Einstein" ),

												new FlaggedOption( "input_file", JSAP.STRING_PARSER, "", JSAP.NOT_REQUIRED, 'f', JSAP.NO_LONGFLAG, 
														"File path of the file to be processed" ),

														new FlaggedOption( "save", JSAP.STRING_PARSER, "", JSAP.NOT_REQUIRED, 's', JSAP.NO_LONGFLAG, 
																"file path to save the patterns model " )
				}
				);

		JSAPResult config = jsap.parse(args);    
		if ( jsap.messagePrinted() ) System.exit( 1 );



		String[] patterns = config.getStringArray("patterns");
		//System.out.println(patterns);

		String input_text = config.getString("input_text");
		//System.out.println(input_text);

		String filepath = config.getString("input_file");
		//System.out.println(filepath);

		String loadFilepath = config.getString("load");
		//System.out.println(loadFilepath);

		String saveFilepath = config.getString("save");
		//System.out.println(saveFilepath);


		//by default take the parameters from patterns
		if ((patterns.length > 0) || (loadFilepath.length() > 0)){

			if (patterns.length > 0){
				System.out.print(" patternsssss: ");
				for (int i = 0; i < patterns.length; ++i) {
					System.out.print(" " + patterns[i]);
					_byte_test_patterns.add(patterns[i].getBytes());
					acPattern = new AC_Pattern (_byte_test_patterns);

				}
				System.out.println();
			}else{


				try{
					System.out.println("Loading Patterns from: " + loadFilepath);
					acPattern = new AC_Pattern (loadFilepath);

				}catch(Exception e){


					showError(LOADING_ERROR, jsap);
				}


			}

		}
		else{

			showError(PATTERNS_ERROR, jsap);
		}


		//save patterns to file
		try{
			if (saveFilepath.length() > 0){

				System.out.println("Saving Patterns to: " + saveFilepath);
				acPattern.savePatterns(saveFilepath);
			}
		}catch(Exception e){


			showError(SAVE_ERROR, jsap);
		}

		//create the marcher
		ma = acPattern.matcher();
		if (ma == null){
			showError(" Problems creating the matcher", jsap);
		}

		//by default take the parameters from patterns
		if ((filepath.length() > 0) || (input_text.length() > 0)){

			try{

				if (input_text.length() > 0){

					//doing matching
					System.out.println("doing matching by text: " + input_text);
					doMatchingString(test_id, ma, input_text, 0);

				}else{


					//doing matching
					System.out.println("doing matching using an input file: " + filepath);
					File f = new File(filepath);
					doMatchingFile(test_id, ma, f, 0);

				}
			}catch(Exception e){

				//e.printStackTrace();
				System.out.println(" Error file input" );
				onError = true;

			}

		}
		else{

			showError(INPUT_ERROR, jsap);
		}

		//if everything OK
		if (!onError)
			ma.getMacherResults().printResults();


	}

	/**
	 * In case of error, show a message with the error
	 * 
	 * 
	 * @param msg Console output message
	 */
	private static void showError(String msg, SimpleJSAP jsap) {

		System.out.println(msg);

		System.out.println(jsap.getUsage());;
		System.out.println(jsap.getHelp());

		System.exit(1);

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
	private static void doMatchingString(String id, Matcher matcher, String stream, int start_position){

		//do Matching

		try {
			Test_Matcher htest = new Test_Matcher(id, matcher, stream, start_position);
			htest.run();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			onError  = true;
		}

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
	private static void doMatchingFile(String id, Matcher matcher, File stream, int start_position){

		//do Matching
		Test_Matcher htest;
		try {
			htest = new Test_Matcher(id, matcher, stream, start_position);
			htest.run();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			onError  = true;
		}

	}

}
