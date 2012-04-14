package parallel.cluster;

import java.util.*;
import java.io.*;

import parallel.smp.*;

public class DistributedExternalCommandFactory implements AnalyzerFactory< File > {
    // begin class variables
    // these are both REALLY hacky, and assume that requesters will be made
    // in order
    private static int requesterNum = 0;
    private static List< FileClient > requesterToClient =
	new ArrayList< FileClient >();
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

    public Analyzer< File > makeAnalyzer() {
	try {
	    FileClient client = new FileClient( hostname );
	    associateClient( client );
	    return new DistributedExternalCommandFileAnalyzer( command, client );
	} catch ( IOException e ) {
	    return null;
	}
    }

    private static void associateClient( FileClient client ) {
	requesterToClient.add( client );
    }

    // assumes that the requester is valid
    public static FileClient getClientForRequester( int requester ) {
	return requesterToClient.get( requester );
    }
}
