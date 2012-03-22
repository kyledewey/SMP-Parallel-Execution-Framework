/*
 * SchedulerFactory.java
 *
 */

package parallel.smp;

import java.util.*;

/**
 * Creates schedules of a specific kind.
 * @author Kyle Dewey
 */
public interface SchedulerFactory< T > {
    /**
     * Makes a schedule for the given number of requesters and the
     * given number of items
     * @param numRequesters The number of requesters
     * @param items The items to schedule
     * @return A scheduler based on these parameters
     */
    public Scheduler< T > makeScheduler( int numRequesters,
					 List< T > items );
}
