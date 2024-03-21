package core;

import utils.PriorityMessage;

import javax.swing.*;
import java.util.PriorityQueue;

/**
 * A Singleton class that dispatches messages based on a PriorityQueue.
 */
public class MessageDispatcher {
    private static MessageDispatcher instance;
    private JTextArea textArea;
    private static final PriorityQueue<PriorityMessage> priorityMessageQueue = new PriorityQueue<>();

    private MessageDispatcher() { }

    public static synchronized MessageDispatcher getInstance() {
        if (instance == null) {
            instance = new MessageDispatcher();
        }
        return instance;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void createPriorityMessage(PriorityMessage priorityMessage) {
        synchronized (priorityMessageQueue) {
            priorityMessageQueue.add(priorityMessage);
        }
    }

    public void dispatchMessages() {
        SwingUtilities.invokeLater(() -> {
            synchronized (priorityMessageQueue) {
                while (!priorityMessageQueue.isEmpty()) {
                    PriorityMessage priorityMessage = priorityMessageQueue.poll();
                    textArea.append(priorityMessage.message() + "\n");
                }
            }
        });
    }
}

