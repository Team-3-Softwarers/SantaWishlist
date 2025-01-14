/***************************************************************
*file: NotificationSystem.java
*authors: Sarkis Gafafyan, Damon Mapinda, Vincent Perez, 
*		  Marie Philavong, and Brandon Shippy
*class: CS 4800 - Software Engineering
*assignment: Final Project
*date last modified: 01/14/25
*
*purpose: This program contains the NotificationSystem class, 
*         which provides methods to send notifications to both 
*         children and Santa. The notifications are displayed 
*         using dialog boxes, allowing communication through a 
*         simple user interface.
*
****************************************************************/

import javax.swing.JOptionPane;

public class NotificationSystem
{

    /***************************************************************
    *function: feedbackToChild
    *purpose: Displays a notification to the child with a custom message.
    *         The message is shown in a dialog box with an 
    *         "Information" icon.
    ****************************************************************/
    public static void feedbackToChild(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Child Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    /***************************************************************
    *function: notifySanta
    *purpose: Displays a notification to Santa with a custom message.
    *         The message is shown in a dialog box with an 
    *         "Information" icon.
    ****************************************************************/
    public static void notifySanta(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Santa Notification", JOptionPane.INFORMATION_MESSAGE);
    }
}