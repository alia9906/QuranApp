package ir.farzadshami.quran.helpers;

import java.util.ArrayList;
import java.util.Comparator;

public class Sort<T>{
    public void Sort(ArrayList<T> arrayList , Comparator<T> comparator){
        if(arrayList != null)
        quickSort(arrayList , 0 , arrayList.size() - 1 , comparator);
    }

    private void quickSort(ArrayList<T> arrayList , int start , int finish , Comparator<T> comparator){
        if(start < finish) {
            int p = partition(arrayList, start, finish, comparator);

            quickSort(arrayList, start, p - 1, comparator);
            quickSort(arrayList, p + 1, finish, comparator);
        }
    }
    private int partition(ArrayList<T> arr, int begin, int end , Comparator<T> comparator) {
        T pivot = arr.get(end);
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (comparator.compare(arr.get(j) , pivot) <= 0) {
                i++;

                swapArrList(arr , i , j);
            }
        }

        swapArrList(arr , i+1 , end);

        return i+1;
    }
    private void swapArrList(ArrayList<T> arr , int i , int j){
        T temp = arr.get(i);
        arr.set(i , arr.get(j));
        arr.set(j , temp);
    }
}
