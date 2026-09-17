package com.mycompany.chatsapp;

import java.util.regex.Pattern;

// Login class - handles registration and login for the chat app
public class Login {

    private String userName;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
    private boolean isLoggedIn;

    public Login() {
        isLoggedIn = false;
    }

    // username needs an underscore and can't be longer than 5 chars 
    public boolean checkUserName(String userName) {
        if (userName == null) {
            return false;
        }

        if (userName.contains("_") && userName.length() <= 5) {
            return true;
        }

        return false;
    }

    // password needs 8+ chars, a capital, a number and a special character
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasNumber = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecialChar = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();

        if (hasCapital && hasNumber && hasSpecialChar) {
            return true;
        } else {
            return false;
        }
    }

    // checks for a country code (like +27) then up to 10 more digits
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }

        String regex = "^\\+\\d{1,3}\\d{1,10}$";

        return Pattern.matches(regex, cellPhoneNumber);
    }

    // runs all the checks and either registers the user or tells them what's wrong
    public String registerUser(String userName, String password, String cellPhoneNumber, String firstName, String lastName) {

        if (checkUserName(userName) == false) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (checkPasswordComplexity(password) == false) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (checkCellPhoneNumber(cellPhoneNumber) == false) {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }

        // all good, save the details
        this.userName = userName;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;

        return "User registered successfully.";
    }

    // checks the entered username/password against what was saved during registration
    public boolean loginUser(String userName, String password) {
        if (this.userName != null && this.userName.equals(userName) && this.password != null && this.password.equals(password)) {
            isLoggedIn = true;
        } else {
            isLoggedIn = false;
        }

        return isLoggedIn;
    }

    // just returns the right message depending on if loginUser worked or not
    public String returnLoginStatus() {
        if (isLoggedIn) {
            return "Welcome " + firstName + "," + lastName + " it is great to see you.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
}