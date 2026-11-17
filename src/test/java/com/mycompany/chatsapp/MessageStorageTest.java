package com.mycompany.chatsapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Part 3 unit tests - uses the five test messages from the brief to populate the arrays
public class MessageStorageTest {

    private static final String TEXT_1 = "Did you get the cake?";
    private static final String TEXT_2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String TEXT_3 = "Yohoooo, I am at your gate.";
    private static final String TEXT_4 = "It is dinner time!";
    private static final String TEXT_5 = "Ok, I am leaving without you.";

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @BeforeEach
    public void setUp() {
        Message.resetCounters();
        Message.setFileStorageEnabled(false); // don't touch the real JSON file while testing

        // IDs are fixed so the tests are predictable
        message1 = new Message("1000000001", "+27834557896", TEXT_1, 0);
        message2 = new Message("1000000002", "+27838884567", TEXT_2, 1);
        message3 = new Message("1000000003", "+27834484567", TEXT_3, 2);
        // message 4 only has the developer number 0838884567 in the brief, so it is used as the ID and recipient
        message4 = new Message("0838884567", "0838884567", TEXT_4, 3);
        message5 = new Message("1000000005", "+27838884567", TEXT_5, 4);

        message1.SentMessage(1);  // Sent
        message2.SentMessage(3);  // Stored
        message3.SentMessage(2);  // Disregard
        message4.SentMessage(1);  // Sent
        message5.SentMessage(3);  // Stored
    }

    @AfterEach
    public void tearDown() {
        Message.setFileStorageEnabled(true);
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList(TEXT_1, TEXT_4), Message.getSentMessageTexts());
    }

    @Test
    public void testDisregardedMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList(TEXT_3), Message.getDisregardedMessageTexts());
    }

    @Test
    public void testStoredMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList(TEXT_2, TEXT_5), Message.getStoredMessageTexts());
    }

    @Test
    public void testHashAndIdArraysPopulated() {
        assertEquals(5, Message.getMessageHashes().size());
        assertEquals(5, Message.getMessageIds().size());
    }

    @Test
    public void testDisplayLongestMessage() {
        assertEquals(TEXT_2, Message.getLongestStoredMessage());
    }

    @Test
    public void testSearchForMessageId() {
        assertEquals("It is dinner time!", Message.searchByMessageId("0838884567"));
    }

    @Test
    public void testSearchForMessageId_NotFound() {
        assertEquals("Message ID not found.", Message.searchByMessageId("9999999999"));
    }

    @Test
    public void testSearchMessagesForRecipient() {
        assertEquals(Arrays.asList(TEXT_2, TEXT_5), Message.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        String hash = message2.getMessageHash();

        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time\" successfully deleted.",
                Message.deleteMessageByHash(hash));

        // and it should really be gone from the stored array
        assertEquals(Arrays.asList(TEXT_5), Message.getStoredMessageTexts());
    }

    @Test
    public void testDeleteMessageUsingHash_NotFound() {
        assertEquals("Message hash not found.", Message.deleteMessageByHash("NOPE"));
    }

    @Test
    public void testDisplayReport() {
        String report = Message.displayReport();

        assertTrue(report.contains(message1.getMessageHash()));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains(TEXT_1));
        assertTrue(report.contains(message4.getMessageHash()));
        assertTrue(report.contains(TEXT_4));
        assertFalse(report.contains(TEXT_2));
    }

    @Test
    public void testStoredMessagesReport() {
        String report = Message.displayStoredMessagesReport();

        assertTrue(report.contains(message2.getMessageId()));
        assertTrue(report.contains(message2.getMessageHash()));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains(TEXT_5));
    }
}