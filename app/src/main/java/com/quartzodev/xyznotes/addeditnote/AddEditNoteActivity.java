package com.quartzodev.xyznotes.addeditnote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.architecture.blueprints.todoapp.Injection;
import com.quartzodev.xyznotes.R;
import com.quartzodev.xyznotes.util.ActivityUtils;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";
    public static final String ARG_EDIT_NOTE_ID = "EDIT_NOTE_ID";
    public static final int REQUEST_CODE = 10;

    private AddEditNotePresenter mAddEditNotePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_note_act);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AddEditNoteFragment addEditNoteFragment =
                (AddEditNoteFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (addEditNoteFragment == null) {
            // Create the fragment
            addEditNoteFragment = AddEditNoteFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), addEditNoteFragment, R.id.contentFrame);
        }

        Long nodeId = getIntent().getLongExtra(ARG_EDIT_NOTE_ID, 0);

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        mAddEditNotePresenter = new AddEditNotePresenter(
                Injection.provideNotesRepository(getApplicationContext()), addEditNoteFragment,
                nodeId == 0 ? null : nodeId,
                shouldLoadDataFromRepo); //Set id here when editing

    }

}
