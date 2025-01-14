/***************************************************************
*file: Wishlist.java
*authors: Sarkis Gafafyan, Damon Mapinda, Vincent Perez, 
*		  Marie Philavong, and Brandon Shippy
*class: CS 4800 - Software Engineering
*assignment: Final Project
*date last modified: 01/14/25
*
*purpose: This class manages a child's wishlist. It allows adding, 
*         editing, and removing items from the wishlist, as well 
*         as searching for specific items. The class provides 
*         functionalities to manipulate and display the wishlist 
*         for the child.
****************************************************************/

import java.util.ArrayList;
import java.util.List;

public class Wishlist
{
    private String childName;  // the name of the child for whom the wishlist is created
    private List<WishlistItem> items;  // List to hold items in the wishlist

    /***************************************************************
    *function: Wishlist
    *purpose: Constructor to initialize the wishlist for the child.
    *         It initializes the childName and the items list to
    *         an empty ArrayList.
    ****************************************************************/
    public Wishlist(String childName)
    {
        this.childName = childName;
        this.items = new ArrayList<>();
    }

    /***************************************************************
    *function: addItem
    *purpose: Adds an item to the wishlist. The item consists of a 
    *         name and description. If either the name or description 
    *         is empty or null, an error message is displayed.
    ****************************************************************/
    public void addItem(String itemName, String description)
    {
        if (itemName == null || itemName.trim().isEmpty() || description == null || description.trim().isEmpty())
        {
            System.out.println("Item name or description cannot be empty.");
            return;
        }
        
        // add the new item to the wishlist
        items.add(new WishlistItem(itemName, description));
    }

    /***************************************************************
    *function: editItem
    *purpose: Edits an item on the wishlist at a specific index.
    *         It updates the name and description of the item.
    *         If the index is invalid, an error message is displayed.
    ****************************************************************/
    public void editItem(int itemIndex, String newItemName, String newDescription)
    {
        if (itemIndex < 0 || itemIndex >= items.size())
        {
            System.out.println("Invalid item index.");
            return;
        }
        
        WishlistItem item = items.get(itemIndex);  // get the item at the specified index
        item.setItemName(newItemName);  // update the item name
        item.setDescription(newDescription);  // update the item description
    }

    /***************************************************************
    *function: removeItem
    *purpose: Removes an item from the wishlist at a specified index.
    *         If the index is invalid, an error message is displayed.
    ****************************************************************/
    public void removeItem(int itemIndex)
    {
        if (itemIndex < 0 || itemIndex >= items.size()) 
        {
            System.out.println("Invalid item index.");
            return; 
        }
        
        // remove the item at the specified index
        items.remove(itemIndex);
    }

    /***************************************************************
    *function: getItems
    *purpose: Returns the list of items currently in the wishlist.
    ****************************************************************/
    public List<WishlistItem> getItems()
    {
        return items;
    }

    /***************************************************************
    *function: searchItem
    *purpose: Searches for an item in the wishlist by its name.
    *         If found, it returns the WishlistItem; otherwise, 
    *         it returns null.
    ****************************************************************/
    public WishlistItem searchItem(String itemName)
    {
        for (WishlistItem item : items)
        {
            if (item.getItemName().equalsIgnoreCase(itemName))
            {
                return item;
            }
        }
        return null;
    }

    /***************************************************************
    *function: toString
    *purpose: Overriding toString() method to format the wishlist 
    *         display. It returns a string representation of the 
    *         child's wishlist with each item's name and description.
    ****************************************************************/
    @Override
    public String toString()
    {
        StringBuilder wishlistString = new StringBuilder("Wishlist for " + childName + ":\n");
        
        for (WishlistItem item : items)
        {
            wishlistString.append(item.getItemName()).append(": ").append(item.getDescription()).append("\n");
        }
        
        return wishlistString.toString();
    }
}