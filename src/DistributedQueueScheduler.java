package parallel.cluster;

import parallel.smp.*;

import java.util.*;
import java.io.*;

public class DistributedQueueScheduler extends Scheduler< File > {
    public DistributedQueueScheduler( int numRequesters ) throws IOException {
	// pass a dummy list of files; these aren't used by anything distributed
	super( numRequesters, new ArrayList< File >() );
    }

    // returns a file to analyze
    public File nextItemWithoutValidation( int requester ) {
	File retval = null;
	try {
	    FileClient client = 
		DistributedExternalCommandFactory.getClientForRequester( requester );
	    String message = client.transfer.receiveMessage();
	    if ( !message.equals( FileTransfer.NO_MORE_FILES_MSG ) ) {
		retval = FileTransfer.makeTempFile();
		client.transfer.getAndSaveFile( message, retval );
	    }
	    return retval;
	} catch ( IOException e ) {
	    e.printStackTrace();
	    System.err.println( e );
	}

	return retval;
    }
}

	    