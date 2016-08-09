# HomeWork
Multithread Hybrid System for String Pattern Matching (Aho-Corasick and Wu Manber)

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
 * NOTE: The Wu Manber method require to be optimazed
 */

Usage:
  [--help]
        Prints this help message.

  [-p patterns1,patterns2,...,patternsN ]
        List of patterns to match (separated by commas) ex. the, ip. (case
        sensitive) (default: )

  [-i <input_text>]
        A simple text to be processed ex.  I have no special talents. I am only
        passionately curious. ― Albert Einstein (default: )

  [-f <input_file>]
        File path of the file to be processed (default: )

  [-s <save>]
        file path to save the patterns model  (default: )

  -z <ac_pattern_size>
        Aho-Corasick maxime parameter size (default: 7)

  -c <ac_threads>
        Number of threads for Aho-Corasick method (default: 1)

  -m <wm_threads>
        Number of threads for Wu Manber method (default: 3)


