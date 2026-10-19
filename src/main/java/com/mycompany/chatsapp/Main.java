package com.mycompany.chatsapp;

import java.util.Scanner;
 
// Console app that ties Login (Part 1) and Message (Part 2) together
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
 
        // ---------- Part 2: QuickChat ----------
        System.out.println();
        System.out.println("Welcome to QuickChat.");
 
        int numberOfMessages = readNumber(scanner, "How many messages do you want to enter? ");
        int messagesEntered = 0;
 
        boolean quit = false;
 
        while (!quit) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
 
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
            } else {
                System.out.println("Please choose 1, 2 or 3.");
            }
        }
 
        scanner.close();
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
