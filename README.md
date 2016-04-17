Android app to search Twitter for popular/trending topics/tweets.

Learned:
* SharedPreferences:
    + used to load/store settings/configuration from previous sessions to current session.
    + uses key-val pairs similar to a hash map or dictionary.

* Implicit Intents:
    + intents that are not lauch using Intent i = new Intent(...); startActivity(i);
    + used to send query string and launch browser with recieved query string.

* RecyclerView:
    + improved version of ListView b/c separation of data's presentation from reuse logic, more flexible customization operations for presenting items.
        * For example, ListViews have to be a vertical list, RecyclerViews have layout managers than can display vertical/horizontal/grid/custom.
    + RecyclerView.ViewHolder:
        * RecyclerViews formalize the view-holder pattern by requiring it, whereas it was just a suggest for ListViews.
    + RecyclerView.Adapter:
        * used by ViewHolder to bind list items to data in a List.
    + RecyclerView.ItemDecorator
        * ListViews automatically divide list items with a horizontal line,
        but RecyclerViews don't automatically, but we can achieve the same result using ItemDecorators.
    + used to save previous search to make searchs quicker and easier.

* Context Menus:
    + use AlertDialogs to build a context menu to Edit, Delete, Share a search topic.

