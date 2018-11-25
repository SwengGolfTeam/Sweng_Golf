package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import static ch.epfl.sweng.swenggolf.tools.Check.checkString;

public class CheckTest {

    @Test(expected = IllegalArgumentException.class)
    public void checkStringExceptionOnSizeError() {
        checkString("is this less than 3 characters long ?", "error", 1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkStringExceptionOnNullInput() {
        checkString(null, "null", 1, 23);
    }
}
