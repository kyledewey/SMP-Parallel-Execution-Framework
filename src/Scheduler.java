/*
 * Scheduler.java
 *
 */

package parallel.smp;

import java.util.*;

/**
 * Orders requests for items.
 * @author Kyle Dewey
 */
public abstract class Scheduler< T > {
    // begin instance variables
    private int numRequesters;
    private List< T > items;
    // end instance variables

    /**
     * Creates a scheduler with the given number of requesters.
     * Assumes that requesters have id's 0 through numRequesters - 1
     * @param numRequesters The number of requesters
     * @param items The items to schedule
     */
    public Scheduler( int numRequesters,
		      List< T > items ) {
	this.numRequesters = numRequesters;
	this.items = new ArrayList< T >( items );
    }

    /**
     * Gets the number of requesters
     * @return The number of requesters
     */
    public int getNumRequesters() {
	return numRequesters;
    }

    /**
     * Gets the items we have
     * @return The items we have
     */
    protected List< T > getItems() {
	return items;
    }

    /**
     * Gets if the given requester exists
     * @param requester The requester
     * @return true if the requester exists, else false
     */
    public boolean isRequester( int requester ) {
	return requester >= 0 && requester < getNumRequesters();
    }

    /**
     * Validates that the given requester exists.
     * @param requester The ID of the requester
     * @throws UnknownRequesterException If the requester isn't recognized
     */
    public void validateRequester( int requester ) 
	throws UnknownRequesterException{
	if ( !isRequester( requester ) ) {
	    throw new UnknownRequesterException( requester );
	}
    }

    /**
     * Gets the next item for the given requester, validating that the 
     * requester exists.
     * @param requester The requester
     * @return The item for the given requester.  Returns null if we are out of
     * items for the given requester.
     * @throws UnknownRequesterException If the requester isn't recognized
     */
    public T nextItem( int requester ) throws UnknownRequesterException {
	validateRequester( requester );
	return nextItemWithoutValidation( requester );
    }

    /**
     * Gets the next item for the given requester.
     * @param requester The requester
     * @return The item for the given requester.  Returns null if we are out of
     * items for the given requester.
     */
    public abstract T nextItemWithoutValidation( int requester );

}
