package com.mycompany.chatsapp;

import java.util.Scanner;

//registRegistration and login
public class Main {
    
   public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);
       Login login = new Login();
        
       System.out.println("=== Chat App Registration ==");
        
       System.out.println("Enter first name: ");
       String firstName = scanner.nextLine();
        
       System.out.println("Enter last name: ");
       String lastName = scanner.nextLine();
        
       System.out.println("Enter username (must contain an underscore and be no more than 5 characters): ");
       String userName = scanner.nextLine();
        
       System.out.println("Enter password (min 8 characters, a capital letter, a number, and a special character): ");
       String password = scanner.nextLine();
        
       System.out.println("Enter South African cell phone number (e.g. +27838098390): ");
       String cellPhoneNumber = scanner.nextLine();
        
       String registrationMessage = login.registerUser(userName, password, cellPhoneNumber, firstName, lastName);
       System.out.println();
       System.out.println(registrationMessage);
        
       //only let then log in if registration actually went through
       if (registrationMessage.equals("User registrated successfully.")) {
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
   }
}
        
        
        
        
 
