/*
 * FileAnalysisFramework.java
 *
 */

package parallel.smp;

import java.util.*;
import java.io.*;

/**
 * An analysis framework specific to external commands and files.
 * @author Kyle Dewey
 */
public class FileAnalysisFramework extends AnalysisFramework< File > {
    /**
     * Creates an analysis framework for the given files, using the 
     * given number of threads and the given command.
     * @param numThreads The number of threads to use
     * @param command The command to use
     * @param files The files to analyze
     * @param schedulerFactory The factory to use to make schedulers.
     */
    public FileAnalysisFramework( int numThreads,
				  String command,
				  List< File > files,
				  SchedulerFactory< File > schedulerFactory ) {
	super( numThreads,
	       files,
	       schedulerFactory,
	       new ExternalCommandFactory( command ) );
    }

    /**
     * Creates an analysis factory using the maximum available number 
     * of threads with the given command and files.
     * @param command The command to use
     * @param files The files to use
     */
    public FileAnalysisFramework( String command,
				  List< File > files ) {
	super( files,
	       new ExternalCommandFactory( command ) );
    }

    /**
     * Maps strings to files
     * @param strings The strings
     * @param index The index to start at
     * @return files based on those strings
     */
    public static List< File > stringsToFiles( List< String > strings,
					       int index ) {
	List< File > retval = new ArrayList< File >( strings.size() - index );
	for( int x = index; x < strings.size(); x++ ) {
	    retval.add( new File( strings.get( x ) ) );
	}

	return retval;
    }

    /**
     * The main function.
     * @param args Command line arguments.  The first is the command to run,
     * and the second is all the files.
     */
    public static void main( String[] args ) {
	if ( args.length < 2 ) {
	    System.err.println( "Needs a command and one or more file names." );
	    System.exit( 1 );
	}

	List< String > fileNames = Arrays.asList( args );
	FileAnalysisFramework framework = 
	    new FileAnalysisFramework( args[ 0 ],
				       stringsToFiles( fileNames, 1 ) );
	framework.doAnalysis();
    }
}
