package com.quartzodev.xyznotes.addeditnote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.quartzodev.xyznotes.data.Note;
import com.quartzodev.xyznotes.data.source.NotesDataSource;

/**
 * Created by victoraldir on 03/12/2017.
 */

public class AddEditNotePresenter implements AddEditNoteContract.Presenter, NotesDataSource.GetNoteCallback {

    @NonNull
    private final NotesDataSource mNotesRepository;

    @NonNull
    private final AddEditNoteContract.View mAddNoteView;

    @Nullable
    private Long mNoteId;

    private boolean mIsDataMissing;

    public AddEditNotePresenter(@NonNull NotesDataSource notesRepository,
                                @NonNull AddEditNoteContract.View addNoteView,
                                Long noteId,
                                boolean shouldLoadDataFromRepo) {
        mNotesRepository = notesRepository;
        mAddNoteView = addNoteView;
        mNoteId = noteId;

        mIsDataMissing = shouldLoadDataFromRepo;

        mAddNoteView.setPresenter(this);
    }

    @Override
    public void saveNote(String content) {
        if(isNewNote()){
            createNote(content);
        }else{
            updateNote(content);
        }
    }

    @Override
    public void start() {
        if (!isNewNote() && mIsDataMissing) {
            populateNote();
        }
    }

    @Override
    public void populateNote() {
        if (isNewNote()) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        mNotesRepository.getNote(mNoteId, this);
    }

    private boolean isNewNote() {
        return mNoteId == null;
    }

    private void createNote(String content){

        Note newNote = new Note(content);

        if(newNote.isEmpty()){
            mAddNoteView.showEmptyNoteError();
        }else{
            mNotesRepository.saveNote(newNote);
            mAddNoteView.showNotesList();
        }
    }

    private void updateNote(String content) {
        if (isNewNote()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        mNotesRepository.saveNote(new Note(mNoteId,content));
        mAddNoteView.showNotesList(); // After an edit, go back to the list.
    }

    @Override
    public void onNoteLoaded(Note note) {
        // The view may not be able to handle UI updates anymore
        if (mAddNoteView.isActive()) {
            mAddNoteView.setContent(note.getContent());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {

    }
}
