import java.util.ArrayList;

public class Itemset {//项集
    private int size;
    private int count;
    private int countLeft;
    private float confidence=0;
    private ArrayList<String> items;
    private FPTreeNode nextNode;
    Itemset(){
        size=0;
        count=0;
        countLeft=0;
        items=new ArrayList<String>();
    }

    Itemset(int count){
        this.count=count;
        items=new ArrayList<String>();
    }
    Itemset(String item){
        count=1;
        countLeft=1;
        size=1;
        items=new ArrayList<String>();
        items.add(item);
    }
    Itemset(Itemset copySet){
        count = copySet.count;
        countLeft=copySet.countLeft;
        size=copySet.size;
        confidence=copySet.confidence;
        items=new ArrayList<String>();
        for(int i = 0; i < copySet.getSize();i++){
            items.add(new String(copySet.getItem(i)));
        }

    }
    public FPTreeNode getNextNode(){
        return nextNode;
    }
    public void setNextNode(FPTreeNode nextNode){
        this.nextNode=nextNode;
    }
    public ArrayList<String> getItems(){
        return items;
    }
    public int getSize() {
        if(size==items.size())
        return size;
        else return items.size();
    }
    public int getCount(){return count;}
    public boolean addItem(String item){
        size++;
        return items.add(item);
    }
    public String remove(int index){
        size--;
        return items.remove(index);
    }
    public String getItem(int index){
        if(index >= size)return null;
        return items.get(index);
    }
    public void setCount(int count){
        this.count = count;
    }
    public int incCount(int count){
        countLeft+=count;
        this.count+=count;
        return this.count;
    }
    public int decCountLeft(){
        return (countLeft--);
    }
    public boolean contains(Itemset itemset){
        for(int i = 0; i < itemset.size;i++){
            boolean contain=false;
            for(int j = 0; j < size;j++){
                if(itemset.getItem(i).equals(items.get(j))){
                    contain=true;
                    break;
                }
            }
            if(!contain)return false;
        }
        return true;
    }
    public boolean hasInfrequentSubset(ArrayList<Itemset> L){
        for(int i = 0; i < size;i++){
            Itemset subset=new Itemset(this);
            subset.remove(i);
            boolean contain=false;
            for(int j = 0; j < L.size();j++){
                if(L.get(j).contains(subset))
                    contain=true;
            }
            if(!contain)return true;
        }
        return false;
    }
    public void setConfidence(float con){
        confidence=con;
    }
    public boolean hasSameItem(Itemset B){
        for(int i = 0; i < items.size();i++){
            for(int j = 0; j < B.items.size();j++){
                if(items.get(i).equals(B.items.get(j)))
                    return true;
            }
        }
        return false;
    }
    public float getConfidence(){
        return confidence;
    }
    public boolean hasItem(String findItem){
        for(int i = 0; i < items.size();i++){
            if(items.get(i).equals(findItem))
                return true;
        }
        return false;
    }
}
