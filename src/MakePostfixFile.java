package parallel.cluster;

import java.io.File;

public class MakePostfixFile implements ToOutputFile {
    // begin instance variables
    public final String postfix;
    // end instance variables

    public MakePostfixFile( String postfix ) {
	this.postfix = postfix;
    }

    public MakePostfixFile() {
	this( DistributedExternalCommandFileAnalyzer.POSTFIX );
    }

    public File getOutputFile( File inputFile ) {
	return new File( inputFile.getPath() + postfix );
    }
}
