## LAB2
### Steps to run the code
1. Compile if needed: ```javac *.java```
2. Run the code: ```java SystemMain Students.txt Courses.txt``` Or click the run button in SystemMain.java


### QA:
1. LogHandler.java is added. This class subscribe to event "EV_SHOW". 
2. Add ```LogHandler logHandler = new LogHandler();``` at [line 88] in SystemMain.java
3. All the outputs are saved in log.txt file

### QB:
1. In RegisterStudentHandler.java, From [line 62 - 65], the number of students registered in a 
class is checked, if it is larger than the maximum capacity(set the maximum capacity to 3 for easier testing),
send a show event.

### QC
1. CourseConflictHandler.java is added. It is subscribed to "EV_REGISTER_STUDENT". 
2. In SystemMain.java, we set CourseConflictHandler's output code as RegisterStudentHandler's input code.
   Supported by EventBus, RegisterStudentHandler access the registration based on the param received from CourseConflictHandler.
