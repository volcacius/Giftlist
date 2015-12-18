package it.polimi.dima.giftlist.ui.main;

import java.util.List;

import it.polimi.dima.giftlist.data.model.Ribot;
import it.polimi.dima.giftlist.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
