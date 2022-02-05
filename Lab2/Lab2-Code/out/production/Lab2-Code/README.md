### QA:
1. LogHandler.java is added. This class subscribe to event "EV_SHOW". 
2. Add <code> LogHandler logHandler = new LogHandler(); </code> at line 88 in SystemMain.java

### QB:
1. In RegisterStudentHandler.java, From line 62 - 65, the number of students registered in a 
class is checked, if it is larger than the maximum capacity(set the maximum capacity to 3 for easier testing),
send a show event.

### Qc
1. Add in SystemMain.java
    ```	 
         * Add another component, handler for course conflict check
            CourseConflictHandler objCommandEventHandler7 =
                    new CourseConflictHandler(
                            db,
                            EventBus.EV_REGISTER_STUDENT,
                            EventBus.EV_REGISTER_STUDENT_CONFLICT_CHECK);``` 
2. Modify in SystemMain.java
    ```	
             * Make changes here, make the RegisterStudentHandler get the output of CourseConflictHandler to be input
            RegisterStudentHandler objCommandEventHandler6 =
                new RegisterStudentHandler(
                    db,
                    EventBus.EV_REGISTER_STUDENT_CONFLICT_CHECK,
                    EventBus.EV_SHOW);```
3. Added in EventBus
   ```public static final int EV_REGISTER_STUDENT_CONFLICT_CHECK = 7; ``` 
4. Change in CourseConflictHandler.java
5. Change in RegisterStudentHandler.java