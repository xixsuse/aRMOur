package com.skepticalone.mecachecker.ui.list;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

interface FabCallbacks {
    FloatingActionMenu getFloatingActionMenu();

    FloatingActionButton getFabNormalDay();

    FloatingActionButton getFabLongDay();

    FloatingActionButton getFabNightShift();
}
