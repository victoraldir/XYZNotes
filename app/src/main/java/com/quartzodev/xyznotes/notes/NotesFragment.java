package com.quartzodev.xyznotes.notes;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quartzodev.xyznotes.R;
import com.quartzodev.xyznotes.data.Note;
import com.quartzodev.xyznotes.databinding.NotesFragBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class NotesFragment extends Fragment implements Observer<List<Note>> {

    private NotesViewModel mNotesViewModel;
    private NotesFragBinding mNotesFragBinding;
    private NoteAdapter mListAdapter;

    public NotesFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mNotesViewModel.start();
    }

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mNotesFragBinding = NotesFragBinding.inflate(inflater,container,false);

        mNotesViewModel = NotesActivity.obtainViewModel(getActivity());

        mNotesFragBinding.setViewmodel(mNotesViewModel);

        subscribeNotesList();

        return mNotesFragBinding.getRoot();
    }

    public void subscribeNotesList(){
        mNotesViewModel.items.observe(getActivity(),this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupFab();
        setupListAdapter();
    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotesViewModel.addNewNote();
            }
        });
    }

    private void setupListAdapter() {
        RecyclerView listView =  mNotesFragBinding.tasksList;

        mListAdapter = new NoteAdapter(
                new ArrayList<Note>(0),
                mNotesViewModel
        );

        RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 2);

        listView.setLayoutManager(lm);

        listView.setAdapter(mListAdapter);
    }

    @Override
    public void onChanged(@Nullable List<Note> notes) {
        mListAdapter.replaceData(notes);
    }
}
