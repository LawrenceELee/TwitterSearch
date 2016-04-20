package com.example.lawrence.twittersearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// subclass of Recycler.Adapter for binding data to RecyclerView items
public class SearchesAdapter
        extends RecyclerView.Adapter<SearchesAdapter.ViewHolder> {

    // listeners from MainActivity that are registered for each list item
    private final View.OnClickListener mClickListener;
    private final View.OnLongClickListener mLongClickListener;

    // list to contain RecyclerView items' data (search tags)
    private final List<String> mTags;

    public SearchesAdapter(List<String> tags, View.OnClickListener clickListener,
                           View.OnLongClickListener longClickListener) {

        mTags = tags;
        mClickListener = clickListener;
        mLongClickListener = longClickListener;
    }

    // set up new list item and its ViewHolder
    // when RecyclerView (the list) is created: inflate layout,
    // wrap each list item in a ViewHolder, and return ViewHolder for display.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the list_item layout for each list item.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        // wrap list item in a ViewHolder and return for display.
        return (new ViewHolder(view, mClickListener, mLongClickListener));
    }

    // set up the text of list item to display the search tag
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // set the textview's text to the value of mTags at location position.
        holder.textView.setText(mTags.get(position));
    }

    // returns num of items that adapter binds
    @Override
    public int getItemCount() {
        return mTags.size();
    }

    // inner subclass of RecyclerView.ViewHolder used to implement
    // the view-holder pattern in the context of a RecyclerView.
    // every item in a RecyclerView must be wrapped in its own RecyclerView.Holder.
    // this is MANDATORY for RecyclerViews (suggested for ListViews)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        // constructor for the inner static ViewHolder class.
        // it takes a view and 2 listeners for that view.
        // pass view to parent ViewHolder constructor, configure the TextView, and set listeners.
        public ViewHolder( View itemView, View.OnClickListener clickListener, View.OnLongClickListener longClickListener) {

            // pass view to parent constructor.
            super(itemView);

            // get ref to text view object.
            textView = (TextView) itemView.findViewById(R.id.textView);

            // attach listeners to itemView
            itemView.setOnClickListener(clickListener);
            itemView.setOnLongClickListener(longClickListener);
        }
    }

}
