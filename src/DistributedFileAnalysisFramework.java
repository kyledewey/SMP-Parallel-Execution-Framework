package parallel.cluster;

import java.util.*;
import java.io.*;

import parallel.smp.*;

public class DistributedFileAnalysisFramework extends AnalysisFramework< File > {
    public DistributedFileAnalysisFramework( int numThreads,
					     String command,
					     List< File > files,
					     String hostname ) {
	super( numThreads,
	       files,
	       new DistributedQueueSchedulerFactory(),
	       new DistributedExternalCommandFactory( command, hostname ) );
    }

    // takes the hostname, the command to run, and the files to process
    public static void main( String[] args ) {
	if ( args.length < 3 ) {
	    System.err.println( "Needs a hostname, a command to run, and the files to process." );
	    System.exit( 1 );
	}

	DistributedFileAnalysisFramework framework =
	    new DistributedFileAnalysisFramework( AnalysisFramework.availableProcessors(),
						  args[ 1 ],
						  FileAnalysisFramework.stringsToFiles( Arrays.asList( args ), 2 ),
						  args[ 0 ] );
	framework.doAnalysis();
    }
}

											