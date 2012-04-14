package parallel.cluster;

import java.io.File;

public interface ToOutputFile {
    // given an input file, it returns an output file
    public File getOutputFile( File inputFile );
}
