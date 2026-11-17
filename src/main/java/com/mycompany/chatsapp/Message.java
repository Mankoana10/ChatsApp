package com.mycompany.chatsapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Message class - Part 2 (sending messages) and Part 3 (storing data and displaying reports)
public class Message {

    private static final String STORAGE_FILE = "storedMessages.json";

    // running totals shared by every Message object
    private static int messageCounter = 0;
    private static int totalMessagesSent = 0;
    private static boolean fileStorageEnabled = true;
    private static String sender = "Developer";

    // Part 3 arrays - filled while the program runs, nothing is hard-coded
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIds = new ArrayList<>();

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

    // extra constructor so tests (and the JSON loader) can control the ID and message number
    public Message(String messageId, String recipient, String messageText, int messageNumber) {
        this.messageId = messageId;
        this.recipient = recipient;
        this.messageText = messageText;
        this.thisMessageNumber = messageNumber;
    }

    private String generateMessageId() {
        SecureRandom random = new SecureRandom();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    
    // Part 2
    

    // message ID can't be more than ten characters
    public boolean checkMessageID() {
        return messageId != null && messageId.length() <= 10;
    }

    // needs a country code and no more than ten characters after it
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

    public boolean checkMessageLength() {
        return messageText != null && messageText.length() <= 250;
    }

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

    // first two digits of ID : message number : first word + last word, all caps
    public String createMessageHash() {
        String firstTwo = messageId.substring(0, 2);

        String[] words = messageText.trim().split("\\s+");
        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);

        messageHash = firstTwo + ":" + thisMessageNumber + ":" + firstWord + lastWord;
        return messageHash;
    }

    private String cleanWord(String word) {
        return word.replaceAll("[^a-zA-Z]", "").toUpperCase();
    }

    public String getMessageIdStatus() {
        return "Message ID generated: " + messageId;
    }

    // 1 = send, 2 = disregard, 3 = store
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                addToTrackingArrays();
                totalMessagesSent++;
                sentMessages.add(this);
                return "Message successfully sent.";
            case 2:
                addToTrackingArrays();
                disregardedMessages.add(this);
                return "Press 0 to delete the message.";
            case 3:
                addToTrackingArrays();
                storedMessages.add(this);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    // every message that gets processed has its hash and ID added to the arrays
    private void addToTrackingArrays() {
        if (messageHash == null) {
            createMessageHash();
        }
        messageHashes.add(messageHash);
        messageIds.add(messageId);
    }

    // returns everything that's been sent so far, one message per line
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.getDetails()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    // one message's details in the order the brief wants: ID, hash, recipient, message
    private String getDetails() {
        return "Message ID: " + messageId
                + ", Message Hash: " + messageHash
                + ", Recipient: " + recipient
                + ", Message: " + messageText;
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    
    // JSON storage
    

    // appends this message to the JSON file (one JSON object per line)
    public String storeMessage() {
        if (!fileStorageEnabled) {
            return "Message successfully stored.";
        }

        try (FileWriter writer = new FileWriter(STORAGE_FILE, true)) {
            writer.write(toJson() + System.lineSeparator());
            return "Message successfully stored.";
        } catch (IOException e) {
            return "Could not store message: " + e.getMessage();
        }
    }

    private String toJson() {
        return "{"
                + "\"messageId\":\"" + escape(messageId) + "\","
                + "\"hash\":\"" + escape(messageHash) + "\","
                + "\"recipient\":\"" + escape(recipient) + "\","
                + "\"message\":\"" + escape(messageText) + "\","
                + "\"number\":\"" + thisMessageNumber + "\""
                + "}";
    }

    // reads the JSON file back into the stored messages array (call once when the app starts)
    public static void loadStoredMessagesFromFile() {
        File file = new File(STORAGE_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String id = readJsonValue(line, "messageId");
                String hash = readJsonValue(line, "hash");
                String recipientValue = readJsonValue(line, "recipient");
                String text = readJsonValue(line, "message");
                String numberText = readJsonValue(line, "number");

                if (id.length() < 2 || text.isEmpty() || findMessageById(id) != null) {
                    continue;
                }

                int number = 0;
                try {
                    number = Integer.parseInt(numberText);
                } catch (NumberFormatException e) {
                    number = 0;
                }

                Message loaded = new Message(id, recipientValue, text, number);
                loaded.messageHash = hash.isEmpty() ? loaded.createMessageHash() : hash;

                storedMessages.add(loaded);
                messageHashes.add(loaded.messageHash);
                messageIds.add(loaded.messageId);
            }
        } catch (IOException e) {
            System.out.println("Could not read stored messages: " + e.getMessage());
        }
    }

