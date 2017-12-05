package com.quartzodev.xyznotes.addeditnote;

import com.quartzodev.xyznotes.BasePresenter;
import com.quartzodev.xyznotes.BaseView;

/**
 * Created by victoraldir on 03/12/2017.
 */

public interface AddEditNoteContract {

    interface View extends BaseView<Presenter> {

        void showEmptyNoteError();

        void showNotesList();

        boolean isActive();

        void setContent(String content);

    }


    interface Presenter extends BasePresenter {
        void saveNote(String content);

        void populateNote();
    }
}
