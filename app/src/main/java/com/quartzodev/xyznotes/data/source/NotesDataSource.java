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

package com.quartzodev.xyznotes.data.source;

import android.support.annotation.NonNull;

import com.quartzodev.xyznotes.data.Note;

import java.util.List;


/**
 * Main entry point for accessing notes data.
 * <p>
 * For simplicity, only getNotes() and getNote() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new note is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface NotesDataSource {

    interface LoadNotesCallback {

        void onNotesLoaded(List<Note> notes);

        void onDataNotAvailable();
    }

    interface GetNoteCallback {

        void onNoteLoaded(Note note);

        void onDataNotAvailable();
    }

    void getNotes(@NonNull LoadNotesCallback callback);

    void getNote(@NonNull Long noteId, @NonNull GetNoteCallback callback);

    void saveNote(@NonNull Note note);

    void completeNote(@NonNull Note note);

    void completeNote(@NonNull String taskId);

    void activateNote(@NonNull Note note);

    void activateNote(@NonNull String taskId);

    void clearCompletedNotes();

    void refreshNotes();

    void deleteAllNotes();

    void deleteNote(@NonNull String taskId);
}
