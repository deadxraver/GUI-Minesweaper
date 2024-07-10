package exceptions;

import java.io.IOException;

public class InputException extends IOException {
    @Override
    public String getMessage() {
        return "Wrong move input";
    }
}
