package com.quartzodev.xyznotes.notes;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.quartzodev.xyznotes.data.Note;
import com.quartzodev.xyznotes.databinding.NoteItemBinding;

import java.util.List;

/**
 * Created by victoraldir on 04/12/2017.
 */

public class NoteAdapter extends BaseAdapter {

    private final NotesViewModel mNotesViewModel;


    private List<Note> mNotes;

    public NoteAdapter(List<Note> notes,
                       NotesViewModel notesViewModel){

        mNotes = notes;
        mNotesViewModel = notesViewModel;
    }

    public void replaceData(List<Note> notes) {
        setList(notes);
    }

    private void setList(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mNotes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        NoteItemBinding binding;

        if (view == null) {
            // Inflate
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            // Create the binding
            binding = NoteItemBinding.inflate(inflater, viewGroup, false);
        } else {
            // Recycling view
            binding = DataBindingUtil.getBinding(view);
        }

        NoteItemUserActionsListener userActionsListener = new NoteItemUserActionsListener() {
            @Override
            public void onNoteClicked(Note note) {
                mNotesViewModel.getOpenNoteEvent().setValue(note.getId());
            }
        };

        binding.setNote(mNotes.get(i));

        binding.setListener(userActionsListener);

        binding.executePendingBindings();

        return binding.getRoot();
    }
}
