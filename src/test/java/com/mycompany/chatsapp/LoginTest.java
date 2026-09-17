package com.mycompany.chatsapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Unit tests for the Login class, using the test data given in the POE brief
public class LoginTest {

    // checkUserName tests
    @Test
    public void testCheckUserName_CorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkUserName("mml_1"));
    }

    @Test
    public void testCheckUserName_IncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkUserName("mank!!!!!!"));
    }

    // checkPasswordComplexity tests
    @Test
    public void testCheckPasswordComplexity_MeetsRequirements() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Mank0@na!10"));
    }

    @Test
    public void testCheckPasswordComplexity_DoesNotMeetRequirements() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password"));
    }

    // checkCellPhoneNumber tests
    @Test
    public void testCheckCellPhoneNumber_CorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCheckCellPhoneNumber_IncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    // registerUser tests
    @Test
    public void testRegisterUser_UsernameIncorrectlyFormatted() {
        Login login = new Login();
        String result = login.registerUser("mank!!!!!!", "Manko@na!10", "+27838968976", "Mankoana", "Letsoalo");
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }

    @Test
    public void testRegisterUser_PasswordDoesNotMeetComplexity() {
        Login login = new Login();
        String result = login.registerUser("mml_1", "password", "+27838968976", "Mankoana", "Letsoalo");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", result);
    }

    @Test
    public void testRegisterUser_CellPhoneIncorrectlyFormatted() {
        Login login = new Login();
        String result = login.registerUser("mml_1", "Manko@na!10", "08966553", "Mankoana", "Letsoalo");
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.", result);
    }

    @Test
    public void testRegisterUser_Successful() {
        Login login = new Login();
        String result = login.registerUser("mml_1", "Manko@na!10", "+27838968976", "Mankoana", "Letsoalo");
        assertEquals("User registered successfully.", result);
    }

    // loginUser tests
    @Test
    public void testLoginUser_Successful() {
        Login login = new Login();
        login.registerUser("mml_1", "Manko@na!10", "+27838968976", "Mankoana", "Letsoalo");
        assertTrue(login.loginUser("mml_1", "Manko@na!10"));
    }

    @Test
    public void testLoginUser_Failed() {
        Login login = new Login();
        login.registerUser("mml_1", "Manko@na!10", "+27838968976", "Mankoana", "Letsoalo");
        assertFalse(login.loginUser("kyl_1", "WrongPassword1!"));
    }

    // returnLoginStatus tests
    @Test
    public void testReturnLoginStatus_SuccessMessage() {
        Login login = new Login();
        login.registerUser("mml_1", "Manko@na!10", "+27838968976", "Mankoana", "Letsoalo");
        login.loginUser("mml_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Mankoana,Letsoalo it is great to see you.", login.returnLoginStatus());
    }

    @Test
    public void testReturnLoginStatus_FailureMessage() {
        Login login = new Login();
        login.registerUser("mml_1", "Manko@na!10", "+27838968976", "Mankoana", "Letsoalo");
        login.loginUser("mml_1", "WrongPassword1!");
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus());
    }
}