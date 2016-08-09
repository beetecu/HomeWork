package org.stringmatching.utils;


/**
 * 
 * Utility for regular expressions using a byte array.
 * Turns out that regular expressions are eight bit safe in Java, 
 * and bytes can safely map into the lower half of the character type. 
 * With a simple adapter it becomes a trivial task. 
 * 
 * @author others
 *
 */
public class ByteCharSequence implements CharSequence {

	/**
	 * 
	 */
    private final byte[] data;
    /**
	 * 
	 */
    private final int length;
    /**
	 * 
	 */
    private final int offset;
    
    /**
     * @param data 
	 * 
	 */
    public ByteCharSequence(byte[] data) {
        this(data, 0, data.length);
    }

    /**
     * @param data 
     * @param offset 
     * @param length 
	 * 
	 */
    public ByteCharSequence(byte[] data, int offset, int length) {
        this.data = data;
        this.offset = offset;
        this.length = length;
    }


    @Override
    public int length() {
        return this.length;
    }

    @Override
    public char charAt(int index) {
        return (char) (data[offset + index] & 0xff);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new ByteCharSequence(data, offset + start, end - start);
    }

}

