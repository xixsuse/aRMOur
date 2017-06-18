package com.skepticalone.mecachecker.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class DatabaseInitUtil {

    private static final String[] FIRST = new String[]{
            "Special edition", "New", "Cheap", "Quality", "Used"};
    private static final String[] SECOND = new String[]{
            "Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle"};
    private static final String[] DESCRIPTION = new String[]{
            "is finally here", "is recommended by Stan S. Stanman",
            "is the best sold product on Mêlée Island", "is \uD83D\uDCAF", "is ❤️", "is fine"};
    private static final String[] COMMENTS = new String[]{
            "Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6",
    };

    static void initializeDb(AppDatabase db) {
        List<Expense> expenses = new ArrayList<>(FIRST.length * SECOND.length);

        generateData(expenses);

        insertData(db, expenses);
    }

    private static void generateData(List<Expense> expenses) {
        Random rnd = new Random();
        for (int i = 0; i < FIRST.length; i++) {
            for (int j = 0; j < SECOND.length; j++) {
                Expense expense = new Expense(
                        FIRST[i] + " " + SECOND[j],
                        BigDecimal.valueOf(rnd.nextInt(), 2),
                        DESCRIPTION[j],
                        null,
                        null
                );
                expenses.add(expense);
            }
        }
    }

    private static void insertData(AppDatabase db, List<Expense> expenses) {
        db.beginTransaction();
        try {
            for (Expense expense : expenses) {
                db.expenseDao().insertExpense(expense);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}
