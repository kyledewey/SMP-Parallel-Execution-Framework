package parallel.cluster;

import java.io.File;

import parallel.smp.*;

public class DistributedExternalCommandFactory implements AnalyzerFactory< File > {
    // begin instance variables
    private final String command;
    private final FileClient client;
    // end instance variables

    public DistributedExternalCommandFactory( String command,
					      FileClient client ) {
	this.command = command;
	this.client = client;
    }

    public Analyzer< File > makeAnalyzer() {
	return new DistributedExternalCommandFileAnalyzer( command, client );
    }
}
