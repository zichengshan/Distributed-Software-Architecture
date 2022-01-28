import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/******************************************************************************************************************
* File:MiddleFilter.java
* Project: Lab 1
* Copyright:
*   Copyright (c) 2020 University of California, Irvine
*   Copyright (c) 2003 Carnegie Mellon University
* Versions:
*   1.1 January 2020 - Revision for SWE 264P: Distributed Software Architecture, Winter 2020, UC Irvine.
*   1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
* This class serves as an example for how to use the FilterRemplate to create a standard filter. This particular
* example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
* filter's output port.
* Parameters: None
* Internal Methods: None
******************************************************************************************************************/

public class MiddleFilter_B extends FilterFramework_B
{
			public static List<Double> alt = new ArrayList<>();                 // alt is used to save the current altitude and its two previous altitudes
			public static List<List<Byte>> altitude_list = new ArrayList<>();	// altitude_list is used to save the 8 bytes of altitude information and its two previous altitudes'
//			public static List<String[]> wildJump = new ArrayList<>();			// wildJump is used to save the original value of wild jump altitude
			public static List<String[]> Data = new ArrayList<>();
			boolean hi = false;
	public void run()
    {

		int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		int IdLength = 4;				// This is the length of IDs in the byte stream
		byte databyte;				    // This is the data byte read from the stream
		int bytesread = 0;				// This is the number of bytes read from the stream
		int byteswritten = 0;           // This is the number of bytes written to the file
		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		int i;							// This is a loop counter
		double velocity = 0.0;		    // Used to store velocity
		double altitude = 0.0; 			// Used to store altitude
		double pressure = 0.0;			// Used to store pressure
		double temperature = 0.0; 		// Used to store temperature

		// Next we write a message to the terminal to let the world know we are alive...
		System.out.print( "\n" + this.getName() + "::Middle Reading ");
		Calendar TimeStamp = Calendar.getInstance();
		// Set the data format to "YYYY:DD:HH:MM:SS" style
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");

		iniData();
		while (true)
		{
			// Here we read a byte and write a byte
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

					// output
					WriteFilterOutputPort(databyte);
					byteswritten++;
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
				List<Byte> list = new ArrayList<>();
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

					// if id != 4, we have not met the altitude info, let databyte flow out of the filter without changing
					if(id != 2){
						WriteFilterOutputPort(databyte);
						byteswritten++;
					}else{
						// else, we need to save the bytes into the list for further check
						list.add(databyte);
					}
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
					}
				else if (id == 1){
					velocity = Double.longBitsToDouble(measurement);
					}
				else if( id == 3){
					pressure = Double.longBitsToDouble(measurement);
					}
				else if ( id == 4){
					temperature = Double.longBitsToDouble(measurement);
					if(hi == true)
						addData(TimeStampFormat.format(TimeStamp.getTime()), velocity, altitude, pressure, temperature);
					hi = false;
					}

				// if id == 2, we need to check whether this altitude is a wild jump
				// if it is a wild jump, save the altitude into wildJump list and write in WildPoint.csv and replace it with the average of the previous two altitudes
				else if(id == 2){
					long measurementCurrent = 0;
					// traverse the list to get the measurement
					for(int j = 0; j < list.size(); j++){
						measurementCurrent = measurementCurrent | (list.get(j) & 0xFF);
						if (j != MeasurementLength - 1)
							measurementCurrent = measurementCurrent << 8;
					}
					double altitudeCurrent = Double.longBitsToDouble(measurementCurrent);
					alt.add(altitudeCurrent);
					altitude_list.add(list);

					if(alt.size() == 2){
						if(Math.abs((alt.get(1) - alt.get(0))) > 100.0){
							altitude = alt.get(1);
							hi = true;
//							wildJump.add(new String[]{String.valueOf(alt.get(1))});
							alt.set(1,alt.get(0));
							altitude_list.set(1, altitude_list.get(0));
						}
					}
					else if (alt.size() == 3){
						if(Math.abs((alt.get(2) - alt.get(1))) > 100.0){
							altitude = alt.get(2);
							hi = true;
//							wildJump.add(new String[]{String.valueOf(alt.get(2))});
							double averageAltitude = (alt.get(1) + alt.get(0))/2;
							alt.set(2, averageAltitude);
							List<Byte> newList = new ArrayList<>();
							long measurementCurrent0 = 0;
							long measurementCurrent1 = 0;
							for (int j = 0; j < 8; j++){
								measurementCurrent0 = measurementCurrent0 | (altitude_list.get(0).get(j) & 0xFF);
								measurementCurrent1 = measurementCurrent1 | (altitude_list.get(1).get(j) & 0xFF);
								if (j != MeasurementLength - 1) {
									measurementCurrent0 = measurementCurrent0 << 8;
									measurementCurrent1 = measurementCurrent1 << 8;
								}
							}
							long newmeasurementCurrent = measurementCurrent0 + (measurementCurrent1-measurementCurrent0)/2;
							byte[] bytes= ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(newmeasurementCurrent).array();
							for(int x = 0; x < bytes.length; x++)
								newList.add(bytes[x]);
							altitude_list.set(2, newList);

						}
						//delete the first element to make sure the maximum size is 2
						alt.remove(0);
						altitude_list.remove(0);
					}
					int size = altitude_list.size();
					List<Byte> output = altitude_list.get(size-1);
					for(int j = 0; j < output.size(); j++){
						WriteFilterOutputPort(output.get(j));
						byteswritten++;
					}
				}
			}
			catch (EndOfStreamException e)
			{
				ClosePorts();
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
				break;
			}
		}// while
		try {
			csvWriter.writeWildJumpCsv(Data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
		Data.add(strings);
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
		Data.add(strings);
	}
}