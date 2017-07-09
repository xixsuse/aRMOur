package com.skepticalone.mecachecker.data;

import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class DatabaseInitUtil {
    private static final Random RANDOM = new Random();

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
        for (int i = 0; i < FIRST.length; i++) {
            for (int j = 0; j < SECOND.length; j++) {
                expenses.add(randomExpense(i, j));
            }
        }
    }

    public static CrossCoverEntity randomCrossCoverShift() {
        return new CrossCoverEntity(new LocalDate(2015 + RANDOM.nextInt(3), 1 + RANDOM.nextInt(12), 1 + RANDOM.nextInt(28)), BigDecimal.valueOf(RANDOM.nextInt(10000), 2), "There's #" + RANDOM.nextInt());
    }

    private static ExpenseEntity randomExpense(String title, String description) {
//        DateTime claimed = RANDOM.nextBoolean() ? new DateTime(RANDOM.nextLong()) : null;
//        DateTime paid = claimed == null ? null : new DateTime(RANDOM.nextLong());
//        if (paid != null && paid.isBefore(claimed)) paid = null;

        return new ExpenseEntity(
                title,
                BigDecimal.valueOf(RANDOM.nextInt(10000), 2),
                description);
    }

    public static ExpenseEntity randomExpense() {
        return randomExpense(FIRST[RANDOM.nextInt(FIRST.length)] + " " + SECOND[RANDOM.nextInt(SECOND.length)], DESCRIPTION[RANDOM.nextInt(DESCRIPTION.length)]);
    }

    private static ExpenseEntity randomExpense(int i, int j) {
        return randomExpense(FIRST[i] + " " + SECOND[j], DESCRIPTION[j]);
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
