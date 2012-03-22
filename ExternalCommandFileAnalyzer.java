/*
 * ExternalCommandFileAnalyzer.java
 *
 */

package parallel.smp;

import java.io.*;

/**
 * An analyzer that looks at files using an external command.
 * @author Kyle Dewey
 */
public class ExternalCommandFileAnalyzer implements FileAnalyzer {
    // begin instance variables
    private String command;
    // end instance variables

    /**
     * Creates a new command analyzer with the given command.
     * @param command The command to use
     */
    public ExternalCommandFileAnalyzer( String command ) {
	this.command = command;
    }

    /**
     * Does analyses on the given file.
     * Replaces all instances of <code>FILE_VARIABLE</code> in the command
     * with the given file's name, escaping the name with single quotes.
     * It then executes the resulting command.
     * @param file The file to analyze
     */
    public void doAnalysis( File file ) {
	try {
	    Process process = 
		Runtime.getRuntime()
		.exec( new String[]{ command, file.getPath() } );
	    process.waitFor();
	    process.getInputStream().close();
	    process.getOutputStream().close();
	    process.getErrorStream().close();
	} catch( InterruptedException e ) {
	    System.err.println( e );
	} catch ( IOException e ) {
	    System.err.println( e );
	}
    }
}
