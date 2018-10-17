package ch.epfl.sweng.swenggolf;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by dannyroa on 5/10/15.
 *
 * Source code: https://github.com/dannyroa/espresso-samples.
 *
 * Allows a simple selection on RecylerView for testing purposes.
 */
public class RecyclerViewMatcher {
    private final int recyclerViewId;
    private int position;
    private int targetViewId;

    public RecyclerViewMatcher(int recyclerViewId) {
        this.recyclerViewId = recyclerViewId;
    }

    /**
     * Enables the selection of an item on a RecyclerList.
     *
     * @param position the position of the item
     * @return the view matcher of the item, used by espresso
     */
    public Matcher<View> atPosition(final int position) {
        return atPositionOnView(position, -1);
    }

    /**
     * Enables the selection of an item on a RecyclerList.
     *
     * @param position     the position of the item
     * @param targetViewId the view on which the item is
     * @return the view matcher of the item, used by espresso
     */
    public Matcher<View> atPositionOnView(final int position, final int targetViewId) {
        this.position = position;
        this.targetViewId = targetViewId;
        return typeSafeMatcher;
    }

    private TypeSafeMatcher<View> typeSafeMatcher =
            new TypeSafeMatcher<View>() {
                Resources resources = null;
                View childView;

                public void describeTo(Description description) {
                    String idDescription = Integer.toString(recyclerViewId);
                    if (this.resources != null) {
                        try {
                            idDescription = this.resources.getResourceName(recyclerViewId);
                        } catch (Resources.NotFoundException var4) {
                            idDescription = String.format("%s (resource name not found)",
                                    Integer.valueOf(recyclerViewId));
                        }
                    }

                    description.appendText("with id: " + idDescription);
                }

                public boolean matchesSafely(View view) {
                    this.resources = view.getResources();
                    return checkChildView(view) && checkTargetView(view);
                }

                private boolean checkChildView(View view) {
                    if (childView == null) {
                        RecyclerView recyclerView = view.getRootView().findViewById(recyclerViewId);
                        if (recyclerView == null || recyclerView.getId() != recyclerViewId) {
                            return false;
                        }
                        childView = getChildView(recyclerView);
                    }
                    return true;
                }

                private boolean checkTargetView(View view) {
                    if (targetViewId == -1) {
                        return view == childView;
                    }
                    View targetView = childView.findViewById(targetViewId);
                    return view == targetView;
                }

                private View getChildView(RecyclerView recyclerView) {
                    return recyclerView.findViewHolderForAdapterPosition(position).itemView;
                }
            };
}