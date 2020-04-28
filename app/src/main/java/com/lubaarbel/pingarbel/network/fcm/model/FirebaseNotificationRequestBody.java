package com.lubaarbel.pingarbel.network.fcm.model;

public class FirebaseNotificationRequestBody {
    private Notification notification;
    private Data data;
    private String to; // /topics/input
    private String priority; // high

    public FirebaseNotificationRequestBody(Notification notification,
                                           Data data,
                                           String to,
                                           String priority) {
        this.notification = notification;
        this.data = data;
        this.to = to;
        this.priority = priority;
    }
}
