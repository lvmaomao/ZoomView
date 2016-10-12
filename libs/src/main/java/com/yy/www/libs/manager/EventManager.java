package com.yy.www.libs.manager;

public class EventManager {
    private static Event mEvent;

    public static void setEventListener(Event nm) {
        mEvent = nm;
    }

    public static void raiseEvent(int positon) {
        mEvent.onSomethingHappened(positon);
    }
}  