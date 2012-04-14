package parallel.cluster;

import java.io.*;
import java.util.List;

import parallel.smp.*;

public class DistributedQueueSchedulerFactory implements SchedulerFactory< File > {
    public Scheduler< File > makeScheduler( int numRequesters,
					    List< File > items ) {
	try {
	    return new DistributedQueueScheduler( numRequesters );
	} catch ( IOException e ) {
	    e.printStackTrace();
	    System.err.println( e );
	}
	return null;
    }
}
