/*
 * AnalyzerRunnable.java
 *
 */

package parallel.smp;

import java.util.*;

public class AnalyzerRunnable< T > implements Runnable {
    // being instance variables
    private int id; // id of this runner
    private Scheduler< T > scheduler; // thing to get items to analyze from
    private Analyzer< T > analyzer; // thing that does analysis
    // end instance variables

    /**
     * Creates a new runnable based on the given analyzer
     * @param id The id to use
     * @param scheduler A scheduler that can we can get items from to analyze
     * @param analyzer The analyzer that can analyze said items
     */
    public AnalyzerRunnable( int id, 
			     Scheduler< T > scheduler,
			     Analyzer< T > analyzer ) {
	this.id = id;
	this.scheduler = scheduler;
	this.analyzer = analyzer;
    }

    /**
     * Gets the id.
     * @return The id
     */
    public int getId() {
	return id;
    }

    /**
     * Runs the analyzer
     */
    public void run() {
	T currentItem = null;
	try {
	    while( ( currentItem = scheduler.nextItem( getId() ) ) != null ) {
		analyzer.doAnalysis( currentItem );
	    }
	} catch ( UnknownRequesterException e ) {
	    // shouldn't be possible
	    e.printStackTrace();
	    System.exit( 1 );
	}
    }

    /**
     * Makes a list of the runners based on the given schedule.
     * @param scheduler The scheduler
     * @param analyzerFactory Thing that can create analyzers
     * @return runners based on the schedule
     */
    public static < T > List< AnalyzerRunnable< T > > 
	makeRunners( Scheduler< T > scheduler,
		     AnalyzerFactory< T > analyzerFactory ) {
	int numRequesters = scheduler.getNumRequesters();
	List< AnalyzerRunnable< T > > retval = 
	    new ArrayList< AnalyzerRunnable< T > >( numRequesters );
	for( int x = 0; x < numRequesters; x++ ) {
	    retval.add( new AnalyzerRunnable< T >( x,
						   scheduler,
						   analyzerFactory.makeAnalyzer() ) );
	}

	return retval;
    }
}
