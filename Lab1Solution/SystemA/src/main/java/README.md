### System A
#### Steps to set up and run the code
1. Find the Plumber_A.java file following the path: "src/main/java/Plumber_A.java"
2. Click the green triangle button and choose "Modify Run Configuration", and set the working dictionary to
   the current java folder
3. Click the green triangle button and choose "Run 'Plumber_A.main()'"
4. OutputA.csv will be generated in the java folder

#### Description:
Two filters are used in SystemA as follows:
1. SourceFilter_A
    - Reads data from a file, and then writes data to its output port (here is SinkFilter_A)
2. SinkFilter_A
    - Reads data from the input file (here is SourceFilter_A) and writes the
      output from this filter to a CSV file in specific format
* No middleFilter is needed in SystemA, because we do not need to make changes to the data stream

#### Outcomes
1. 100 lines of information are generated in OutputA.csv
3. Have a better understanding of pipe-filter structure