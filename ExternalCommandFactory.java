/*
 * ExternalCommandFactory.java
 *
 */

package parallel.smp;

import java.io.*;

/**
 * Makes <code>ExternalCommandFileAnalyzer</code>s.
 * @author Kyle Dewey
 */
public class ExternalCommandFactory implements AnalyzerFactory< File > {
    // begin instance variables
    private String command; // the command to use
    // end instance variables

    /**
     * Creates a factory that will initialize 
     * <code>ExternalCommandFileAnalyzer</code>s with the given command
     * @param command The command to use for initialization of the 
     * <code>externalCommandFileAnalyzer</code>s.
     */
    public ExternalCommandFactory( String command ) {
	this.command = command;
    }

    /**
     * Makes analyzers using the command passed in the constructor.
     * @return File analyzers based on the given command
     */
    public Analyzer< File > makeAnalyzer() {
	return new ExternalCommandFileAnalyzer( command );
    }
}
