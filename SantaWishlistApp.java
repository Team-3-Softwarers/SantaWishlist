/***************************************************************
*file: SantaWishlistApp.java
*authors: Sarkis Gafafyan, Damon Mapinda, Vincent Perez, 
*		  Marie Philavong, and Brandon Shippy
*class: CS 4800 - Software Engineering
*assignment: Final Project
*date last modified: 01/14/25
*
*purpose: This program manages the backend logic of Santa's 
*         Wishlist application. It implements the singleton 
*         pattern to ensure only one instance of the wishlist 
*         app exists. It allows for creating, editing, and 
*         sending wishlists for children.
*
****************************************************************/

import java.util.HashMap;
import java.util.Map;

public class SantaWishlistApp
{
    private static SantaWishlistApp instance;  // singleton instance of the application
    private Map<String, Wishlist> wishlists;  // Map of child names to their wishlists

    /***************************************************************
    *function: SantaWishlistApp
    *purpose: Private constructor to prevent instantiation.
    *         Initializes the wishlists map.
    ****************************************************************/
    private SantaWishlistApp()
    {
        wishlists = new HashMap<>();
    }

    /***************************************************************
    *function: getInstance
    *purpose: Provides the singleton instance of SantaWishlistApp.
    *         If no instance exists, it creates one.
    ****************************************************************/
    public static SantaWishlistApp getInstance()
    {
        if (instance == null) {
            instance = new SantaWishlistApp();
        }
        return instance;
    }

    /***************************************************************
    *function: createWishlistForChild
    *purpose: Creates a wishlist for a specified child if one does 
    *         not already exist.
    ****************************************************************/
    public void createWishlistForChild(String childName)
    {
        if (!wishlists.containsKey(childName))
        {
        	// add new wishlist for the child
            wishlists.put(childName, new Wishlist(childName));
        }
    }

    /***************************************************************
    *function: addItem
    *purpose: Adds an item to the child's wishlist.
    *         It takes the child's name, item name, and description.
    ****************************************************************/
    public void addItem(String childName, String itemName, String description)
    {
    	// get the wishlist for the specified child
        Wishlist wishlist = wishlists.get(childName);
        
        if (wishlist != null)
        {
        	// add the item to the wishlist
            wishlist.addItem(itemName, description);
        }
    }

    /***************************************************************
    *function: getWishlist
    *purpose: Retrieves the wishlist for a specific child.
    ****************************************************************/
    public Wishlist getWishlist(String childName)
    {
    	// return the wishlist for the specified child
        return wishlists.get(childName);
    }

    /***************************************************************
    *function: editItem
    *purpose: Edits an item in the child's wishlist based on the 
    *         specified index and new details.
    ****************************************************************/
    public void editItem(String childName, int itemIndex, String newItemName, String newDescription)
    {
    	// get the child's wishlist
        Wishlist wishlist = wishlists.get(childName);
        
        if (wishlist != null)
        {
        	// edit the item in the wishlist
            wishlist.editItem(itemIndex, newItemName, newDescription);
        }
    }

    /***************************************************************
    * function: removeItem
    * purpose: Removes an item from the specified child's wishlist.
    *          It searches for the item by name (case-insensitive), 
    *          and if found, removes it from the wishlist. If the 
    *          item is not found or the wishlist is null, the method 
    *          returns false.
    ****************************************************************/
    public boolean removeItem(String childName, String itemName)
    {
    	// get the wishlist for the specified child
        Wishlist wishlist = getWishlist(childName);

        // ensure the wishlist is not null
        if (wishlist != null)
        {
            // iterate through the list of items to find the item to remove
            for (WishlistItem item : wishlist.getItems())
            {
                // compare the item's name (case-insensitive) with the input name
                if (item.getItemName().equalsIgnoreCase(itemName))
                {
                    // if found, remove the item from the list
                    wishlist.getItems().remove(item);
                    return true;
                }
            }
        }
        return false;
    }
    
    /***************************************************************
    *function: sendWishlist
    *purpose: Sends the child's wishlist to Santa.
    ****************************************************************/
    public void sendWishlist(String childName)
    {
    	// get the wishlist for the specified child
        Wishlist wishlist = wishlists.get(childName);
        
        if (wishlist != null)
        {
            // send the wishlist
            System.out.println("Sending wishlist to Santa for " + childName);
        }
    }
}