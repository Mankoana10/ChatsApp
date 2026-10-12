import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
 
// Unit tests for the Message class, using the test data from the Part 2 brief
public class MessageTest {
 
    @BeforeEach
    public void resetBeforeEachTest() {
        // the counters are static, so reset them before every test
        Message.resetCounters();
    }
 
    // ----- checkMessageID() -----
 
    @Test
    public void testCheckMessageID_ValidLength() {
        Message message = new Message("0012345678", "+27718693002", "Hi Mike, can you join us for dinner tonight?", 0);
        assertTrue(message.checkMessageID());
    }
 
    @Test
    public void testCheckMessageID_TooLong() {
        Message message = new Message("00123456789999", "+27718693002", "Hi Mike, can you join us for dinner tonight?", 0);
        assertFalse(message.checkMessageID());
    }
 
    // ----- checkRecipientCell() -----
 
    @Test
    public void testCheckRecipientCell_CorrectlyFormatted() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }
 
    @Test
    public void testCheckRecipientCell_IncorrectlyFormatted() {
        Message message = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.", message.checkRecipientCell());
    }
 
    // ----- message length -----
 
    @Test
    public void testMessageLength_ReadyToSend() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", message.getMessageLengthStatus());
    }
 
    @Test
    public void testMessageLength_TooLong() {
        String longMessage = "a".repeat(260);
        Message message = new Message("+27718693002", longMessage);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", message.getMessageLengthStatus());
    }
 
    // ----- createMessageHash() -----
 
    @Test
    public void testCreateMessageHash_TestCase1() {
        // using a fixed message ID and message number so the hash is predictable
        Message message = new Message("0012345678", "+27718693002", "Hi Mike, can you join us for dinner tonight?", 0);
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }
 
    @Test
    public void testCreateMessageHash_LoopOverMultipleMessages() {
        // looping over a few messages and checking each hash follows the same pattern
        Message[] messages = {
            new Message("0012345678", "+27718693002", "Hi Mike, can you join us for dinner tonight?", 0),
            new Message("0098765432", "08575975889", "Hi Keegan, did you receive the payment?", 1)
        };
 
        String[] expectedHashes = { "00:0:HITONIGHT", "00:1:HIPAYMENT" };
 
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expectedHashes[i], messages[i].createMessageHash());
        }
    }
 
    // ----- Message ID created -----
 
    @Test
    public void testMessageIdStatus() {
        Message message = new Message("0012345678", "+27718693002", "Hi Mike, can you join us for dinner tonight?", 0);
        assertEquals("Message ID generated: 0012345678", message.getMessageIdStatus());
    }
 
    // ----- SentMessage() -----
 
    @Test
    public void testSentMessage_Send() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", message.SentMessage(1));
    }
 
    @Test
    public void testSentMessage_Disregard() {
        Message message = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", message.SentMessage(2));
    }
 
    @Test
    public void testSentMessage_Store() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", message.SentMessage(3));
    }
 
    // ----- Task 1 integration test -----
 
    @Test
    public void testTask1_OnlyOneMessageActuallySent() {
        Message message1 = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        message1.SentMessage(1); // Select Send
 
        Message message2 = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        message2.SentMessage(2); // Select Discard
 
        assertEquals(1, Message.returnTotalMessages());
    }
}
 