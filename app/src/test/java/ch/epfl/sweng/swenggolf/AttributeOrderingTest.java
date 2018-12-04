package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.AttributeOrdering;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AttributeOrderingTest {

    private final String attribute = "attribute";

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnNullAttribute() {
        AttributeOrdering.ascendingOrdering(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnEmptyAttribute() {
        AttributeOrdering.ascendingOrdering("", 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void numberOfElementShouldBePositive() {
        AttributeOrdering.ascendingOrdering(attribute, 0);
    }

    @Test
    public void getAttributeIsCorrect() {
        AttributeOrdering ordering = AttributeOrdering.ascendingOrdering(attribute, 1);
        assertThat(ordering.getAttribute(), is(attribute));
    }

    @Test
    public void getNumberOfElementsIsCorrect() {
        int numberOfElements = 6;
        AttributeOrdering ordering = AttributeOrdering.descendingOrdering(attribute, numberOfElements);
        assertThat(ordering.getNumberOfElements(), is(numberOfElements));
    }

    @Test
    public void isAscendingIsCorrect() {
        AttributeOrdering ordering = AttributeOrdering.ascendingOrdering(attribute, 5);
        assertTrue(ordering.isAscending());
        assertFalse(ordering.isDescending());
    }

    @Test
    public void isDescendingIsCorrect() {
        AttributeOrdering ordering = AttributeOrdering.descendingOrdering(attribute, 5);
        assertTrue(ordering.isDescending());
        assertFalse(ordering.isAscending());
    }

}
