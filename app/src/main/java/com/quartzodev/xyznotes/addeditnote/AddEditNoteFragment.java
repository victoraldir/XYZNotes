package com.quartzodev.xyznotes.addeditnote;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.quartzodev.xyznotes.R;
import com.quartzodev.xyznotes.view.LinedEditText;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditNoteFragment extends Fragment implements AddEditNoteContract.View {

    private AddEditNoteContract.Presenter mPresenter;
    private ConstraintLayout mContainer;
    private LinedEditText mContent;

    public AddEditNoteFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static AddEditNoteFragment newInstance() {
        return new AddEditNoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.add_edit_note_frag, container, false);

        mContainer = root.findViewById(R.id.add_edit_note_container);
        mContent = root.findViewById(R.id.add_note_content);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_edit_note_menu, menu);
    }

    @Override
    public void setPresenter(AddEditNoteContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showEmptyNoteError() {
        Snackbar.make(mContainer, getString(R.string.empty_note_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNotesList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setContent(String content) {
        mContent.setText(content);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(R.id.action_save_note == id){
            mPresenter.saveNote(mContent.getText().toString());
        }

        return super.onOptionsItemSelected(item);
    }
}
