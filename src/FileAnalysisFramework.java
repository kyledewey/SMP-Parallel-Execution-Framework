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
     * Creates an analysis factory using the given number 
     * of threads with the given command and files.
     * @param command The command to use
     * @param files The files to use
     */
    public FileAnalysisFramework( int numThreads,
				  String command,
				  List< File > files ) {
	super( numThreads, 
	       files,
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

    public static void usage() {
	System.err.println( "Usage: java parallel.smp.FileAnalysisFramework [-cores #cores] filenames" );
	System.err.println( "Defaults to the total available number of cores." );
    }

    public static boolean hasCores( String[] args ) {
	return args.length >= 2 && args[ 0 ].equals( "-cores" );
    }

    public static int numCores( String[] args ) throws NumberFormatException {
	int retval;

	if ( hasCores( args ) ) {
	    retval = Integer.parseInt( args[ 1 ] );
	    if ( retval <= 0 ) {
		throw new NumberFormatException( "Needs a non-negative number of cores" );
	    }
	} else {
	    retval = AnalysisFramework.availableProcessors();
	}

	return retval;
    }

    public static String getCommand( String[] args ) {
	return ( hasCores( args ) ) ? args[ 2 ] : args[ 0 ];
    }

    public static List< File > getFiles( String[] args ) {
	List< String > fileNames = Arrays.asList( args );
	int index = ( hasCores( args ) ) ? 3 : 1;
	return stringsToFiles( fileNames, index );
    }

    public static boolean paramsValid( String[] args ) {
	boolean hasCores = hasCores( args );
	return ( ( hasCores && args.length > 3 ) || 
		 ( !hasCores && args.length > 1 ) );
    }

    /**
     * The main function.
     * @param args Command line arguments.  The first is the command to run,
     * and the second is all the files.
     */
    public static void main( String[] args ) {
	if ( !paramsValid( args ) ) {
	    usage();
	    System.exit( 1 );
	}

	try {
	    FileAnalysisFramework framework = 
		new FileAnalysisFramework( numCores( args ),
					   getCommand( args ),
					   getFiles( args ) );
	    framework.doAnalysis();
	} catch ( NumberFormatException e ) {
	    System.err.println( "The number of cores must be an integer > 0" );
	}
    }
}
