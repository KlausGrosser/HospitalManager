package com.hospital.hospitalmanagement.registration;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class EmailValidator
        implements Predicate<String> {

    @Override
    public boolean test(String email) {
        //TODO : Regex to validate email
        String regex = "^[a-z]*[a-z0-9.]+@[a-z0-9.]+\\.[a-z]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
