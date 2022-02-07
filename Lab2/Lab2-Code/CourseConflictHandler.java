import java.util.ArrayList;
import java.util.StringTokenizer;

public class CourseConflictHandler extends CommandEventHandler {
    /**
     * Constructs a command event handler. At the time of creation, it subscribes to the given
     * command event by default.
     *
     * @param objDataBase    reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode  output event code to send the command processing result
     */
    public CourseConflictHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
        super(objDataBase, iCommandEvCode, iOutputEvCode);
    }

    @Override
    protected String execute(String param) {
        // Parse the parameters.
        StringTokenizer objTokenizer = new StringTokenizer(param);
        String sSID     = objTokenizer.nextToken();
        String sCID     = objTokenizer.nextToken();
        String sSection = objTokenizer.nextToken();

        // Get the student and course records.
        Student objStudent = this.objDataBase.getStudentRecord(sSID);
        Course objCourse = this.objDataBase.getCourseRecord(sCID, sSection);
        /**
         * 0: Invalid student ID
         * 1: Invalid course ID or course section
         * 2: Registration Conflicts
         * 3: No problem
         */
        if (objStudent == null) {
            return "0" + " " + sSID + " " + sCID + " " + sSection;
        }
        if (objCourse == null) {
            return "1" + " " + sSID + " " + sCID + " " + sSection;
        }
        // Check if the given course conflicts with any of the courses the student has registered.
        ArrayList vCourse = objStudent.getRegisteredCourses();
        for (int i=0; i<vCourse.size(); i++) {
            if (((Course) vCourse.get(i)).conflicts(objCourse)) {
                return "2" + " " + sSID + " " + sCID + " " + sSection;
            }
        }
        return "3" + " " + sSID + " " + sCID + " " + sSection;
    }
}
