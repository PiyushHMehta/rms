package com.zeta.view;

import com.zeta.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuCatalog {
    private final List<MenuItem> menuItems;
    {
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(1, "Pizza", 200, 3));
        menuItems.add(new MenuItem(2, "Burger", 150, 2));
        menuItems.add(new MenuItem(3, "Coke", 50, 1));
        menuItems.add(new MenuItem(4, "Pasta", 180, 4));
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public MenuItem getMenuItemById(int id) {
        MenuItem menuItem = null;
        for(MenuItem item: menuItems) {
            if(item.getId() == id) {
                menuItem = item;
                break;
            }
        }
        return menuItem;
    }
}
