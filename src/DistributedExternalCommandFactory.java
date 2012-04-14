package parallel.cluster;

import java.util.*;
import java.io.*;

import parallel.smp.*;

public class DistributedExternalCommandFactory implements AnalyzerFactory< File > {
    // begin constants
    public static int NUM_CONNECTION_RETRIES = 10;
    public static int SCALING_SECONDS_BETWEEN_RETRY = 1;
    // end constants

    // begin class variables
    // these are both REALLY hacky, and assume that requesters will be made
    // in order
    private static int requesterNum = 0;
    private static Map< Integer, FileClient > requesterToClient =
	new HashMap< Integer, FileClient >();
    // end class variables

    // begin instance variables
    private final String command;
    private final String hostname;
    // end instance variables

    public DistributedExternalCommandFactory( String command,
					      String hostname ) {
	this.command = command;
	this.hostname = hostname;
    }

    // will retry if it fails
    public static FileClient makeFileClient( String hostname ) throws IOException {
	int attempt = 0;
	int scale = SCALING_SECONDS_BETWEEN_RETRY;
	FileClient retval = null;
	IOException toThrow = null;

	while( retval == null &&
	       attempt < NUM_CONNECTION_RETRIES ) {
	    try {
		retval = new FileClient( hostname );
	    } catch ( IOException e ) {
		toThrow = e;
		e.printStackTrace();
		System.err.println( "Client failed to connect to the server. " +
				    "Will retry in " + scale + " seconds." );
		try {
		    Thread.sleep( scale * 1000 );
		} catch ( InterruptedException e1 ) {}
		scale *= 2;
		attempt++;
	    }
	}

	if ( retval == null ) {
	    throw toThrow;
	} else {
	    return retval;
	}
    }
    
    // assumes that the given id
    public static FileClient makeFileClientAndRegister( String hostname, int id ) throws IOException {
	FileClient retval = makeFileClient( hostname );
	associateClient( retval, id );
	return retval;
    }

    public Analyzer< File > makeAnalyzer() {
	try {
	    FileClient client = makeFileClientAndRegister( hostname, nextId() );
	    return new DistributedExternalCommandFileAnalyzer( command, client );
	} catch ( IOException e ) {
	    return null;
	}
    }

    private static int nextId() {
	int retval = requesterNum;
	requesterNum++;
	return retval;
    }

    private static void associateClient( FileClient client, int id ) {
	requesterToClient.put( new Integer( id ), client );
    }

    // assumes that the requester is valid
    public static FileClient getClientForRequester( int requester ) {
	return requesterToClient.get( new Integer( requester ) );
    }
}
