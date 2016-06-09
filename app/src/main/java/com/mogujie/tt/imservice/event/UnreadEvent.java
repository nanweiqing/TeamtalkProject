package com.mogujie.tt.imservice.event;

import com.mogujie.tt.imservice.entity.UnreadEntity;

/**
 * @author : yingmu on 15-1-6.
 * @email : yingmu@mogujie.com.
 */
public class UnreadEvent {

    public UnreadEntity entity;
    public Event event;
    public String sessionKey;

    public UnreadEvent(){}
    public UnreadEvent(Event e){
        this.event = e;
    }
    public UnreadEvent(Event e,String sessionKey){
        this.event = e;
        this.sessionKey = sessionKey;
    }

    public enum Event {
        UNREAD_MSG_LIST_OK,
        UNREAD_MSG_RECEIVED,

        SESSION_READED_UNREAD_MSG
    }
}