    // pulls one value out of a line of JSON
    private static String readJsonValue(String line, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\":\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return unescape(matcher.group(1));
        }
        return "";
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescape(String value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length()) {
                sb.append(value.charAt(i + 1));
                i++;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // rewrites the whole file from the stored array (used after a delete)
    private static void rewriteStorageFile() {
        if (!fileStorageEnabled) {
            return;
        }

        try (FileWriter writer = new FileWriter(STORAGE_FILE, false)) {
            for (Message m : storedMessages) {
                writer.write(m.toJson() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Could not update stored messages file: " + e.getMessage());
        }
    }

    
    // Part 3 - Stored Messages menu features
    

    public static void setSender(String senderName) {
        sender = senderName;
    }

    // a. sender and recipient of every stored message
    public static String displaySenderAndRecipient() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }

        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(sender)
              .append(", Recipient: ").append(m.recipient)
              .append(System.lineSeparator());
        }
        return sb.toString();
    }

    // b. the longest stored message
    public static String getLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }

        String longest = "";
        for (Message m : storedMessages) {
            if (m.messageText.length() > longest.length()) {
                longest = m.messageText;
            }
        }
        return longest;
    }

    // looks through sent, disregarded and stored messages for an ID
    public static Message findMessageById(String id) {
        for (Message m : sentMessages) {
            if (m.messageId.equals(id)) {
                return m;
            }
        }
        for (Message m : disregardedMessages) {
            if (m.messageId.equals(id)) {
                return m;
            }
        }
        for (Message m : storedMessages) {
            if (m.messageId.equals(id)) {
                return m;
            }
        }
        return null;
    }

    // c. search for a message ID - returns the message text
    public static String searchByMessageId(String id) {
        Message found = findMessageById(id);
        if (found == null) {
            return "Message ID not found.";
        }
        return found.messageText;
    }

    // c. same search but with the recipient too, for showing on screen
    public static String searchByMessageIdDetails(String id) {
        Message found = findMessageById(id);
        if (found == null) {
            return "Message ID not found.";
        }
        return "Recipient: " + found.recipient + ", Message: " + found.messageText;
    }

    // d. every message sent or stored for one recipient
    public static List<String> searchByRecipient(String recipientNumber) {
        List<String> results = new ArrayList<>();

        for (Message m : sentMessages) {
            if (m.recipient.equals(recipientNumber)) {
                results.add(m.messageText);
            }
        }
        for (Message m : storedMessages) {
            if (m.recipient.equals(recipientNumber)) {
                results.add(m.messageText);
            }
        }
        return results;
    }

    // e. delete a message using its hash
    public static String deleteMessageByHash(String hash) {
        Message found = null;

        for (Message m : storedMessages) {
            if (m.messageHash.equalsIgnoreCase(hash)) {
                found = m;
                break;
            }
        }
        if (found == null) {
            for (Message m : sentMessages) {
                if (m.messageHash.equalsIgnoreCase(hash)) {
                    found = m;
                    break;
                }
            }
        }
        if (found == null) {
            for (Message m : disregardedMessages) {
                if (m.messageHash.equalsIgnoreCase(hash)) {
                    found = m;
                    break;
                }
            }
        }

        if (found == null) {
            return "Message hash not found.";
        }

        storedMessages.remove(found);
        sentMessages.remove(found);
        disregardedMessages.remove(found);
        messageHashes.remove(found.messageHash);
        messageIds.remove(found.messageId);
        rewriteStorageFile();

        // the brief's expected output has no full stop before the closing quote
        String text = found.messageText;
        if (text.endsWith(".")) {
            text = text.substring(0, text.length() - 1);
        }
        return "Message: \"" + text + "\" successfully deleted.";
    }

    // report of the sent messages: hash, recipient, message
    public static String displayReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages.";
        }

        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message Hash: ").append(m.messageHash)
              .append(", Recipient: ").append(m.recipient)
              .append(", Message: ").append(m.messageText)
              .append(System.lineSeparator());
        }
        return sb.toString();
    }

    // f. report of the full details of every stored message
    public static String displayStoredMessagesReport() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }

        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append(m.getDetails()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    
    // array getters (copies, so nobody changes the real arrays by accident)
    

    public static List<String> getSentMessageTexts() {
        return textsOf(sentMessages);
    }

    public static List<String> getDisregardedMessageTexts() {
        return textsOf(disregardedMessages);
    }

    public static List<String> getStoredMessageTexts() {
        return textsOf(storedMessages);
    }

    public static List<String> getMessageHashes() {
        return new ArrayList<>(messageHashes);
    }

    public static List<String> getMessageIds() {
        return new ArrayList<>(messageIds);
    }

    private static List<String> textsOf(List<Message> messages) {
        List<String> texts = new ArrayList<>();
        for (Message m : messages) {
            texts.add(m.messageText);
        }
        return texts;
    }

    
    // helpers for tests
    

    public static void setFileStorageEnabled(boolean enabled) {
        fileStorageEnabled = enabled;
    }

    // wipes the static counters and arrays between test cases
    public static void resetCounters() {
        messageCounter = 0;
        totalMessagesSent = 0;
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIds.clear();
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