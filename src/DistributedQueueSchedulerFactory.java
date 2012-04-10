package parallel.cluster;

import java.io.*;
import java.util.List;

import parallel.smp.*;

public class DistributedQueueSchedulerFactory implements SchedulerFactory< File > {
    // begin instance variables
    public final String hostname;
    // end instance variables

    public DistributedQueueSchedulerFactory( String hostname ) {
	this.hostname = hostname;
    }

    public Scheduler< File > makeScheduler( int numRequesters,
					    List< File > items ) {
	try {
	    return new DistributedQueueScheduler( numRequesters,
						  items,
						  hostname );
	} catch ( IOException e ) {
	    e.printStackTrace();
	    System.err.println( e );
	}
	return null;
    }
}
