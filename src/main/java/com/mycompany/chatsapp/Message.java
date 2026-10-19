package com.mycompany.chatsapp;

import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

// Message class- handles sending, storing and printing messages
public class Message {

    // these are shared across every Message object, since we need running totals
    private static int messageCounter = 0;
    private static int totalMessagesSent = 0;
    private static List<String> sentMessages = new ArrayList<>();

    private String messageId;
    private String recipient;
    private String messageText;
    private String messageHash;
    private int thisMessageNumber;

    public Message(String recipient, String messageText) {
        this.messageId = generateMessageId();
        this.recipient = recipient;
        this.messageText = messageText;
        this.thisMessageNumber = messageCounter;
        messageCounter++;
    }

    // extra constructor mainly so we can control the ID and message number for testing
    // (normally the ID is random and the number is auto-assigned)
    public Message(String messageId, String recipient, String messageText, int messageNumber) {
        this.messageId = messageId;
        this.recipient = recipient;
        this.messageText = messageText;
        this.thisMessageNumber = messageNumber;
    }

    private String generateMessageId() {
        // just a random 10 digit number for tracking the message
        SecureRandom random = new SecureRandom();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    // message ID can't be more than ten characters
    public boolean checkMessageID() {
        return messageId != null && messageId.length() <= 10;
    }

    // same rule as the login cell number check - needs a country code and no more than ten characters
    public String checkRecipientCell() {
        if (recipient == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. "
                    + "Please correct the number and try again.";
        }

        String regex = "^\\+\\d{1,3}\\d{1,10}$";

        if (recipient.matches(regex)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. "
                    + "Please correct the number and try again.";
        }
    }

    // just a boolean version of the length check, handy for quick checks elsewhere
    public boolean checkMessageLength() {
        return messageText != null && messageText.length() <= 250;
    }

    // gives back the actual message the brief wants, with how many characters over the limit it is
    public String getMessageLengthStatus() {
        if (messageText == null) {
            return "Please enter a message of less than 250 characters.";
        }

        if (messageText.length() > 250) {
            int over = messageText.length() - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }

        return "Message ready to send.";
    }

    // builds the hash - first two digits of the message ID, then the message number, then the
    // first and last words of the message squashed together in caps
    public String createMessageHash() {
        String firstTwo = messageId.substring(0, 2);

        String[] words = messageText.trim().split("\\s+");
        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);

        messageHash = firstTwo + ":" + thisMessageNumber + ":" + firstWord + lastWord;
        return messageHash;
    }

    // strips out anything that isn't a letter and uppercases what's left
    private String cleanWord(String word) {
        return word.replaceAll("[^a-zA-Z]", "").toUpperCase();
    }

    // just confirms the ID was generated, in the wording the brief asks for
    public String getMessageIdStatus() {
        return "Message ID generated: " + messageId;
    }

    // handles what the user picked from the menu - 1 send, 2 disregard, 3 store
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                totalMessagesSent++;
                sentMessages.add(printSingleMessage());
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    // returns everything that's been sent so far, one message per line
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (String msg : sentMessages) {
            sb.append(msg).append(System.lineSeparator());
        }
        return sb.toString();
    }

    // lays out one message's details in the order the brief wants: ID, hash, recipient, message
    private String printSingleMessage() {
        return "Message ID: " + messageId
                + ", Message Hash: " + messageHash
                + ", Recipient: " + recipient
                + ", Message: " + messageText;
    }

    // how many messages have actually been sent (not stored or disregarded)
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // writes the message out to a JSON file so it isn't lost when the program closes
    public String storeMessage() {
        try (FileWriter writer = new FileWriter("storedMessages.json", true)) {
            String json = "{"
                    + "\"messageId\":\"" + messageId + "\","
                    + "\"recipient\":\"" + recipient + "\","
                    + "\"message\":\"" + messageText + "\""
                    + "}" + System.lineSeparator();
            writer.write(json);
            return "Message successfully stored.";
        } catch (IOException e) {
            return "Could not store message: " + e.getMessage();
        }
    }

    // used in tests to wipe the static counters between test cases
    public static void resetCounters() {
        messageCounter = 0;
        totalMessagesSent = 0;
        sentMessages.clear();
    }

    public String getMessageId() {
        return messageId;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public int getThisMessageNumber() {
        return thisMessageNumber;
    }
}