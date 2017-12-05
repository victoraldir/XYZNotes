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

package com.quartzodev.xyznotes.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.quartzodev.xyznotes.data.Note;
import com.quartzodev.xyznotes.data.source.NotesDataSource;
import com.quartzodev.xyznotes.data.source.local.NotesDao;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class NotesRemoteDataSource implements NotesDataSource {

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static NotesRemoteDataSource INSTANCE;

    private NotesDao notesDao;

    private final static Map<Long, Note> NOTES_SERVICE_DATA;

    static {
        NOTES_SERVICE_DATA = new LinkedHashMap<>(2);
        addNote("This is a real note coming from the cloud!");
        addNote("Have to keep it safe :) . I am also comming from the remote datasource");
    }

    private static void addNote(String content) {
        Note newNote = new Note(content);
        NOTES_SERVICE_DATA.put(newNote.getId(), newNote);
    }

    @Override
    public void getNotes(@NonNull LoadNotesCallback callback) {

    }

    @Override
    public void getNote(@NonNull Long noteId, @NonNull final GetNoteCallback callback) {
        final Note note = NOTES_SERVICE_DATA.get(noteId);

        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onNoteLoaded(note);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveNote(@NonNull Note note) {
        NOTES_SERVICE_DATA.put(note.getId(), note);
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
