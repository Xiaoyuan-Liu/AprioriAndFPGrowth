import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class QuickSort{
	int begin,  end;
	private int[] array;
	CountDownLatch mergeSignal;
	QuickSort(int[] array, int begin, int end, CountDownLatch mergeSignal){
		this.array = array;
		this.begin = begin;
		this.end = end;
		this.mergeSignal = mergeSignal;
	}
	private static void Swap(ArrayList<String> L1, int p1,int p2) {

		String tmp = L1.get(p1);
		L1.set(p1,L1.get(p2));
		L1.set(p2,tmp);
		//array[p1] = array[p2];
		//array[p2] = tmp;
	}
	public static int partition(ArrayList<String> L1, int p, int q, ArrayList<Itemset>Priority) {
		String pivot = L1.get(q);
		int pivotRank=-1;
		for(int i = 0; i < Priority.size();i++){
			if(Priority.get(i).getItem(0).equals(pivot)) {
				pivotRank = i;
				break;
			}
		}
		int i = p - 1;
		for(int j = p; j <= q - 1;j++) {
			int rank = -1;
			for(int k = 0; k < Priority.size();k++){
				if(Priority.get(k).getItem(0).equals(L1.get(j))) {
					rank = k;
					break;
				}

			}
			if(rank<pivotRank) {
				i++;
				Swap(L1,j,i);
			}
		}
		Swap(L1,q,i + 1);
		return i + 1;
	}
	private static void quickSort(ArrayList<String> L1, int p, int q, ArrayList<Itemset>Priority) {
		if(p < q) {
			int r = partition(L1, p, q, Priority);
			quickSort(L1, p, r - 1, Priority);
			quickSort(L1, r + 1, q, Priority);
		}
	}
	public static void qsort(ArrayList<String> L1, int p, int q,ArrayList<Itemset>Priority) {
		quickSort(L1,p,q,Priority);
	}

}
