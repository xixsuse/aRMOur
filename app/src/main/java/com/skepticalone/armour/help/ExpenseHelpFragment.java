//package com.skepticalone.armour.help;
//
//import android.support.annotation.NonNull;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.skepticalone.armour.R;
//
//public final class ExpenseHelpFragment extends HelpFragment {
//
//    @Override
//    int getTitle() {
//        return R.string.expenses;
//    }
//
//    @Override
//    void onAddToView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
//        View section;
//        section = inflater.inflate(R.layout.help_item_payment, container, false);
//        section.findViewById(R.id.help_description_2).setVisibility(View.VISIBLE);
//        container.addView(section);
//        section = inflater.inflate(R.layout.help_item_add_single, container, false);
//        ((TextView) section.findViewById(R.id.title)).setText(R.string.new_expense);
//        ((TextView) section.findViewById(R.id.add_single_label)).setText(R.string.help_add_expense_label);
//        section.findViewById(R.id.help_description_1).setVisibility(View.GONE);
//        container.addView(section);
//        section = inflater.inflate(R.layout.help_item_delete, container, false);
//        ((TextView) section.findViewById(R.id.title)).setText(R.string.help_delete_expenses_title);
//        ((TextView) section.findViewById(R.id.select_label)).setText(R.string.help_select_expenses_label);
//        container.addView(section);
//        section = inflater.inflate(R.layout.help_item_totals, container, false);
//        ((TextView) section.findViewById(R.id.help_description_1)).setText(R.string.help_totals_expenses_description);
//        container.addView(section);
//        section = inflater.inflate(R.layout.help_item_subtotals, container, false);
//        ((TextView) section.findViewById(R.id.title)).setText(R.string.help_subtotals_expenses_title);
//        ((TextView) section.findViewById(R.id.select_label)).setText(R.string.help_select_expenses_label);
//        ((TextView) section.findViewById(R.id.help_description_1)).setText(R.string.help_subtotals_expenses_description);
//        container.addView(section);
//    }
//}
