package ch.epfl.sweng.swenggolf.notification;

import android.view.View;

/**
 * Listener of the click on an item.
 */
public interface ItemClickListener {

    /**
     * Make an action when a click is performed.
     *
     * @param view     the view of the item
     * @param position the position of the item
     */
    void onClick(View view, int position);
}
