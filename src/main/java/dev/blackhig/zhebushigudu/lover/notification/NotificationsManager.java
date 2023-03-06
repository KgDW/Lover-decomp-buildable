package dev.blackhig.zhebushigudu.lover.notification;

import java.util.ArrayList;

public class NotificationsManager
{
    public ArrayList<Notification> notifications;
    
    public NotificationsManager() {
        this.notifications = new ArrayList<>();
    }
    
    public void add(final Notification noti) {
        noti.y = (float)(this.notifications.size() * 25);
        this.notifications.add(noti);
    }
    
    public void draw() {
        Notification remove = null;
        for (final Notification notification : this.notifications) {
            if (notification.x == 0.0f) {
                notification.in = !notification.in;
            }
            if (Math.abs(notification.x - notification.width) < 0.1 && !notification.in) {
                remove = notification;
            }
            if (notification.in) {
                notification.x = notification.animationUtils.animate(0.0f, notification.x, 0.1f);
            }
            else {
                notification.x = notification.animationUtils.animate(notification.width, notification.x, 0.10000000149011612);
            }
            notification.onRender();
        }
        if (remove != null) {
            this.notifications.remove(remove);
        }
    }
}
