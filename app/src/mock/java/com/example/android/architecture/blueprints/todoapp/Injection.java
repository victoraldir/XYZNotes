/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.example.android.architecture.blueprints.todoapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.architecture.blueprints.todoapp.data.FakeNotesRemoteDataSource;
import com.quartzodev.xyznotes.data.source.NotesRepository;
import com.quartzodev.xyznotes.data.source.local.NotesLocalDataSource;
import com.quartzodev.xyznotes.data.source.local.XYZNotesDatabase;
import com.quartzodev.xyznotes.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link com.quartzodev.xyznotes.data.source.NotesDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static NotesRepository provideNotesRepository(@NonNull Context context) {
        checkNotNull(context);
        XYZNotesDatabase database = XYZNotesDatabase.getInstance(context);
        return NotesRepository.getInstance(FakeNotesRemoteDataSource.getInstance(),
                NotesLocalDataSource.getInstance(new AppExecutors(),
                        database.noteDao()));
    }
}
