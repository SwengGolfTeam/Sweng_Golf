package ch.epfl.sweng.swenggolf.tools;

/**
 * Tool class which checks properties.
 */
public final class Check {

    private Check() {

    }

    /**
     * Use this function to test if the length of the string is between minLength and maxLength.
     *
     * @param inputText the string to check the length
     * @param fieldName the name of the string checked
     * @param minLength the minimum length of the string
     * @param maxLength the maximum length of the string
     * @return the inputText
     * @throws IllegalArgumentException if the string does not fulfill the requirements
     */
    public static String checkString(String inputText, String fieldName, int minLength,
                                     int maxLength) {
        if (inputText == null) {
            throw new IllegalArgumentException(fieldName + "cannot be null");
        }
        int length = inputText.length();
        if (length > maxLength
                || length < minLength) {
            throw new IllegalArgumentException("The " + fieldName + " has not the good length. It"
                    + " should be between " + minLength + " and " + maxLength + ", but was"
                    + length + ".");
        }
        return inputText;
    }
}
