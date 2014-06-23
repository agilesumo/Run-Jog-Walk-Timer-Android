package com.agilesumo.runjogwalk;

import android.widget.EditText;
import java.util.regex.Pattern;


public class Validation {
    private static final String EMPTY_ERROR_MESSAGE = "Workout Name cannot be empty";
    private static final String INVALID_CHARACTERS_MESSAGE ="Name contains invalid characters. Name can only contain letters, numbers and spaces";
    private static final String NAME_REGEX = "[a-zA-Z0-9 ]+";
	
	public static boolean hasText(EditText editText){
		 
        String text = editText.getText().toString().trim();
        editText.setError(null);
 
        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(EMPTY_ERROR_MESSAGE);
            return false;
        }
 
        return true;
	}

	public static boolean isValidName(EditText editText){
		 
        String text = editText.getText().toString().trim();
        editText.setError(null);
        
        // length 0 means there is no text
        if (!Pattern.matches(NAME_REGEX, text)) {
            editText.setError(INVALID_CHARACTERS_MESSAGE);
            return false;
        }
 
        return true;
	}
	
}
