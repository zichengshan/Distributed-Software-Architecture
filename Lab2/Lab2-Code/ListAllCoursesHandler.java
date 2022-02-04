/**
 * @(#)ListAllCoursesHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */

import java.util.ArrayList;


/**
 * "List all courses" command event handler.
 */
public class ListAllCoursesHandler extends CommandEventHandler {

    /**
     * Construct "List all courses" command event handler.
     *
     * @param objDataBase reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode output event code to send the command processing result
     */
    public ListAllCoursesHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
        super(objDataBase, iCommandEvCode, iOutputEvCode);
    }

    /**
     * Process "List all courses" command event.
     *
     * @param param a string parameter for command
     * @return a string result of command processing
     */
    protected String execute(String param) {
        // Get all course records.
        ArrayList vCourse = this.objDataBase.getAllCourseRecords();

        // Construct a list of course information and return it.
        String sReturn = "";
        for (int i=0; i<vCourse.size(); i++) {
            sReturn += (i == 0 ? "" : "\n") + ((Course) vCourse.get(i)).toString();
        }
        return sReturn;
    }
}