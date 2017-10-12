package com.skepticalone.armour.ui.list;

import android.support.annotation.NonNull;
import android.view.View;

public interface DeletedItemsInfo extends View.OnClickListener {
    @NonNull
    String getMessage();
}
