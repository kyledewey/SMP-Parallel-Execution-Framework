package parallel.cluster;

import java.io.*;

public class MakeTempFile implements ToOutputFile {
    public File getOutputFile( File inputFile ) {
	try {
	    File retval = FileTransfer.makeTempFile();
	    System.out.println( "TEMP FILE: " + retval.getPath() );
	    return retval;
	} catch ( IOException e ) {
	    e.printStackTrace();
	    System.err.println( e );
	    return null;
	}
    }
}
