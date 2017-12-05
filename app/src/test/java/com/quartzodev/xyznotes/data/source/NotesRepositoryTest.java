package com.quartzodev.xyznotes.data.source;

import com.quartzodev.xyznotes.data.Note;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Created by victoraldir on 02/12/2017.
 */
public class NotesRepositoryTest {

    private final static String NOTE_CONTENT1 = "Note Content 1";
    private final static String NOTE_CONTENT2 = "Note Content 1";
    private final static String NOTE_CONTENT3 = "Note Content 2";

    private NotesRepository mNotesRepository;

    @Mock
    private NotesDataSource mNotesRemoteDataSource;

    @Mock
    private NotesDataSource mNotesLocalDataSource;

    @Before
    public void setup(){

        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mNotesRepository = NotesRepository.getInstance(mNotesRemoteDataSource, mNotesLocalDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        NotesRepository.destroyInstance();
    }


    @Test
    public void shouldSaveNoteAndSaveNoteAPI() {
        // Given a stub note with a content
        Note newNote = new Note(NOTE_CONTENT1);

        // When a note is saved to the notes repository
        mNotesRepository.saveNote(newNote);

        // Then the service API and persistent repository are called and the cache is updated
        verify(mNotesRemoteDataSource).saveNote(newNote);
        verify(mNotesLocalDataSource).saveNote(newNote);
        assertThat(mNotesRepository.mCachedNotes.size(), is(1));
    }
}