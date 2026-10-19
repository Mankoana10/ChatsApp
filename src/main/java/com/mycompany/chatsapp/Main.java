package com.mycompany.chatsapp;

import java.util.Scanner;

// Console app for Part 1 - registration and login
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

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

        // only let them log in if registration actually went through
        if (registrationMessage.equals("User registered successfully.")) {
            System.out.println();
            System.out.println("=== Login ===");

            System.out.print("Enter username: ");
            String loginUserName = scanner.nextLine();

            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            login.loginUser(loginUserName, loginPassword);
            System.out.println(login.returnLoginStatus());
        }

        scanner.close();
    
        //After login is successful
    
       System.out.println("How many messages do you want to send?");
       int numMessages = interger.parseInt(input.nextLine());
       
       for (int i = 0; i< numMessages; i++) {
           System.out.println("Enter recipient (+27...)");
           String recipient = input.nextLine();
           
           System.out.println("Enter message (max 250 chars)");
           String text = input.nextLine();
           
           //This is the connection
           message newMessage = new Message(recipient, text);
           
           //Use Message methods
           System.out.println(newMessage.checkMessageLength());
           if (!newMessage.checkRecipientCell()) {
               i--; //ask again
               continue;
           }
           
           System.out.println(newMessage.checkMessageLength());
           if (newMessage.getMessageText().length() > 250) {
               i--; 
               continue;
           }
           
           System.out.println(newMessage.createMessageHash());
           
           String option = JOptionPane. showInputDialog("1) send\n2) Disregard\n3) store");
           if (option.equals("1")) {
               newMessage.storeMessage();
               System.out,println(newMessage.printMessage());
           }
       }
       System.out.println("Total sent:  " + Message.returnTotalMessages());
    
    }
}