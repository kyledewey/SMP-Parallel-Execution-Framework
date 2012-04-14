/*
 * AnalysisFramework.java
 *
 */

package parallel.smp;

import java.util.*;
import java.util.concurrent.*;

public class AnalysisFramework< T > {
    // begin instance variables
    private Scheduler< T > scheduler; // the scheduler for the different threads
    private List< AnalyzerRunnable< T > > runners; // things that do the running
    // end instance variables

    /**
     * Creates an analysis framework across the given items for the given
     * number of threads.  Uses the given factory to make the scheduler
     * @param numThreads The number of threads to use
     * @param items The items to put into the schedule
     * @param schedulerFactory That which makes the schedules
     * @param analyzerFactory Thing that can make analyzers for the given
     * type
     */
    public AnalysisFramework( int numThreads,
			      List< T > items,
			      SchedulerFactory< T > schedulerFactory,
			      AnalyzerFactory< T > analyzerFactory ) {
	scheduler = schedulerFactory.makeScheduler( numThreads,
						    items );
	runners = AnalyzerRunnable.makeRunners( scheduler,
						analyzerFactory );
    }

    /**
     * Like <code>AnalysisFramework</code>, but it uses a 
     * <code>QueueSchedulerFactory</code>.
     * @param numThreads The number of threads to use
     * @param items The items to put into the schedule
     * @param analyzerFactory Thing that can make analyzers for the given type
     */
    public AnalysisFramework( int numThreads,
			      List< T > items,
			      AnalyzerFactory< T > analyzerFactory ) {
	this( numThreads,
	      items,
	      new QueueSchedulerFactory< T >(),
	      analyzerFactory );
    }

    /**
     * Like <code>AnalysisFramework</code>, only it uses 
     * <code>Runtime.getRuntime().availableProcessors()</code> for
     * <code>numThreads</code>.
     * @param items The items to put into the schedule
     * @param analyzerFactory Thing that can make analyzers for the
     * given type
     */
    public AnalysisFramework( List< T > items,
			      AnalyzerFactory< T > analyzerFactory ) {
	this( availableProcessors(),
	      items,
	      analyzerFactory );
    }

    public static int availableProcessors() {
	return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Performs the actual analyses.
     * Blocks until all threads are finished.
     */
    public void doAnalysis() {
	ExecutorService executor = 
	    Executors.newFixedThreadPool( scheduler.getNumRequesters() );
	for( AnalyzerRunnable< T > runner : runners ) {
	    executor.submit( runner );
	}

	try {
	    executor.shutdown();
	    executor.awaitTermination( Long.MAX_VALUE,
				       TimeUnit.SECONDS );
	} catch ( InterruptedException e ) {
	    System.err.println( e );
	}
    }
}

