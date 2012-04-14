/**
 * Here is how the (extremely basic) file transfer protocol works:
 * -Send a message saying how many bytes a file is with a specific
 *  format
 * -Actually send the file
 */

package parallel.cluster;

import java.io.*;

public class FileTransfer {
    // begin constants
    // protocol messages
    public static final String TEMP_FILE_PREFIX = "MOO";
    public static final String TEMP_FILE_POSTFIX = "COW";
    public static final String HAS_FILE_MSG_PREFIX = "HAS FILE OF ";
    public static final String HAS_FILE_MSG_POSTFIX = " BYTES";
    public static final String CLIENT_HELLO = "Hi! I'm a client!";
    public static final String NO_MORE_FILES_MSG = "OUT OF FILES";

    // everything else
    public static final int BUFFER_SIZE = 1024;
    // end constants

    // begin instance variables
    private final InputStream input;
    private final OutputStream output;
    // end instance variables

    public FileTransfer( InputStream input, OutputStream output ) {
	this.input = new BufferedInputStream( input ); 
	this.output = new BufferedOutputStream( output );
    }

    public static boolean isHasFileMessage( String message ) {
	return ( message.startsWith( HAS_FILE_MSG_PREFIX ) &&
		 message.endsWith( HAS_FILE_MSG_POSTFIX ) );
    }

    // returns -1 if the given message isn't that of a has file message
    public static long getFileSizeFromMessage( String message ) {
	long retval = -1;
	if ( isHasFileMessage( message ) ) {
	    try {
		message = message.substring( HAS_FILE_MSG_PREFIX.length(),
					     message.length() - HAS_FILE_MSG_POSTFIX.length() );
		retval = Long.parseLong( message );
	    } catch ( NumberFormatException e ) {}
	}
	return retval;
    }

    // The "correct" way to do this is with this:
    // Reader input = new InputStreamReader( is )...
    // However, it appears that the InputStreamReader class does some buffering
    // of its own that is completely off-limits, and the double-buffering
    // can trigger deadlock
    public static String readLine( InputStream input ) throws IOException {
	String retval = "";
	int currentInt;
	char currentChar;

	while ( ( currentInt = input.read() ) != -1 &&
		( currentChar = (char)currentInt ) != '\n' ) {
	    retval += currentChar;
	}

	if ( currentInt == -1 ) {
	    throw new BadMessageException( "Stream ended before message did." +
					   " Message so far: " + retval );
	}

	return retval;
    }

    public static String receiveMessage( InputStream input ) throws IOException {
	return readLine( input );
    }

    public String receiveMessage() throws IOException {
	return receiveMessage( input );
    }

    public static File makeTempFile() throws IOException {
	File retval = File.createTempFile( TEMP_FILE_PREFIX,
					   TEMP_FILE_POSTFIX );
	retval.deleteOnExit();
	return retval;
    }

    public void getAndSaveFile( File destination ) throws IOException {
	getAndSaveFile( receiveMessage(), destination );
    }

    public void getAndSaveFile( String message, File destination ) throws IOException {
	getAndSaveFile( input, message, destination );
    }

    public static void getAndSaveFile( InputStream input,
				       String message,
				       File destination ) throws IOException {
	long fileSize = -1;
	if ( ( fileSize = getFileSizeFromMessage( message ) ) != -1 ) {
	    getAndSaveFile( input, fileSize, destination );
	} else {
	    throw new BadMessageException( "Malformed message from server we are getting file from: \"" + 
					   message + "\"" );
	}
    }

    // assumes that amount < buffer.length
    public static void readChunk( byte[] buffer, 
				  InputStream input,
				  int amount ) throws IOException {
	int position = 0;
	int readRetval;
	while( position < amount ) {
	    readRetval = input.read( buffer, 
				     position,
				     amount - position );
	    if ( readRetval != -1 ) {
		position += readRetval;
	    } else {
		throw new IOException( "Stream ended prematurely" );
	    }
	}
    }
				     
	
    public static void getAndSaveFile( InputStream input,
				       long fileSize, 
				       File destination ) throws IOException {
	long bytesRead = 0;
	byte[] buffer = new byte[ BUFFER_SIZE ];
	BufferedOutputStream output = 
	    new BufferedOutputStream( new FileOutputStream( destination ) );
	while( bytesRead < fileSize ) {
	    int amount = Math.min( (int)( fileSize - bytesRead ),
				   buffer.length );
	    readChunk( buffer,
		       input,
		       amount );
	    output.write( buffer, 0, amount );
	    bytesRead += amount;
	}

	output.close();
    }

    public static long fileLength( String filename ) {
	return new File( filename ).length();
    }

    public void sendMessage( String message ) throws IOException {
	sendLine( message );
    }

    public void sendLine( String line ) throws IOException {
	output.write( ( line + "\n" ).getBytes() );
	output.flush();
    }

    public static String chomp( String s ) {
	if ( s.endsWith( "\n" ) ) {
	    s = s.substring( 0, s.length() - 1 );
	}
	return s;
    }

    public void sendFile( String filename ) throws IOException {
	sendMessage( HAS_FILE_MSG_PREFIX + Long.toString( fileLength( filename ) ) + 
		     HAS_FILE_MSG_POSTFIX );
	sendFileContents( filename, output );
    }

    public static void sendFileContents( String filename, 
					 OutputStream clientOutputStream ) throws IOException {
	BufferedInputStream input = new BufferedInputStream( new FileInputStream( filename ) );
	byte[] buffer = new byte[ BUFFER_SIZE ];
	long size = fileLength( filename );
	long bytesRead = 0;
	while ( bytesRead < size ) {
	    int amount = Math.min( (int)( size - bytesRead ),
				   buffer.length );
	    int tempBytesRead = input.read( buffer, 0, amount );
	    if ( tempBytesRead == -1 ) {
		throw new IOException( "Stream ended prematurely" );
	    } else {
		clientOutputStream.write( buffer, 0, tempBytesRead );
		bytesRead += tempBytesRead;
	    }
	}
	clientOutputStream.flush();
	input.close();
    }
}


    