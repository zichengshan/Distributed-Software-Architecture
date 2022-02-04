/**
 * @(#)ClientOutput.java
 *
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 *
 */


import java.util.Observable;
import java.util.Observer;


/**
 * This class represents a client output component which is responsible for displaying text messages
 * onto the console upon receiving show events. Show events are expected to carry a 
 * <code>String</code> object as its parameter that is to be displayed. This component need to
 * subscribe to those events to receive them, which is done at the time of creation.
 *
 * @author Jung Soo Kim
 * @version 1.0
 */
public class ClientOutput implements Observer {

    /**
     * Constructs a client output component. A new client output component subscribes to show events
     * at the time of creation.
     */
    public ClientOutput() {
        // Subscribe to SHOW event.
        EventBus.subscribeTo(EventBus.EV_SHOW, this);
    }

    /**
     * Event handler of this client output component. On receiving a show event, the attached
     * <code>String</code> object is displayed onto the console.
     *
     * @param event an event object. (caution: not to be directly referenced)
     * @param param a parameter object of the event. (to be cast to appropriate data type)
     */
    public void update(Observable event, Object param) {
        // Display the event parameter (a string) onto stdout.
        System.out.println((String) param);
    }
}
