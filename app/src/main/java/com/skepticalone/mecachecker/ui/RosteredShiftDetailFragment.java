package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftDetailAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.data.viewModel.DateViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.LocalTime;

public final class RosteredShiftDetailFragment
        extends DetailFragment<RosteredShiftEntity, RosteredShiftViewModel>
        implements RosteredShiftDetailAdapter.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<RosteredShiftEntity> createAdapter(Context context) {
        return new RosteredShiftDetailAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    RosteredShiftViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(RosteredShiftViewModel.class);
    }

    @Override
    public void changeDate() {
        showDialogFragment(new RosteredShiftDateDialogFragment());
    }

    @Override
    public void changeTime(boolean start, boolean logged) {
        showDialogFragment(RosteredShiftTimeDialogFragment.newInstance(start, logged));
    }

    @Override
    public void setLogged(boolean logged) {
        getViewModel().setLogged(getCurrentItem().getId(), logged);
    }

    @NonNull
    @Override
    CommentDialogFragment getNewCommentDialogFragment() {
        return new RosteredShiftCommentDialogFragment();
    }

    public static final class RosteredShiftCommentDialogFragment extends CommentDialogFragment<RosteredShiftEntity> {

        @NonNull
        @Override
        ViewModelContract<RosteredShiftEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(RosteredShiftViewModel.class);
        }

    }

    public static final class RosteredShiftDateDialogFragment extends ShiftDateDialogFragment<RosteredShiftEntity> {

        @NonNull
        @Override
        DateViewModelContract<RosteredShiftEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(RosteredShiftViewModel.class);
        }

    }

    public static final class RosteredShiftTimeDialogFragment extends TimeDialogFragment<RosteredShiftEntity, RosteredShiftViewModel> {

        private static final String LOGGED = "LOGGED";
        private boolean logged;

        private static RosteredShiftTimeDialogFragment newInstance(boolean start, boolean logged) {
            Bundle arguments = getArgs(start);
            arguments.putBoolean(LOGGED, logged);
            RosteredShiftTimeDialogFragment fragment = new RosteredShiftTimeDialogFragment();
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            logged = getArguments().getBoolean(LOGGED);
        }

        @NonNull
        @Override
        ShiftData getShiftDataForDisplay(@NonNull RosteredShiftEntity shift) {
            final ShiftData shiftData;
            if (logged) {
                shiftData = shift.getLoggedShiftData();
                if (shiftData == null) throw new IllegalStateException();
            } else {
                shiftData = shift.getShiftData();
            }
            return shiftData;
        }

        @NonNull
        @Override
        RosteredShiftViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(RosteredShiftViewModel.class);
        }

        @Override
        void onTimeSet(@NonNull LocalTime time, boolean start) {
            getViewModel().saveNewTime(getCurrentItem(), time, start, logged);
        }

    }
}
