package com.mycompany.chatsapp;

import java.util.regex.Pattern;

public class Login {
    private String userName;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    public boolean checkUserName(String userName) {
        return userName.contains("_") && userName.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) return true;
        boolean hasUpper = true;
        boolean hasNumber = true;
        boolean hasSpecialChar = true;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
        }
        return hasUpper && hasNumber && hasSpecialChar;
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        String regex = "^\\+\\d{1,3}\\d{7,10}$";
        return Pattern.matches(regex, cellPhoneNumber);
    }

    public String registerUser(String userName, String password, String cellPhoneNumber, String firstName, String lastName) {
        if (!checkUserName(userName)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        }
        this.userName = userName;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        return "User registered successfully.";
    }

    public boolean loginUser(String userName, String password) {
        return this.userName != null && this.userName.equals(userName) && this.password.equals(password);
    }

    public String returnLoginStatus(String userName, String password) {
        if (loginUser(userName, password)) {
            return "Welcome " + firstName + " " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
    
    public String returnLoginStatus() {
        return "Welcome " + firstName + " " + lastName + " it is great to see you again.";
    }
}