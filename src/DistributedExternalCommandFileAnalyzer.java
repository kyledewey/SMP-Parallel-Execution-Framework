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

    // begin instance variables
    private final FileClient client;
    // end instance variables
    public DistributedExternalCommandFileAnalyzer( String command,
						   FileClient client ) {
	super( command );
	this.client = client;
    }

    public void doAnalysis( File file ) {
	super.doAnalysis( file );
	try {
	    client.transfer.sendFile( file.getPath() + POSTFIX );
	} catch ( IOException e ) {
	    e.printStackTrace();
	    System.err.println( e );
	}
    }
}
