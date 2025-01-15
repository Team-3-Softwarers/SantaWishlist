
/** *************************************************************
 *file: SantaWishlistGUI.java
 *authors: Sarkis Gafafyan, Damon Mapinda, Vincent Perez,
 *		  Marie Philavong, and Brandon Shippy
 *class: CS 4800 - Software Engineering
 *assignment: Final Project
 *date last modified: 01/15/25
 *
 *purpose: This program manages the graphical user interface (GUI)
 *         for Santa's Wishlist application. It allows children
 *         to create, edit, and send their wishlists to Santa,
 *         and allows parents to view the wishlists of their children.
 *         It includes login authentication for both child and adult
 *         (Santa) users, and ensures that only parents can view
 *         wishlists.
 *
 *************************************************************** */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.swing.*;

public class SantaWishlistGUI extends JFrame {

    private SantaWishlistApp wishlistApp; // reference to the wishlist application backend
    private JTextArea wishlistTextArea;   // text area to display the child's wishlist
    private String userRole;              // role of the user (child/adult)
    private String childName;             // name of the child for whom the wishlist is created

    /**
     * *************************************************************
     * function: SantaWishlistGUI purpose: Constructor that sets up the GUI for
     * the application. It handles login, authentication, and GUI element
     * initialization for both child and parent users.
     * **************************************************************
     */
    public SantaWishlistGUI() {
        // get the instance of SantaWishlistApp
        wishlistApp = SantaWishlistApp.getInstance();

        // prompt for login (child or adult/Santa)
        String[] roles = {"Child", "Santa"};
        userRole = (String) JOptionPane.showInputDialog(this, "Select user role:", "Login",
                JOptionPane.PLAIN_MESSAGE, null, roles, roles[0]);

        if (userRole == null) {
            System.exit(0);  // exit if the login dialog was canceled
            return;
        }

        // if user selects Santa, ask for password ('parent123')
        if ("Santa".equals(userRole)) {
            JPasswordField passwordField = new JPasswordField(10);  // creates password field
            int attempts = 0;	// track the number of attempts
            boolean authenticated = false;

            // loop to give 3 attempts
            while (attempts < 3 && !authenticated) {
                int option = JOptionPane.showConfirmDialog(this, passwordField,
                        "Enter password for Santa:", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    char[] passwordArray = passwordField.getPassword();
                    String password = new String(passwordArray);  // convert char array to string

                    if ("parent123".equals(password)) {
                        authenticated = true; // password is correct, exit the loop
                    } else {
                        attempts++;  // increment the attempt counter
                        if (attempts < 3) {
                            JOptionPane.showMessageDialog(this, "Incorrect password! Attempt " + attempts + " of 3.");
                        }
                    }
                } else {
                    // exit if the user cancels the password dialog
                    System.exit(0);
                    return;
                }
            }

            // if the user didn't authenticate within 3 attempts
            if (!authenticated) {
                JOptionPane.showMessageDialog(this, "Too many incorrect attempts. Exiting.");
                System.exit(0);  // exit the application after 3 failed attempts
            }
        }

        // initialize backend logic (child name is still required)
        childName = JOptionPane.showInputDialog(this, "Enter the child's name:");

        if (childName == null || childName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Child's name cannot be empty!");
            System.exit(0);  // exit if the name is not provided
            return;  // exit the constructor, so no further GUI creation happens
        }

        wishlistApp.createWishlistForChild(childName);  // create wishlist for child

        // set up GUI window
        setTitle("Santa's Wishlist");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // this will allow the window to close when "X" is clicked
        setSize(400, 300);
        setLayout(new BorderLayout());

        // add wishlist text area
        wishlistTextArea = new JTextArea();
        wishlistTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(wishlistTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // add buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Item");
        JButton removeButton = new JButton("Remove Item");
        JButton sendButton = new JButton("Send to Santa");
        JButton parentViewButton = new JButton("Parent View");
        JButton exportButton = new JButton("Export to TXT");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(parentViewButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.revalidate();
        buttonPanel.repaint();

        /**
         * *************************************************************
         * function: addButton ActionListener purpose: Handles adding an item to
         * the child's wishlist. Only children can add items. They are prompted
         * for the item's name and description.
         * **************************************************************
         */
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Child".equals(userRole)) {
                    String itemName = JOptionPane.showInputDialog("Enter item name:");
                    String description = JOptionPane.showInputDialog("Enter item description:");

                    if (itemName != null && !itemName.trim().isEmpty() && description != null && !description.trim().isEmpty()) {
                        wishlistApp.addItem(childName, itemName, description);
                        updateWishlistDisplay();
                    }
                } else {
                    JOptionPane.showMessageDialog(SantaWishlistGUI.this, "Parents cannot add items.");
                }
            }
        });

        /**
         * *************************************************************
         * function: editButton ActionListener purpose: Handles editing an item
         * in the child's wishlist. Only children can edit items in their
         * wishlist. They are prompted to specify which item to edit.
         * **************************************************************
         */
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Child".equals(userRole)) {
                    // prompt for the item to edit
                    String oldItem = JOptionPane.showInputDialog("Enter the item you want to edit:");

                    if (oldItem != null && !oldItem.trim().isEmpty()) {
                        // get the index of the item in the wishlist
                        Wishlist wishlist = wishlistApp.getWishlist(childName);  // get the wishlist of the child

                        if (wishlist != null) {
                            int itemIndex = -1;
                            // find the item index manually by looping through the wishlist items
                            for (int i = 0; i < wishlist.getItems().size(); i++) {
                                if (wishlist.getItems().get(i).getItemName().equalsIgnoreCase(oldItem)) {
                                    itemIndex = i;
                                    break;
                                }
                            }

                            if (itemIndex != -1) {
                                // if the item was found, ask for the new description
                                String newItem = JOptionPane.showInputDialog("Enter the new description:");

                                if (newItem != null && !newItem.trim().isEmpty()) {
                                    // call editItem with index, oldItem, and new description
                                    wishlistApp.editItem(childName, itemIndex, oldItem, newItem);
                                    updateWishlistDisplay();
                                }
                            } else {
                                JOptionPane.showMessageDialog(SantaWishlistGUI.this, "Item not found!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(SantaWishlistGUI.this, "No wishlist found for this child.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(SantaWishlistGUI.this, "Parents cannot edit items.");
                }
            }
        });

        /**
         * ****************************************************************************
         * function: removeButton ActionListener purpose: Prompts the child for
         * an item to remove from their wishlist. If the user is a child and the
         * item is found in their wishlist, it removes the item and updates the
         * display. If the item isn't found or the user is not a child, an
         * appropriate message is shown.
         * ****************************************************************************
         */
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if the user is a child
                if ("Child".equals(userRole)) {
                    // prompt the user for the name of the item to remove
                    String itemToRemove = JOptionPane.showInputDialog("Enter the item you want to remove:");

                    // rnsure the input is not null or empty
                    if (itemToRemove != null && !itemToRemove.trim().isEmpty()) {
                        // get the wishlist for the specified child
                        Wishlist wishlist = wishlistApp.getWishlist(childName);

                        // ensure the wishlist exists
                        if (wishlist != null) {
                            // attempt to remove the item from the wishlist using the backend logic
                            boolean itemRemoved = wishlistApp.removeItem(childName, itemToRemove);

                            // if the item was successfully removed, update the display
                            if (itemRemoved) {
                                updateWishlistDisplay();
                            } else {
                                // if the item wasn't found, show an error message
                                JOptionPane.showMessageDialog(SantaWishlistGUI.this, "Item not found!");
                            }
                        } else {
                            // if no wishlist exists for the child, show an error message
                            JOptionPane.showMessageDialog(SantaWishlistGUI.this, "No wishlist found for this child.");
                        }
                    }
                } else {
                    // if the user is not a child, they cannot remove items
                    JOptionPane.showMessageDialog(SantaWishlistGUI.this, "Parents cannot remove items.");
                }
            }
        });

        /**
         * *************************************************************
         * function: sendButton ActionListener purpose: Sends the child's
         * wishlist to Santa, confirming the action with a success message.
         * **************************************************************
         */
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wishlistApp.sendWishlist(childName);  // send the wishlist to Santa
                JOptionPane.showMessageDialog(SantaWishlistGUI.this, "Wishlist sent to the North Pole!");  // display success message
            }
        });

        /**
         * *************************************************************
         * function: exportButton ActionListener purpose: Exports the current
         * wishlist to a text file. The file is saved with the child's name and
         * current date.
         * **************************************************************
         */
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Wishlist wishlist = wishlistApp.getWishlist(childName);
                if (wishlist != null && !wishlist.getItems().isEmpty()) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Save Wishlist");
                    fileChooser.setSelectedFile(new java.io.File(childName + "_wishlist.txt"));

                    int userSelection = fileChooser.showSaveDialog(SantaWishlistGUI.this);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        java.io.File fileToSave = fileChooser.getSelectedFile();
                        try (FileWriter writer = new FileWriter(fileToSave)) {
                            // Write header
                            writer.write("Wishlist for " + childName + "\n");
                            writer.write("Created on: " + java.time.LocalDate.now() + "\n\n");

                            // Write items
                            for (WishlistItem item : wishlist.getItems()) {
                                writer.write(item.getItemName() + ": " + item.getDescription() + "\n");
                            }

                            JOptionPane.showMessageDialog(SantaWishlistGUI.this,
                                    "Wishlist exported successfully to:\n" + fileToSave.getAbsolutePath());
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(SantaWishlistGUI.this,
                                    "Error exporting wishlist: " + ex.getMessage(),
                                    "Export Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(SantaWishlistGUI.this,
                            "No items in wishlist to export!",
                            "Export Error",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        /**
         * *************************************************************
         * function: parentViewButton ActionListener purpose: Displays the
         * parent view, showing the child's wishlist. Only adults can access
         * this feature.
         * **************************************************************
         */
        parentViewButton.setEnabled("Santa".equals(userRole));
        parentViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showParentView();
            }
        });

        /**
         * *************************************************************
         * function: logoutButton ActionListener purpose: Logs the user out and
         * prompts for login again.
         * **************************************************************
         */
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // log out and go back to login screen
                dispose();  // close the current window
                new SantaWishlistGUI();  // create a new instance to prompt for login again
            }
        });

        // display GUI
        setVisible(true);
    }

    /**
     * *************************************************************
     * function: updateWishlistDisplay purpose: Updates the text area with the
     * current wishlist of the child, displaying each item and its description.
     * **************************************************************
     */
    private void updateWishlistDisplay() {
        // get the wishlist of the specific child
        Wishlist wishlist = wishlistApp.getWishlist(childName);

        // if the wishlist exists, update the display
        if (wishlist != null) {
            String wishlistText = wishlist.getItems().stream()
                    .map(item -> item.getItemName() + ": " + item.getDescription()) // convert each WishlistItem to a string
                    .collect(Collectors.joining("\n"));  // join the strings with new lines

            // set the text area content
            wishlistTextArea.setText(wishlistText);
        }
    }

    /**
     * *************************************************************
     * function: showParentView purpose: Displays the parent view of the child's
     * wishlist. Parents can view and modify the wishlist here.
     * **************************************************************
     */
    private void showParentView() {
        // display the wishlist for parents
        Wishlist wishlist = wishlistApp.getWishlist(childName);

        if (wishlist != null) {
            // create a string representing the child's name and the wishlist
            String wishlistText = "Wishlist for " + childName + ":\n";

            // append each item in the wishlist
            wishlistText += wishlist.getItems().stream()
                    .map(item -> item.getItemName() + ": " + item.getDescription())
                    .collect(Collectors.joining("\n"));

            // set the text area to show the wishlist
            wishlistTextArea.setText(wishlistText);
        }
    }

    /**
     * *************************************************************
     * main function: main purpose: Starts the SantaWishlistGUI application.
     * **************************************************************
     */
    public static void main(String[] args) {
        new SantaWishlistGUI();
    }
}
