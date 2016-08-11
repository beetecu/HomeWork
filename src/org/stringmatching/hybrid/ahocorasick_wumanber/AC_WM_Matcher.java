package org.stringmatching.hybrid.ahocorasick_wumanber;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import org.stringmatching.determinist.aho_corasick.AC_Matcher;
import org.stringmatching.determinist.aho_corasick.AC_Pattern;
import org.stringmatching.heuristic.wu_manber.WM_Matcher;
import org.stringmatching.heuristic.wu_manber.WM_Pattern;
import org.stringmatching.matcher.Matcher;
import org.stringmatching.matcher.MatcherResults;
import org.stringmatching.matcher.PatternResults;


/**
 * This is an implementation of the hybrid multiple pattern matching from
 * http://www.just.edu.jo/~munzer/MyPubs/Hybrid.pdf
 * <p>
 * The premise is to exploit the fact that WM is inefficient for
 * short strings and AC performance degrades for longer strings. 
 * 
 * According to the paper, the system best performance 
 * is achieved using 1 thread for the Aho-Corasick method
 * and 3 thread for the Wu-Manber method. The sequences
 * with size interval betweeen 1-7 are processed by the
 * Aho-Corasick and the ones > 7 by the Wu Manber
 *  
 * 
 */

public class AC_WM_Matcher extends Matcher{

	/**
	 * Number of thread in the pool
	 */
	private static final int FIXEDTHREADPOO = 5;

	private static AC_WM_Pattern _ac_wm_Pattern;

	private static int _numberOfACThreads;
	private static int _numberOfWMThreads;


	static boolean _debug = false;

	private    AC_Matcher _acMatcher;
	private    WM_Matcher _wmMatcher;





	/**
	 * Constructor 
	 * 
	 * @param ac_wm_Pattern Hybrid pattern model
	 * @param numberOfACThreads Number of Aho-Corasick threads
	 * @param numberOfWMThreads Number of Wu Mamber threads
	 * @throws Exception 
	 * 
	 */
	public AC_WM_Matcher(final AC_WM_Pattern ac_wm_Pattern, final int numberOfACThreads, final int numberOfWMThreads) throws Exception {


		this._macherResults = null;

		_numberOfACThreads = numberOfACThreads;
		_numberOfWMThreads = numberOfWMThreads;

		if (ac_wm_Pattern == null)
			throw new IllegalArgumentException("Error, Pattern model is null"); 

		_ac_wm_Pattern = ac_wm_Pattern;

		AC_Pattern acPattern = _ac_wm_Pattern.getACPatternModel();

		if (acPattern == null)
			_acMatcher = null;
		else
			_acMatcher = acPattern.matcher();

		WM_Pattern wmPattern = _ac_wm_Pattern.getWMPatternModel();

		if (wmPattern == null)
			_wmMatcher = null;
		else
			_wmMatcher = wmPattern.matcher();

		if ((_wmMatcher == null) && (_acMatcher== null))
			throw new IllegalArgumentException("Error in the Patter model"); 

	}


	@Override
	public void doMach(byte[] stream, long start_position) throws Exception {


		//_stream = stream;

		int block_size = 0;
		byte[] block;
		long block_start = 0;
		long block_end = block_size;
		
		LaunchMatcherAC tacMatcher;
		LaunchMatcherWM twmMatcher;

		ExecutorService executor = Executors.newFixedThreadPool(FIXEDTHREADPOO);

		if (_acMatcher != null){

			if (this._numberOfACThreads > 1){

				block_size = stream.length / this._numberOfACThreads;

				block_end = block_size;



				for (int i=0; i< this._numberOfACThreads; i++){

					block = new byte[(int) (block_end - block_start)];	

					System.arraycopy(stream, (int)block_start, block, 0, (int) (block_end - block_start));

					tacMatcher = new LaunchMatcherAC(block, block_start);

					block_start = block_end;
					block_end = block_end + block_size;

					executor.execute(tacMatcher);

				}
			}else{

				tacMatcher = new LaunchMatcherAC(stream, 0);
				executor.execute(tacMatcher);
			}


		}

		if (_wmMatcher != null){

			if (this._numberOfWMThreads > 1){

				block_size = stream.length / this._numberOfWMThreads;
				block_start = 0;
				block_end = block_size;

				for (int i=0; i< this._numberOfWMThreads; i++){

					block = new byte[(int) (block_end - block_start)];	

					System.arraycopy(stream, (int)block_start, block, 0, (int) (block_end - block_start));

					twmMatcher = new LaunchMatcherWM(block, block_start);

					block_start = block_end;
					block_end = block_end + block_size;

					executor.execute(twmMatcher);

				}
			}else{
				
				twmMatcher = new LaunchMatcherWM(stream, 0);
				executor.execute(twmMatcher);
			}


		}
		executor.shutdown();


		while (!executor.isTerminated()) {

			//System.out.println("working");
		}

		System.out.println();
		//_wmMatcher.getMacherResults().printResults();
		//_acMatcher.getMacherResults().printResults();

		System.out.println("End matching process");
		updateResults();




	}

	/**
	 * Update Matcher results
	 * @throws Exception 
	 * 
	 */
	public  void updateResults() throws Exception {


		try{

			if (_macherResults == null){

				_macherResults = new MatcherResults();

				if (_acMatcher != null){
					// the results from the Aho Coracick Matcher
					for (PatternResults p : _acMatcher.getMacherResults()._results)
						_macherResults._results.add(p);
				}

				if (_wmMatcher != null){

					// the results from the Aho Coracick Matcher
					for (PatternResults p : _wmMatcher.getMacherResults()._results)
						_macherResults._results.add(p);
				}
			}
			else{
				// update for each item their results list
				if (_acMatcher != null){
					// the results from the Aho Coracick Matcher
					_macherResults.update(_acMatcher.getMacherResults());
				}

				if (_wmMatcher != null){
					// the results from the Aho Coracick Matcher
					_macherResults.update(_wmMatcher.getMacherResults());
				}


			}
		}catch(Exception e){

			e.printStackTrace();

			_macherResults = null;


		}
	}


	/**
	 * 
	 * @author beetecu
	 *
	 */
	private class LaunchMatcherWM implements Runnable{

		private long _offset;
		private byte[] _block;

		public LaunchMatcherWM(byte[] block, long offset){
			_offset = offset;
			_block = block;
		}


		@Override
		public void run() {
			// TODO Auto-generated method stub
			//System.out.println("_block_start " + _offset);
			try {
				_wmMatcher.doMach(_block, _offset);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class LaunchMatcherAC implements Runnable{

		private long _offset;
		private byte[] _block;

		public LaunchMatcherAC(byte[] block, long offset){
			_offset = offset;
			_block = block;
		}


		@Override
		public void run() {
			try {
				_acMatcher.doMach(_block, _offset);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}


}
