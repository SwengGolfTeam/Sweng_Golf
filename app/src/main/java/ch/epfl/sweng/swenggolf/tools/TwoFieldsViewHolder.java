package ch.epfl.sweng.swenggolf.tools;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


/**
 * Class used to represent a row in a RecyclerView.
 */
public class TwoFieldsViewHolder extends RecyclerView.ViewHolder {

    private TextView fieldOne;
    private TextView fieldTwo;
    private View container;

    /**
     * Creates a holder with the ids for the Views to use to display.
     *
     * @param container the View that will contain the elements
     * @param fieldOne  the id of the fieldOne View
     * @param fieldTwo  the id of the subtitle View
     */
    public TwoFieldsViewHolder(View container, int fieldOne, int fieldTwo) {
        super(container);
        this.container = container;
        this.fieldOne = container.findViewById(fieldOne);
        this.fieldTwo = container.findViewById(fieldTwo);

        // change font to custom one
        Typeface customFont = Typeface.createFromAsset(itemView.getContext().getAssets(),
                "fonts/niramit_medium.ttf");
        this.fieldOne.setTypeface(customFont);
        this.fieldTwo.setTypeface(customFont);
    }

    public View getContainer() {
        return container;
    }

    /**
     * Getter for the fieldOne.
     *
     * @return the view associated to the fieldOne
     */
    public View getFieldOne() {
        return fieldOne;
    }

    /**
     * Getter for the subtitle.
     *
     * @return the view associated to the subtitle
     */
    public View getFieldTwo() {
        return fieldTwo;
    }
}

