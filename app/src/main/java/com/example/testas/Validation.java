package com.example.testas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static final String USERNAME_REGEX_PATTERN="^[a-zA-Z]{3,20}$";
    public static final String PASSWORD_REGEX_PATTERN="^[a-zA-Z]{3,20}$";

    public static boolean isValidUsername(String username){
        Pattern pattern=Pattern.compile(USERNAME_REGEX_PATTERN); //Sukuriamas šablonas pagal mūsų taisykles
        Matcher matcher=pattern.matcher(username); //Pagal susikurtą šabloną palyginami susikurti duomenys
        return matcher.find(); //Gražina true jeigu atitinka, false priešingu atveju
    }

    public static boolean isValidPassword(String password){
        Pattern pattern=Pattern.compile(PASSWORD_REGEX_PATTERN); //Sukuriamas šablonas pagal mūsų taisykles
        Matcher matcher=pattern.matcher(password); //Pagal susikurtą šabloną palyginami susikurti duomenys
        return matcher.find(); //Gražina true jeigu atitinka, false priešingu atveju
    }

}
