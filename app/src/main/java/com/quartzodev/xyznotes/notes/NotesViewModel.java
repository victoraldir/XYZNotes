package com.quartzodev.xyznotes.notes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.quartzodev.xyznotes.SingleLiveEvent;
import com.quartzodev.xyznotes.SnackbarMessage;
import com.quartzodev.xyznotes.data.Note;
import com.quartzodev.xyznotes.data.source.NotesDataSource;
import com.quartzodev.xyznotes.data.source.NotesRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the data to be used in the task list screen.
 * <p>
 * BaseObservable implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a Bindable annotation to the property's
 * getter method.
 */

public class NotesViewModel extends AndroidViewModel {


    private final NotesRepository mNotesRepository;

    private final SingleLiveEvent<Void> mNewNoteEvent = new SingleLiveEvent<>();

    public NotesViewModel(@NonNull Application application, NotesRepository notesRepository) {
        super(application);

        mContext = application.getApplicationContext(); // Force use of Application Context.
        mNotesRepository = notesRepository;
    }


    SingleLiveEvent<Void> getNewNoteEvent() {
        return mNewNoteEvent;
    }


    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link com.quartzodev.xyznotes.data.source.NotesDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    void loadNotes(boolean forceUpdate, final boolean showLoadingUI){
        if (showLoadingUI) {
            dataLoading.set(true);
        }

        if (forceUpdate) {
            mNotesRepository.refreshNotes();
        }

        mNotesRepository.getNotes(new NotesDataSource.LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {

                List<Note> notesToShow = new ArrayList<>();

                // We filter the tasks based on the requestType
                for (Note note : notes) {
                    //TODO implement filtering here!
                    notesToShow.add(note);
                }

                if (showLoadingUI) {
                    dataLoading.set(false);
                }

                mIsDataLoadingError.set(false);

                items.clear();
                items.addAll(notesToShow);
                empty.set(items.isEmpty());

            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    public void addNewNote() {
        mNewNoteEvent.call();
    }


    // These observable fields will update Views automatically
    public final ObservableList<Note> items = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableField<String> currentFilteringLabel = new ObservableField<>();

    public final ObservableField<String> noTasksLabel = new ObservableField<>();

    public final ObservableField<Drawable> noTaskIconRes = new ObservableField<>();

    public final ObservableBoolean empty = new ObservableBoolean(false);

    public final ObservableBoolean tasksAddViewVisible = new ObservableBoolean();

    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    private final SingleLiveEvent<Long> mOpenNoteEvent = new SingleLiveEvent<>();

    private final Context mContext; // To avoid leaks, this must be an Application Context.

    private final SingleLiveEvent<Void> mNewTaskEvent = new SingleLiveEvent<>();


    public void start() {
        loadNotes(false);
    }

    public void loadNotes(boolean forceUpdate) {
        loadNotes(forceUpdate, true);
    }


    public void clearCompletedTasks() {
    }

    public void completeTask(Note note, boolean completed) {
    }

    SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }

    SingleLiveEvent<Long> getOpenNoteEvent() {
        return mOpenNoteEvent;
    }

    SingleLiveEvent<Void> getNewTaskEvent() {
        return mNewTaskEvent;
    }

    private void showSnackbarMessage(Integer message) {
        mSnackbarText.setValue(message);
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    public void addNewTask() {
        mNewTaskEvent.call();
    }
}
