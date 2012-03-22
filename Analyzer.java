/*
 * Analyzer.java
 *
 */

package parallel.smp;

/**
 * Defines something that can do analyses on objects.
 * @author Kyle Dewey
 */
public interface Analyzer< T > {
    /**
     * Performs analysis on the given object.
     * Note that analysis will be performed in parallel.
     * @param object The object to analyze
     */
    public void doAnalysis( T object );
}
