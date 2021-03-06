package ru.vandrikeev.android.phrasebook.presentation.presenter.history;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import ru.vandrikeev.android.phrasebook.model.translations.HistoryTranslation;
import ru.vandrikeev.android.phrasebook.model.translations.TranslationRepository;
import ru.vandrikeev.android.phrasebook.presentation.presenter.RxPresenter;
import ru.vandrikeev.android.phrasebook.presentation.view.history.TranslationListView;

/**
 * Presenter for {@link TranslationListView} with recent translations.
 */
@InjectViewState
public class HistoryPresenter extends RxPresenter<TranslationListView> {

    @NonNull
    private TranslationRepository repository;

    @Inject
    public HistoryPresenter(@NonNull TranslationRepository repository) {
        this.repository = repository;
    }

    private void getTranslations() {
        dispose();
        getViewState().showLoading();

        disposable = repository.getRecents()
                .subscribe(
                        new Consumer<List<? extends HistoryTranslation>>() {
                            @Override
                            public void accept(@NonNull List<? extends HistoryTranslation> model) throws Exception {
                                if (model.isEmpty()) {
                                    getViewState().showEmpty();
                                } else {
                                    getViewState().setModel(model);
                                    getViewState().showContent();
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            @SuppressWarnings("NullableProblems")
                            public void accept(@NonNull Throwable e) throws Exception {
                                getViewState().showError(e);
                            }
                        }
                );
    }

    public void clear() {
        dispose();
        getViewState().showLoading();

        disposable = repository.clearHistory()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    @SuppressWarnings("NullableProblems")
                    public void accept(@NonNull Integer deleted) throws Exception {
                        getViewState().clearContent();
                        getViewState().showEmpty();
                    }
                });
    }

    public void setFavorite(@NonNull HistoryTranslation translation, final boolean favorite) {
        disposable = repository.setFavorite(translation, favorite)
                .subscribe(
                        new Consumer<HistoryTranslation>() {
                            @Override
                            public void accept(@NonNull HistoryTranslation translation) throws Exception {
                                getViewState().updateTranslation(translation);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable e) throws Exception {
                                getViewState().showError(e);
                            }
                        }
                );
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getTranslations();
    }
}
