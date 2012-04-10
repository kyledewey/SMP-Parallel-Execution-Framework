package parallel.cluster;

import java.io.IOException;

public class BadClientException extends IOException {
    public BadClientException( String message ) {
	super( message );
    }
    public BadClientException() {}
}
