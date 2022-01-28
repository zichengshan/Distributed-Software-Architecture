### System B
#### Steps to set up and run the code
1. Find the Plumber_B.java file following the path: "src/main/java/Plumber_B.java"
2. Click the green triangle button and choose "Modify Run Configuration", and set the working dictionary to 
   the current java folder
3. Click the green triangle button and choose "Run 'Plumber_B.main()'"
4. OutputB.csv and WildPoint.csv will be generated in the java folder 

#### Description:
Three filters are used in SystemA as follows:
1. SourceFilter_B
    - Reads data from a file, and then writes data to its output port (here is SinkFilter_B)
2. MiddleFilter_B
   - write the records of wild jumps (with the original value, before replacement) to a text file called WildPoints.csv using the same format as System A
   - send the updated data stream to the Sink Filter. 
3. SinkFilter_B
    - Reads data from the input file (here is SourceFilter_B) and writes the
      output from this filter to a CSV file in specific format

#### Outcomes
1. 100 lines of information are generated in OutputB.csv
2. 21 lines of information are generated in WildPoint.csv
3. Have a better understanding of pipe-filter structure
