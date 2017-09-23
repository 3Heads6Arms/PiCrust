package com.anhhoang.picrust.data.source;

import java.util.Collection;
import java.util.List;

/**
 * Created by anh.hoang on 9/22/17.
 */

public interface BaseDataSource<T> {
    void get(ResultsCallback<T> callback);

    void get(int id, ResultCallback<T> callback);

    void save(Collection<T> entities);

    void refresh();

    interface ResultsCallback<T> {
        void onLoaded(List<T> result);

        void onDataNotAvailable(Object additionalInfo);
    }

    interface ResultCallback<T> {
        void onLoaded(T result);

        void onDataNotAvailable(Object additionalInfo);
    }
}
