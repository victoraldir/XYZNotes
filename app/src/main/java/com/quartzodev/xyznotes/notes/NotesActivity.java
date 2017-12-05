package com.quartzodev.xyznotes.notes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.quartzodev.xyznotes.R;
import com.quartzodev.xyznotes.ViewModelFactory;
import com.quartzodev.xyznotes.addeditnote.AddEditNoteActivity;
import com.quartzodev.xyznotes.util.ActivityUtils;

public class NotesActivity extends AppCompatActivity implements NotesNavigator, NotesItemNavigator {

    public static final int RC_ADD_TASK = 1;

    private NotesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_act);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewModel = obtainViewModel(this);

        // Subscribe to "open note" event
        mViewModel.getOpenNoteEvent().observe(this, new Observer<Long>() {

            @Override
            public void onChanged(@Nullable Long id) {
                openItemEdit(id);
            }
        });

        // Subscribe to "new note" event
        mViewModel.getNewNoteEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void _) {
                addNewNote();
            }
        });

        setupViewFragment();

    }

    private void setupViewFragment() {
        NotesFragment notesFragment =
                (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (notesFragment == null) {
            // Create the fragment
            notesFragment = NotesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), notesFragment, R.id.contentFrame);
        }
    }

    @Override
    public void addNewNote() {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        startActivityForResult(intent, NotesActivity.RC_ADD_TASK);
    }

    public static NotesViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        NotesViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(NotesViewModel.class);

        return viewModel;
    }

    @Override
    public void openItemEdit(Long noteId) {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteActivity.ARG_EDIT_NOTE_ID, noteId);
        startActivityForResult(intent, AddEditNoteActivity.REQUEST_CODE);
    }
}
