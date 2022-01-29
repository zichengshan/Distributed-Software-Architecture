//public class SinkFilter_A {
//}
import sun.jvm.hotspot.utilities.Bits;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/******************************************************************************************************************
 * File:SinkFilterTemplate.java
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions:
 *	1.0 November 2008 - Initial rewrite of original assignment 1 (ajl).
 *
 * Description:
 *
 * This class serves as a template for creating sink filters. The details of threading, connections writing output
 * are contained in the FilterFramework super class. In order to use this template the program should rename the class.
 * The template includes the run() method which is executed when the filter is started.
 * The run() method is the guts of the filter and is where the programmer should put their filter specific code.
 * In the template there is a main read-write loop for reading from the input port of the filter. The programmer is
 * responsible for writing the data to a file, or device of some kind. This template assumes that the filter is a sink
 * filter that reads data from the input file and writes the output from this filter to a file or device of some kind.
 * In this case, only the input port is used by the filter. In cases where the filter is a standard filter or a source
 * filter, you should use the FilterTemplate.java or the SourceFilterTemplate.java as a starting point for creating
 * standard or source filters.
 *
 * Parameters: 		None
 *
 * Internal Methods:
 *
 *	public void run() - this method must be overridden by this class.
 *
 ******************************************************************************************************************/

public class SinkFilter_A extends FilterFramework_A
{
    public static List<String[]> streamData = new ArrayList<>();
    public static int count = 0;
    public void run()
    {
        /************************************************************************************
         *	TimeStamp is used to compute time using java.util's Calendar class.
         * 	TimeStampFormat is used to format the time value so that it can be easily printed
         *	to the terminal.
         *************************************************************************************/
        Calendar TimeStamp = Calendar.getInstance();
        // Set the data format to "YYYY:DD:HH:MM:SS" style
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:DD:HH:mm:ss");

        int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
        int IdLength = 4;				// This is the length of IDs in the byte stream
        byte databyte = 0;				// This is the data byte read from the stream
        int bytesread = 0;				// This is the number of bytes read from the stream
        long measurement;				// This is the word used to store all measurements - conversions are illustrated.
        int id;							// This is the measurement id
        int i;							// This is a loop counter
        double velocity = 0.0;		    // Used to store velocity
        double altitude = 0.0; 			// Used to store altitude
        double pressure = 0.0;			// Used to store pressure
        double temperature = 0.0; 		// Used to store temperature

        // Initialize the streamData, add a title
        iniData();

        // First we announce to the world that we are alive...
        System.out.print( "\n" + this.getName() + "::Sink Reading ");

        while (true)
        {
            try
            {
                /***************************************************************************
                 // We know that the first data coming to this filter is going to be an ID and
                 // that it is IdLength long. So we first get the ID bytes.
                 ****************************************************************************/
                id = 0;
                for (i=0; i<IdLength; i++ )
                {
                    databyte = ReadFilterInputPort();	// This is where we read the byte from the stream...
                    id = id | (databyte & 0xFF);		// We append the byte on to ID...
                    if (i != IdLength-1)				// If this is not the last byte, then slide the
                    {									// previously appended byte to the left by one byte
                        id = id << 8;					// to make room for the next byte we append to the ID
                    }
                    bytesread++;						// Increment the byte count
                }

                /****************************************************************************
                 // Here we read measurements. All measurement data is read as a stream of bytes
                 // and stored as a long value. This permits us to do bitwise manipulation that
                 // is neccesary to convert the byte stream into data words. Note that bitwise
                 // manipulation is not permitted on any kind of floating point types in Java.
                 // If the id = 0 then this is a time value and is therefore a long value - no
                 // problem. However, if the id is something other than 0, then the bits in the
                 // long value is really of type double and we need to convert the value using
                 // Double.longBitsToDouble(long val) to do the conversion which is illustrated below.
                 *****************************************************************************/
                measurement = 0;
                for (i=0; i<MeasurementLength; i++ )
                {
                    databyte = ReadFilterInputPort();
                    measurement = measurement | (databyte & 0xFF);	// We append the byte on to measurement...
                    if (i != MeasurementLength-1)					// If this is not the last byte, then slide the
                    {												// previously appended byte to the left by one byte
                        measurement = measurement << 8;				// to make room for the next byte we append to the
                        // measurement
                    }
                    bytesread++;									// Increment the byte count
                }

                /****************************************************************************
                 Based on the data stream format, The corresponding meanings of different id values are as follows:
                 id = 0 : Time
                 id = 1: Velocity
                 id = 2: Altitude
                 id = 3: Pressure
                 id = 4: Temperature
                 id = 5: Pitch (Not needed in SystemA)
                 ****************************************************************************/
                if ( id == 0 ){
                    TimeStamp.setTimeInMillis(measurement);
                    count++;}
                else if (id == 1){
                    velocity = Double.longBitsToDouble(measurement);
                    count++;}
                else if ( id == 2){
                    altitude = Double.longBitsToDouble(measurement);
                    count++;}
                else if( id == 3){
                    pressure = Double.longBitsToDouble(measurement);
                    count++;}
                else if ( id == 4){
                    temperature = Double.longBitsToDouble(measurement);
                    count++;}

                // Add this line of data
                if(count % 5 == 0)
                    addData(TimeStampFormat.format(TimeStamp.getTime()), velocity, altitude, pressure, temperature);

            }
            /*******************************************************************************
             *	The EndOfStreamExeception below is thrown when you reach end of the input
             *	stream. At this point, the filter ports are closed and a message is
             *	written letting the user know what is going on.
             ********************************************************************************/
            catch (EndOfStreamException e)
            {
                ClosePorts();
                System.out.print( "\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesread );
                break;
            }
        } // while

        try {
            csvWriter.writeCsv(streamData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // run

    /**
     * iniData() method is used to initialize the streamData
     */
    private static void iniData(){
        String[] strings = new String[5];
        strings[0] = "Time";
        strings[1] = "Velocity";
        strings[2] = "Altitude";
        strings[3] = "Pressure";
        strings[4] = "Temperature";
        streamData.add(strings);
    }

    /**
     * addData() method is used to add one line of data.
     * To make the main function clean
     * @param time
     * @param velocity
     * @param altitude
     * @param pressure
     * @param temperature
     */
    private static void addData(String time, double velocity, double altitude, double pressure, double temperature){
        String[] strings = new String[5];
        strings[0] = time;
        strings[1] = String.valueOf(velocity);
        strings[2] = String.valueOf(altitude);
        strings[3] = String.valueOf(pressure);
        strings[4] = String.valueOf(temperature);
        streamData.add(strings);
    }
} // FilterTemplate
