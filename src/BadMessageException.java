package parallel.cluster;

import java.io.IOException;

public class BadMessageException extends IOException {
    public BadMessageException( String message ) {
	super( message );
    }
    public BadMessageException() {}
}
