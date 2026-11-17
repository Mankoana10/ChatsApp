package com.mycompany.chatsapp;

import java.util.List;
import java.util.Scanner;

// Console app that ties Login (Part 1), Message (Part 2) and the Stored Messages features (Part 3) together
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        // ---------- Part 1: registration ----------
        System.out.println("=== Chat App Registration ===");

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter username (must contain an underscore and be no more than 5 characters): ");
        String userName = scanner.nextLine();

        System.out.print("Enter password (min 8 characters, a capital letter, a number, and a special character): ");
        String password = scanner.nextLine();

        System.out.print("Enter South African cell phone number (e.g. +27838968976): ");
        String cellPhoneNumber = scanner.nextLine();

        String registrationMessage = login.registerUser(userName, password, cellPhoneNumber, firstName, lastName);
        System.out.println();
        System.out.println(registrationMessage);

        // can't go any further if registration failed
        if (!registrationMessage.equals("User registered successfully.")) {
            scanner.close();
            return;
        }

        // ---------- Part 1: login ----------
        System.out.println();
        System.out.println("=== Login ===");

        System.out.print("Enter username: ");
        String loginUserName = scanner.nextLine();

        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();

        boolean loggedIn = login.loginUser(loginUserName, loginPassword);
        System.out.println(login.returnLoginStatus());

        // messages can only be sent if the user logged in successfully
        if (!loggedIn) {
            scanner.close();
            return;
        }

        // ---------- Part 2 and 3: QuickChat ----------
        System.out.println();
        System.out.println("Welcome to QuickChat.");

        // the logged in user is the sender, and any messages stored in the JSON file get loaded back in
        Message.setSender(login.getCellPhoneNumber());
        Message.loadStoredMessagesFromFile();

        int numberOfMessages = readNumber(scanner, "How many messages do you want to enter? ");
        int messagesEntered = 0;

        boolean quit = false;

        while (!quit) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");

            int option = readNumber(scanner, "Choose an option: ");

            if (option == 1) {
                if (messagesEntered >= numberOfMessages) {
                    System.out.println("You have already entered all " + numberOfMessages + " messages.");
                } else {
                    boolean counted = sendMessageFlow(scanner);
                    if (counted) {
                        messagesEntered++;
                    }
                }
            } else if (option == 2) {
                System.out.println("Coming Soon.");
            } else if (option == 3) {
                quit = true;
                System.out.println();
                System.out.println("Messages sent:");
                System.out.println(Message.printMessages());
                System.out.println("Total number of messages sent: " + Message.returnTotalMessages());
            } else if (option == 4) {
                storedMessagesMenu(scanner);
            } else {
                System.out.println("Please choose 1, 2, 3 or 4.");
            }
        }

        scanner.close();
    }

    // Part 3 - everything the user can do with the stored messages
    private static void storedMessagesMenu(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println();
            System.out.println("=== Stored Messages ===");
            System.out.println("1) Display the sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages to a particular recipient");
            System.out.println("5) Delete a message using the message hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("7) Back to main menu");

            int choice = readNumber(scanner, "Choose an option: ");

            if (choice == 1) {
                System.out.println(Message.displaySenderAndRecipient());
            } else if (choice == 2) {
                System.out.println(Message.getLongestStoredMessage());
            } else if (choice == 3) {
                System.out.print("Enter the message ID: ");
                String id = scanner.nextLine().trim();
                System.out.println(Message.searchByMessageIdDetails(id));
            } else if (choice == 4) {
                System.out.print("Enter the recipient number: ");
                String recipient = scanner.nextLine().trim();
                List<String> results = Message.searchByRecipient(recipient);

                if (results.isEmpty()) {
                    System.out.println("No messages found for that recipient.");
                } else {
                    for (String text : results) {
                        System.out.println("- " + text);
                    }
                }
            } else if (choice == 5) {
                System.out.print("Enter the message hash: ");
                String hash = scanner.nextLine().trim();
                System.out.println(Message.deleteMessageByHash(hash));
            } else if (choice == 6) {
                System.out.println(Message.displayStoredMessagesReport());
            } else if (choice == 7) {
                back = true;
            } else {
                System.out.println("Please choose a number from 1 to 7.");
            }
        }
    }

    // captures one message, validates it, then asks what to do with it
    // returns true if the message was completed (sent, disregarded or stored)
    private static boolean sendMessageFlow(Scanner scanner) {
        System.out.print("Enter recipient cell number (e.g. +27718693002): ");
        String recipient = scanner.nextLine().trim();

        System.out.print("Enter your message (250 characters max): ");
        String messageText = scanner.nextLine().trim();

        Message message = new Message(recipient, messageText);

        String recipientStatus = message.checkRecipientCell();
        System.out.println(recipientStatus);
        if (!recipientStatus.equals("Cell phone number successfully captured.")) {
            return false;
        }

        String lengthStatus = message.getMessageLengthStatus();
        System.out.println(lengthStatus);
        if (!lengthStatus.equals("Message ready to send.")) {
            return false;
        }

        if (messageText.isEmpty()) {
            System.out.println("Message can't be empty.");
            return false;
        }

        message.createMessageHash();
        System.out.println(message.getMessageIdStatus());

        System.out.println();
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");

        int choice = readNumber(scanner, "Choose an option: ");
        System.out.println(message.SentMessage(choice));

        // show the full details once it's been sent
        if (choice == 1) {
            System.out.println("Message ID: " + message.getMessageId());
            System.out.println("Message Hash: " + message.getMessageHash());
            System.out.println("Recipient: " + message.getRecipient());
            System.out.println("Message: " + message.getMessageText());
        }

        return choice >= 1 && choice <= 3;
    }

    // keeps asking until the user types a whole number, so bad input doesn't crash the app
    private static int readNumber(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}