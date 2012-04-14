// like ExternalCommandFileAnalyzer, but it will send back a result file
// the result file must have the same prefix as the input file, with the
// defined postfix here

package parallel.cluster;

import java.io.*;

import parallel.smp.*;

public class DistributedExternalCommandFileAnalyzer extends ExternalCommandFileAnalyzer {
    // begin constants
    public static final String POSTFIX = ".output";
    // end constants

    // begin class variables
    // TODO: this assumes a certain order of requesters being created
    private static int requesterCount = 0;
    // end class variables

    // begin instance variables
    private FileClient client;
    private final int id;
    // end instance variables
    public DistributedExternalCommandFileAnalyzer( String command,
						   FileClient client ) {
	super( command );
	this.client = client;
	id = requesterCount;
	requesterCount++;
    }

    // TODO: if we have completed the file but we fail to send the
    // results, retry connection and resend. This requires a protocol
    // change. Currently we simply discard the results; the same file
    // will end up getting resent by the master later.
    public void doAnalysis( File file ) {
	super.doAnalysis( file );
	try {
	    client.transfer.sendFile( file.getPath() + POSTFIX );
	} catch ( IOException e ) {
	    e.printStackTrace();
	    System.err.println( "Client lost connection to the master. " +
				"Retrying connection..." );
	    try {
		client = 
		    DistributedExternalCommandFactory.makeFileClientAndRegister( client.hostname, id );
	    } catch ( IOException e1 ) {
		e1.printStackTrace();
		System.err.println( "Could not reconnect to the master. " +
				    "Terminating..." );
		System.exit( 1 );
	    }
	}
    }
}
