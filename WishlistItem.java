/***************************************************************
*file: WishlistItem.java
*authors: Sarkis Gafafyan, Damon Mapinda, Vincent Perez, 
*		  Marie Philavong, and Brandon Shippy
*class: CS 4800 - Software Engineering
*assignment: Final Project
*date last modified: 01/14/25
*
*purpose: This class represents an item on a child's wishlist.
*         It stores the name and description of the item and 
*         provides getter and setter methods to access and 
*         modify these attributes.
*
****************************************************************/

public class WishlistItem
{
    private String itemName;	// name of the item
    private String description;	// description of the item

    /***************************************************************
    *function: WishlistItem
    *purpose: Constructor that initializes the item name and description.
    ****************************************************************/
    public WishlistItem(String itemName, String description)
    {
        this.itemName = itemName;
        this.description = description;
    }

    /***************************************************************
    *function: getItemName
    *purpose: Getter method for the item name.
    ****************************************************************/
    public String getItemName()
    {
        return itemName;
    }

    /***************************************************************
    *function: setItemName
    *purpose: Setter method to modify the item name.
    ****************************************************************/
    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    /***************************************************************
    *function: getDescription
    *purpose: Getter method for the item description.
    ****************************************************************/
    public String getDescription()
    {
        return description;
    }

    /***************************************************************
    *function: setDescription
    *purpose: Setter method to modify the item description.
    ****************************************************************/
    public void setDescription(String description)
    {
        this.description = description;
    }
}