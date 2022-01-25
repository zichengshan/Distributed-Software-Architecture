### System A
#### Steps to set up and run the code
1. Open terminal and type "cd System_A" to change the working dictionary to System_A
2. Type "javac *.java" to compile: create the class files
3. Type "java Plumber_A" to execute the code

#### Description:
Two filters are used in SystemA as follows:
1. SourceFilter_A
   - Reads data from a file, and then writes data to its output port (here is SinkFilter_A)
2. SinkFilter_A
   - Reads data from the input file (here is SourceFilter_A) and writes the 
     output from this filter to a CSV file in specific format
3. No middleFilter is needed in SystemA