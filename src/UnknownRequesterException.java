/*
 * UnknownRequesterException.java
 *
 */

package parallel.smp;

/**
 * Exception thrown when the given requester isn't known.
 * @author Kyle Dewey
 */
public class UnknownRequesterException extends Exception {
    // begin constants
    public static final String MESSAGE_PREFIX = "Unkown requester with ID: ";
    // end constants

    /**
     * Creates an exception with the given message
     * @param message The message to use
     */
    public UnknownRequesterException( String message ) {
	super( message );
    }

    /**
     * Creates an exception triggered from a request from the given 
     * requester
     * @param requester The requester
     */
    public UnknownRequesterException( int requester ) {
	this( MESSAGE_PREFIX + Integer.toString( requester ) );
    }
}
