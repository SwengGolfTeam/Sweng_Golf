package ch.epfl.sweng.swenggolf.offer.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * TouchListener which handles click on ListOffer.
 */
public class ListOfferTouchListener implements RecyclerView.OnItemTouchListener {

    private final OnItemClickListener mListener;
    private final GestureDetector mGestureDetector;

    /**
     * Constructs a ListOfferTouchListener, used to handle touch actions on the
     * RecyclerView of the Offers.
     *
     * @param context      the context
     * @param recyclerView the RecyclerView used
     * @param listener     the click listener
     */
    public ListOfferTouchListener(Context context,
                                  final RecyclerView recyclerView,
                                  OnItemClickListener listener) {
        mListener = listener;

        mGestureDetector =
                new GestureDetector(context,
                        new GestureDetector.SimpleOnGestureListener() {

                            @Override
                            public boolean onSingleTapUp(MotionEvent event) {
                                return true;
                            }

                            @Override
                            public void onLongPress(MotionEvent event) {
                                View child = recyclerView
                                        .findChildViewUnder(event.getX(), event.getY());
                                if (child != null && mListener != null) {
                                    mListener.onLongItemClick(child, recyclerView
                                            .getChildAdapterPosition(child));
                                }
                            }
                        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent event) {
        View childView = view.findChildViewUnder(event.getX(), event.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(event)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        // No specific gestures to be handled except for the click
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // No gestures to be intercepted before click
    }

    /**
     * ClickListener which can handle long and short click.
     */
    public interface OnItemClickListener {

        /**
         * Actions made when a single click is performed.
         *
         * @param view     the view
         * @param position the position of the item on the list.
         */
        void onItemClick(View view, int position);

        /**
         * Actions made when a long click is performed.
         *
         * @param view     the view
         * @param position the position of the item on the list.
         */
        void onLongItemClick(View view, int position);
    }
}