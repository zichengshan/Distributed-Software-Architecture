### QA:
1. LogHandler.java is added. This class subscribe to event "EV_SHOW". 
2. Add <code> LogHandler logHandler = new LogHandler(); </code> at line 88 in SystemMain.java

### QB:
1. In RegisterStudentHandler.java, From line 62 - 65, the number of students registered in a 
class is checked, if it is larger than the maximum capacity(set the maximum capacity to 3 for easier testing),
send a show event.

