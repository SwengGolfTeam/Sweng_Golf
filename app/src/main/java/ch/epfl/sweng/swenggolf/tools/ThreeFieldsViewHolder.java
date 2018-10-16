package ch.epfl.sweng.swenggolf.tools;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ThreeFieldsViewHolder extends RecyclerView.ViewHolder {

    private View title;
    private View subTitle;
    private View mainContent;

    /**
     * Creates a holder with the ids for the Views to use to display.
     *
     * @param container   the View that will contain the elements
     * @param title       the id of the title View
     * @param subTitle    the id of the subtitle View
     * @param mainContent the id of the content view
     */
    public ThreeFieldsViewHolder(View container, int title, int subTitle, int mainContent) {
        super(container);
        this.title = container.findViewById(title);
        this.subTitle = container.findViewById(subTitle);
        this.mainContent = container.findViewById(mainContent);
    }

    /**
     * Getter for the title.
     *
     * @return the view associated to the title
     */
    public View getTitle() {
        return title;
    }

    /**
     * Getter for the subtitle.
     *
     * @return the view associated to the subtitle
     */
    public View getSubTitle() {
        return subTitle;
    }

    /**
     * Getter for the main content.
     *
     * @return the view associated to the main content
     */
    public View getMainContent() {
        return mainContent;
    }

}
