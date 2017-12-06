package com.quartzodev.xyznotes.notes;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quartzodev.xyznotes.R;
import com.quartzodev.xyznotes.SingleLiveEvent;
import com.quartzodev.xyznotes.data.Note;

import java.util.List;

/**
 * Created by victoraldir on 04/12/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> mNotes;
    private NotesViewModel mNotesViewModel;

    public NoteAdapter(List<Note> notes, NotesViewModel notesViewModel){
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item_grid, parent, false);
        return new NoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Note note = mNotes.get(position);

        holder.mNoteItemPreview.setText(note.getContent());
        holder.mNoteItemToolbar.setTitle("Title goes here");
        holder.mNoteItemToolbar.setSubtitle("10/10/2017 12:00");
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNotesViewModel.getOpenNoteEvent().setValue(note.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotes == null ? 0 : mNotes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView mNoteItemPreview;
        final Toolbar mNoteItemToolbar;
        final View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mNoteItemPreview = itemView.findViewById(R.id.note_preview);
            mNoteItemToolbar = itemView.findViewById(R.id.toolbar_note_preview);
        }
    }
}
