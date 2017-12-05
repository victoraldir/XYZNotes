/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quartzodev.xyznotes.data.source.local;

import android.support.annotation.NonNull;

import com.quartzodev.xyznotes.data.Note;
import com.quartzodev.xyznotes.data.source.NotesDataSource;
import com.quartzodev.xyznotes.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class NotesLocalDataSource implements NotesDataSource {

    private static volatile NotesLocalDataSource INSTANCE;

    private NotesDao mNotesDao;

    private AppExecutors mAppExecutors;

    private NotesLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull NotesDao notesDao){
        mAppExecutors = appExecutors;
        mNotesDao = notesDao;
    }

    public static NotesLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull NotesDao notesDao){

        synchronized (NotesLocalDataSource.class) {
            if (INSTANCE == null)
                INSTANCE = new NotesLocalDataSource(appExecutors, notesDao);
        }

        return INSTANCE;
    }

    @Override
    public void getNotes(@NonNull final LoadNotesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Note> tasks = mNotesDao.getTasks();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (tasks.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onNotesLoaded(tasks);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getNote(@NonNull final Long noteId, @NonNull final GetNoteCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Note note = mNotesDao.getNoteById(noteId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (note != null) {
                            callback.onNoteLoaded(note);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveNote(@NonNull final Note note) {

        checkNotNull(note);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mNotesDao.insertNote(note);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);

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

    }

    @Override
    public void deleteAllNotes() {

    }

    @Override
    public void deleteNote(@NonNull String taskId) {

    }
}
