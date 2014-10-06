Simple framework for executing massively parallel tasks.  It supports
pluggable tasks and pluggable task schedulers.  By default, it allows
for parallel console commands scheduled in a queue, where the number of
tasks running in parallel is equal to the number of available cores.

### Compilation ###

```console
make
```

### SMP Usage ###
`FileAnalysisFramework` works like so (executed from the same directory where everything was made):

```console
java parallel.smp.FileAnalysisFramework [-cores #cores to use] "command that takes a file name" files-to-process
```

...for example:

```console
java parallel.smp.FileAnalysisFramework "./foo" file1.txt file2.txt file3.txt
```

The above command like will execute:

```
./foo file1.txt
./foo file2.txt
./foo file3.txt
```

These will be executed in parallel where possible (due to limitations of the
number of cores). Since output isn't captured, all output must be done to
files. More specifically, -cores specifies the number of threads to use, which
defaults to the number of cores available.

The default queue scheduler doesn't provide any guarantees regarding execution
order. While it is true that file `N` must be selected for processing before
file `N + 1`, file `N + 1` may end up being processed first depending on how the
JVM schedules threads. If any guarentees are needed regarding execution order,
it will be neccessary to implement your own scheduler.

### Cluser Usage ###
There is a master/slave architecture with a very basic protocol for exchanging
files. The master serves input files, receives output files, and keeps track
of which files have been analyzed. If the master dies, then the analysis comes
to a halt. If a client dies, this is not a huge issue; this won't bring the
master down and any file that the client was analyzing will be ditributed again
later.

The master can be started like so:

```console
java parallel.cluster.FileServer files
```

...this will set up analysis to work with the given files.  The command will
block until all files have been analyzed.

Clients can be started like so:

```console
java parallel.cluster.DistributedFileAnalysisFramework master_address command
```

...where master_address is the IP or DNS-assigned name for the master. The 
command has additional constraints placed upon it, since output files are
sent to the master.  The command is given a filename to analyze, and it MUST
produce an output file of the form `filenamegiven.output` in the SAME directory
as the input file. The given filename will be a complete path. Note that
at the time of this writing, filenames are NOT preserved, so the analysis
cannot be sensitive to filenames.
