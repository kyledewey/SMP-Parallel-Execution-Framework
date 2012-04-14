/**
 * Thread for a FileServer.  Each client is backed by one thread.
 */

package parallel.cluster;

import java.io.*;
import java.net.*;

public class FileServerThread extends Thread {
    // begin instance variables
    private final FileServer server;
    private final Socket client;
    private final FileTransfer transfer;
    private final ToOutputFile toOutputFile;
    private boolean shouldTerminate = false;
    // end instance variables

    public FileServerThread( FileServer server, 
			     Socket client,
			     ToOutputFile toOutputFile ) throws IOException {
	this.toOutputFile = toOutputFile;
	this.server = server;
	this.client = client;
	transfer = new FileTransfer( client.getInputStream(),
				     client.getOutputStream() );
    }
    
    protected void handshake() throws IOException {
	if ( !transfer.receiveMessage().equals( FileTransfer.CLIENT_HELLO ) ) {
	    throw new BadClientException( "Malformed hello from client" );
	}
    }

    protected void sendFileOrNoMoreFiles( String fm ) throws IOException {
	if ( fm.equals( FileTransfer.NO_MORE_FILES_MSG ) ) {
	    transfer.sendMessage( fm );
	    shouldTerminate = true;
	} else {
	    transfer.sendFile( fm );
	    File output = toOutputFile.getOutputFile( new File( fm ) );
	    transfer.getAndSaveFile( output );
	    server.fileComplete( fm );
	}
    }

    // basic protocol: send a file, wait for the client to 
    // process it, get the output result from the client, and
    // repeat.
    public void run() {
	boolean error = false;
	try {
	    handshake();
	} catch ( BadClientException e ) {
	    e.printStackTrace();
	    System.err.println( "Bad handshake from client." );
	    error = true;
	} catch ( IOException e ) {
	    e.printStackTrace();
	    System.err.println( "Connection error on client handshake attempt." );
	    error = true;
	} finally {
	    if ( error ) 
		return;
	}

	while ( !shouldTerminate ) {
	    try {
		sendFileOrNoMoreFiles( server.nextFile() );
	    } catch ( IOException e ) {
		e.printStackTrace();
		System.err.println( "Error occurred in file transmission." );
		shouldTerminate = true; // protocol is to reconnect on error
	    }
	}

	server.checkDone();
    }
}
