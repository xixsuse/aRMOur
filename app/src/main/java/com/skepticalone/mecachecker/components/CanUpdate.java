package com.skepticalone.mecachecker.components;

import android.content.ContentValues;

interface CanUpdate extends HasContentUri {
    void update(ContentValues contentValues);
}