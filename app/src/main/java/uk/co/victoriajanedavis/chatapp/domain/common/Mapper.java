package uk.co.victoriajanedavis.chatapp.domain.common;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

public abstract class Mapper<E, T> {

    public abstract T mapFrom(@NonNull E from);

    public Observable<T> observable(@NonNull E from) {
        return Observable.fromCallable(() -> mapFrom(from));
    }

    public Observable<List<T>> observable(@NonNull List<E> from) {
        return Observable.fromCallable(() -> {
            List<T> mappedList = new ArrayList<>();
            for (E element : from) {
                mappedList.add(mapFrom(element));
            }
            return mappedList;
        });
        //return Observable.fromCallable { from.map { mapFrom(it) } }
    }
}
