/*
 * QueueScheduler.java
 * 
 */

package parallel.smp;

import java.util.*;
import java.util.concurrent.*;

/**
 * Puts all requests in a queue, and requesters feed off of that queue.
 * This is best when requesters rarely have to take items off the queue.
 * This is poor when analysis is short, which leads to contention on the 
 * popping of the queue.
 * @author Kyle Dewey
 */
public class QueueScheduler< T > extends Scheduler< T >{
    // begin instance variables
    private Queue< T > queue;
    // end instance variables

    /**
     * Creates a new scheduler with the given items.
     * @param numRequesters The number of requesters
     * @param items The items to put in the queue
     */
    public QueueScheduler( int numRequesters,
			   List< T > items ) {
	super( numRequesters,
	       items );
	queue = new ConcurrentLinkedQueue< T >( items );
    }

    /**
     * Gets the next item for the given requester.
     * @param requester The requester
     * @return The item for the given requester.  Returns null if we are out of
     * items for the given requester.
     */
    public T nextItemWithoutValidation( int requester ) {
	return queue.poll();
    }
}

