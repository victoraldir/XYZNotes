package com.example.android.architecture.blueprints.todoapp.data;
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


import android.support.annotation.NonNull;

import com.quartzodev.xyznotes.data.Note;
import com.quartzodev.xyznotes.data.source.NotesDataSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
public class FakeNotesRemoteDataSource implements NotesDataSource {

    private static FakeNotesRemoteDataSource INSTANCE;

    private static final Map<String, Note> NOTES_SERVICE_DATA = new LinkedHashMap<>();

    // Prevent direct instantiation.
    private FakeNotesRemoteDataSource() {}

    public static FakeNotesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeNotesRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getNotes(@NonNull LoadNotesCallback callback) {

    }

    @Override
    public void getNote(@NonNull Long noteId, @NonNull GetNoteCallback callback) {
        Note note = NOTES_SERVICE_DATA.get(noteId);
        callback.onNoteLoaded(note);
    }

    @Override
    public void saveNote(@NonNull Note note) {

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
