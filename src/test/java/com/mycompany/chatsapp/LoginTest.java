package com.mycompany.chatsapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//unit tests for Login class
public class LoginTest {
    
    //checkUserName tests
    @Test 
    public void testCheckUserName_correctlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkUserName("MML_10"));
    }
    
    @Test
    public void testCheckUserName_incorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkUserName("Mank!!!!!!"));
    }
    
    //checkPasswordComplexity tests
    @Test
    public void testCheckPasswordComplexity_MeetsRequirements() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Mankoana@10&&"));
    }
    
    @Test 
    public void testCheckPasswordComplexity_DoesNotRequirements() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password"));
    }
    
    //checkCellPhoneNumber tests
    @Test
    public void testCheckCellPhoneNumber_CorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkCellPhoneNumber("+27838098390"));
    }
    
    @Test 
    public void testCheckCellPhoneNumber_IncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("08098390"));
    }
    
    //registerUser tests
    @Test
    public void testRegisterUser_UsernameIncorrectlyFormatted() {
        Login login = new Login();
        String result = login.registerUser("Mank!!!!!!", "Mankoana@10&&", "+27838098390", "Mankoana", "Letsoalo");
        assertEquals("Password is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }
    
    @Test 
    public void testRegisterUser_PasswordDoesNotMeetComplexity() {
        Login login = new Login();
        String result = login.registerUser("MML_10", "password", "+27838098390", "Mankoana", "Letsoalo" );
        assertEquals("password is not correctly formatted ; please ensure that the password contains at least eigh characters, a capital letter, a number, and special character.", result);
    }
    
    @Test
    public void testRegisterUser_CellPhoneIncorrectlyFormatted() {
        Login login = new Login();
        String result = login.registerUser("MML_10", "Mankoana@10&&", "08966553", "Mankoana", "Letsoalo");
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.", result);
    }
 
    @Test
    public void testRegisterUser_Successful() {
        Login login = new Login();
        String result = login.registerUser("MML_10", "Mankoana@10&&", "+27838098390", "Mankoana", "Letsoalo");
        assertEquals("User registered successfully.", result);
    }
 
    // loginUser tests
    @Test
    public void testLoginUser_Successful() {
        Login login = new Login();
        login.registerUser("MML_10", "Mankoana@19&&", "+27838098390", "Mankoana", "Letsoalo");
        assertTrue(login.loginUser("MML_10", "Mankoana@10&&"));
    }
 
    @Test
    public void testLoginUser_Failed() {
        Login login = new Login();
        login.registerUser("MML_10", "Mankoana@10&&", "+27838098390", "Mankoana", "Letsoalo");
        assertFalse(login.loginUser("MML_10", "WrongPassword1!"));
    }
 
    // returnLoginStatus tests
    @Test
    public void testReturnLoginStatus_SuccessMessage() {
        Login login = new Login();
        login.registerUser("MML_10", "Mankoana@10&&", "+27838098390", "Mankoana", "Letsoalo");
        login.loginUser("MML_10", "Mankoana@10&&");
        assertEquals("Welcome Mankoana, Letsoalo it is great to see you.", login.returnLoginStatus());
    }
 
    @Test
    public void testReturnLoginStatus_FailureMessage() {
        Login login = new Login();
        login.registerUser("MML_10", "Mankoana@10&&", "+27838098390", "Mankoana", "Letsoalo");
        login.loginUser("MML_10", "WrongPassword1!");
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus());
    }
}