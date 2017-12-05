package com.quartzodev.xyznotes.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.quartzodev.xyznotes.data.Note;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class NotesRepository implements NotesDataSource {

    private static NotesRepository INSTANCE;

    private NotesDataSource mNotesRemoteDataSource;

    private NotesDataSource mNotesLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<Long, Note> mCachedNotes;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    private  NotesRepository(@NonNull NotesDataSource notesRemoteDataSource,
                             @NonNull NotesDataSource notesLocaleDataSource){

        mNotesRemoteDataSource = notesRemoteDataSource;
        mNotesLocalDataSource = notesLocaleDataSource;

    }

    public static NotesRepository getInstance(NotesDataSource notesRemoteDataSource,
                                              NotesDataSource notesLocalDataSource){

        if(INSTANCE == null)
            INSTANCE = new NotesRepository(notesRemoteDataSource,notesLocalDataSource);

        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getNotes(@NonNull final LoadNotesCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedNotes != null && !mCacheIsDirty) {
            callback.onNotesLoaded(new ArrayList<>(mCachedNotes.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getNotesFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mNotesLocalDataSource.getNotes(new LoadNotesCallback() {

                @Override
                public void onNotesLoaded(List<Note> notes) {
                    refreshCache(notes);

                    callback.onNotesLoaded(new ArrayList<>(mCachedNotes.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getNotesFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void getNote(@NonNull final Long noteId, @NonNull final GetNoteCallback callback) {
        checkNotNull(noteId);
        checkNotNull(callback);

        Note cachedNote = getNoteWithId(noteId);

        // Respond immediately with cache if available
        if (cachedNote != null) {
            callback.onNoteLoaded(cachedNote);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.

        mNotesLocalDataSource.getNote(noteId, new GetNoteCallback() {

            @Override
            public void onNoteLoaded(Note note) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedNotes == null) {
                    mCachedNotes = new LinkedHashMap<>();
                }
                mCachedNotes.put(note.getId(), note);
                callback.onNoteLoaded(note);
            }

            @Override
            public void onDataNotAvailable() {
                mNotesRemoteDataSource.getNote(noteId, new GetNoteCallback() {

                    @Override
                    public void onNoteLoaded(Note note) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedNotes == null) {
                            mCachedNotes = new LinkedHashMap<>();
                        }
                        mCachedNotes.put(note.getId(), note);
                        callback.onNoteLoaded(note);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });

    }

    @Nullable
    private Note getNoteWithId(@NonNull Long id) {
        checkNotNull(id);
        if (mCachedNotes == null || mCachedNotes.isEmpty()) {
            return null;
        } else {
            return mCachedNotes.get(id);
        }
    }

    private void getNotesFromRemoteDataSource(@NonNull final LoadNotesCallback callback) {
        mNotesRemoteDataSource.getNotes(new LoadNotesCallback() {

            @Override
            public void onNotesLoaded(List<Note> notes) {
                refreshCache(notes);
                refreshLocalDataSource(notes);

                callback.onNotesLoaded(new ArrayList<>(mCachedNotes.values()));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void refreshCache(List<Note> notes) {
        if (mCachedNotes == null) {
            mCachedNotes = new LinkedHashMap<>();
        }
        mCachedNotes.clear();
        for (Note note : notes) {
            mCachedNotes.put(note.getId(), note);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Note> notes) {
        mNotesLocalDataSource.deleteAllNotes();
        for (Note note : notes) {
            mNotesLocalDataSource.saveNote(note);
        }
    }

    @Override
    public void saveNote(@NonNull Note note) {

        checkNotNull(note);
        mNotesRemoteDataSource.saveNote(note);
        mNotesLocalDataSource.saveNote(note);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedNotes == null) {
            mCachedNotes = new LinkedHashMap<>();
        }
        mCachedNotes.put(note.getId(), note);

    }

    @Override
    public void completeNote(@NonNull Note note) {

    }

    @Override
    public void completeNote(@NonNull String taskId) {

    }

    @Override
    public void activateNote(@NonNull Note note) {

    }

    @Override
    public void activateNote(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedNotes() {

    }

    @Override
    public void refreshNotes() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllNotes() {

    }

    @Override
    public void deleteNote(@NonNull String taskId) {

    }
}
