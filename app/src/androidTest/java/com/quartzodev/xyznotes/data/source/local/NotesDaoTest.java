package com.quartzodev.xyznotes.data.source.local;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.quartzodev.xyznotes.data.Note;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by victoraldir on 03/12/2017.
 */

@RunWith(AndroidJUnit4.class)
public class NotesDaoTest {

    private static final long ID_TEST = 12;
    private static final String CONTENT_TEST = "This is a note test";

    private static final Note NOTE = new Note(ID_TEST,CONTENT_TEST);

    private XYZNotesDatabase mDatabase;

    @Before
    public void initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                XYZNotesDatabase.class).build();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void shouldPassInsertTaskAndGetById() {
        // When inserting a task
        mDatabase.noteDao().insertNote(NOTE);

        // When getting the task by id from the database
        Note loaded = mDatabase.noteDao().getNoteById(NOTE.getId());

        // The loaded data contains the expected values
        assertNote(loaded, ID_TEST,CONTENT_TEST);
    }

    @Test
    public void shouldFailInsertTaskAndGetById() {
        // When inserting a note

        long id = 11;

        Note newNote = new Note(id,"Fail");

        mDatabase.noteDao().insertNote(newNote);

        // When getting the task by id from the database
        Note loaded = mDatabase.noteDao().getNoteById(id);

        Assert.assertNotEquals(loaded.getContent(), "Diff fail");
    }

    private void assertNote(Note note, Long id, String content) {
        assertThat(note, notNullValue());
        assertThat(note.getId(), is(id));
        assertThat(note.getContent(), is(content));
    }
}