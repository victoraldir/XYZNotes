/*
 * Copyright 2017, The Android Open Source Project
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

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.quartzodev.xyznotes.data.Note;

import java.util.List;

/**
 * Data Access Object for the tasks table.
 */
@Dao
public interface NotesDao {

    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM notes")
    List<Note> getTasks();

    /**
     * Select a task by id.
     *
     * @param noteId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getNoteById(Long noteId);

    /**
     * Insert a note in the database. If the note already exists, replace it.
     *
     * @param note the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    /**
     * Update a task.
     *
     * @param task task to be updated
     * @return the number of tasks updated. This should always be 1.
     */
    @Update
    int updateTask(Note task);

    /**
     * Delete a note by id.
     *
     * @return the number of notes deleted. This should always be 1.
     */
    @Query("DELETE FROM notes WHERE id = :noteId")
    int deleteNoteById(String noteId);

}
