/*
 * QueueSchedulerFactory.java
 *
 */

package parallel.smp;

import java.util.*;

/**
 * Makes queue schedulers.
 * @author Kyle Dewey
 */
public class QueueSchedulerFactory< T > implements SchedulerFactory < T > {
    /**
     * Makes a schedule for the given number of requesters and the
     * given number of items
     * @param numRequesters The number of requesters
     * @param items The items to schedule
     * @return A scheduler based on these parameters
     */
    public Scheduler< T > makeScheduler( int numRequesters,
					 List< T > items ) {
	return new QueueScheduler< T >( numRequesters,
					items );
    }
}
