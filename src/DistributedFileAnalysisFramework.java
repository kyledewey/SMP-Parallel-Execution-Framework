package parallel.cluster;

import java.io.*;
import java.util.ArrayList;

import parallel.smp.*;

public class DistributedFileAnalysisFramework extends AnalysisFramework< File > {
    public DistributedFileAnalysisFramework( int numThreads,
					     String command,
					     String hostname ) {
	super( numThreads,
	       new ArrayList< File >(), // dummy variable
	       new DistributedQueueSchedulerFactory(),
	       new DistributedExternalCommandFactory( command, hostname ) );
    }

    // takes the hostname and the command to run
    public static void main( String[] args ) {
	if ( args.length != 2 ) {
	    System.err.println( "Needs a hostname and a command to run." );
	    System.exit( 1 );
	}

	DistributedFileAnalysisFramework framework =
	    new DistributedFileAnalysisFramework( AnalysisFramework.availableProcessors(),
						  args[ 1 ],
						  args[ 0 ] );
	framework.doAnalysis();
    }
}

											