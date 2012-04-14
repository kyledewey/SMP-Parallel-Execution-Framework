/**
 * A client that understands the protocol from FileServer.
 */

package parallel.cluster;

import java.net.*;
import java.io.*;

public class FileClient {
    // begin instance variables
    private final Socket server;
    public final FileTransfer transfer;
    private boolean done = false;
    private String lastFileName = null;
    public final String hostname;
    // end instance variables

    public FileClient( String hostname ) throws IOException {
	this.hostname = hostname;
	server = new Socket( hostname, FileServer.DEFAULT_PORT );
	transfer = new FileTransfer( server.getInputStream(),
				     server.getOutputStream() );
	handshake();
    }

    protected void handshake() throws IOException {
	transfer.sendMessage( FileTransfer.CLIENT_HELLO );
    }

    public void mainLoop() throws IOException {
	while ( !done ) {
	    processMessage( transfer.receiveMessage() );
	}
    }

    public void processMessage( String message ) throws IOException {
	long fileSize = -1;
	if ( message.equals( FileTransfer.NO_MORE_FILES_MSG ) ) {
	    done = true;
	} else {
	    File temp = FileTransfer.makeTempFile();
	    System.out.println( "CLIENT TEMP 1: " + temp.getPath() );
	    transfer.getAndSaveFile( message, temp );
	    transfer.sendFile( temp.getPath() );
	}
    }

    public static void main( String[] args ) throws IOException {
	new FileClient( "localhost" ).mainLoop();
    }
}

	