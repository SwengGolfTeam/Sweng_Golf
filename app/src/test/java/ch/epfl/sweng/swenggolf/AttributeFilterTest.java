package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.AttributeFilter;

import static org.junit.Assert.assertEquals;

public class AttributeFilterTest {

    private final String attribute = "attribute";
    private final String value = "value";

    @Test
    public void getAttributeWork() {
        AttributeFilter filter = new AttributeFilter(attribute, value);
        assertEquals(filter.getAttribute(), attribute);
    }

    @Test
    public void getValueWork() {
        AttributeFilter filter = new AttributeFilter(attribute, value);
        assertEquals(filter.getValue(), value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionWithNullValue() {
        new AttributeFilter(attribute, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionWithEmptyString() {
        new AttributeFilter("", value);
    }
}
