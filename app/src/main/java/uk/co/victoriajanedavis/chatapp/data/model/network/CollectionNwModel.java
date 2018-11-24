package uk.co.victoriajanedavis.chatapp.data.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CollectionNwModel<T> {

    @SerializedName("count") @Expose private int total;
    @SerializedName("next") @Expose private String next;
    @SerializedName("previous") @Expose private String previous;
    @SerializedName("results") @Expose private List<T> dataList;


    public CollectionNwModel() {
        dataList = new ArrayList<>();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<T> getData() {
        return dataList;
    }

    public void setData(List<T> dataList) {
        this.dataList = dataList;
    }
}

