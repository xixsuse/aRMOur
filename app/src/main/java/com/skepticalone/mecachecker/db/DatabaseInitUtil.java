package com.skepticalone.mecachecker.db;

import com.skepticalone.mecachecker.db.entity.ExpenseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseInitUtil {

    private static final String[] FIRST = new String[]{
            "Special edition", "New", "Cheap", "Quality", "Used"};
    private static final String[] SECOND = new String[]{
            "Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle"};
    private static final String[] DESCRIPTION = new String[]{
            "is finally here", "is recommended by Stan S. Stan",
            "is the best bv sold product on Milk Island", "is \uD83D\uDCAF", "is ❤️", "is fine"};
//    private static final String[] COMMENTS = new String[]{
//            "Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6",
//    };

    static void initializeDb(AppDatabase db) {
        List<ExpenseEntity> expenses = new ArrayList<>(FIRST.length * SECOND.length);

        generateData(expenses);

        insertData(db, expenses);
    }

    private static void generateData(List<ExpenseEntity> expenses) {
        Random rnd = new Random();
        for (int i = 0; i < FIRST.length; i++) {
            for (int j = 0; j < SECOND.length; j++) {
                expenses.add(randomExpense(rnd, i, j));
            }
        }
    }

    private static ExpenseEntity randomExpense(Random rnd, String title, String description) {
//        DateTime claimed = rnd.nextBoolean() ? new DateTime(rnd.nextLong()) : null;
//        DateTime paid = claimed == null ? null : new DateTime(rnd.nextLong());
//        if (paid != null && paid.isBefore(claimed)) paid = null;

        return new ExpenseEntity(
                title,
                BigDecimal.valueOf(rnd.nextInt(10000), 2),
                description);
    }

    public static ExpenseEntity randomExpense(Random rnd) {
        return randomExpense(rnd, FIRST[rnd.nextInt(FIRST.length)] + " " + SECOND[rnd.nextInt(SECOND.length)], DESCRIPTION[rnd.nextInt(DESCRIPTION.length)]);
    }

    private static ExpenseEntity randomExpense(Random rnd, int i, int j) {
        return randomExpense(rnd, FIRST[i] + " " + SECOND[j], DESCRIPTION[j]);
    }

    private static void insertData(AppDatabase db, List<ExpenseEntity> expenses) {
        db.beginTransaction();
        try {
            for (ExpenseEntity expense : expenses) {
                db.expenseDao().insertExpense(expense);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}
