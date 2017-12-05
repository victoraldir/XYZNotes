package com.quartzodev.xyznotes.notes;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import com.quartzodev.xyznotes.data.Note;

import java.util.List;

/**
 * Created by victoraldir on 04/12/2017.
 */

public class NotesListBindings {

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(ListView listView, List<Note> items) {
        NoteAdapter adapter = (NoteAdapter) listView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(items);
        }
    }

}
