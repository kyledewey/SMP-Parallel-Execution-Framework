/*
 * AnalyzerFactory.java
 *
 */

package parallel.smp;

/**
 * Makes analyzers
 * @author Kyle Dewey
 */
public interface AnalyzerFactory< T > {
    /**
     * Makes an analyzer that can analyze objects of the given type
     * @return An analyzer for objects of the given type
     */
    public Analyzer< T > makeAnalyzer();
}
