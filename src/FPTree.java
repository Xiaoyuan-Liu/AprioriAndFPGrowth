import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class FPTree {
    int min_sup;
    FPTreeNode FPTreeRoot;
    ArrayList<Itemset> DB;
    ArrayList<Itemset> frequent1;
    private HashSet<String> set;
    PrintWriter pw;
    FPTree(int min,ArrayList<Itemset> DB){
        min_sup=min;
        FPTreeRoot=new FPTreeNode();
        this.DB=DB;
        set=new HashSet<String>();
        frequent1=findFrequent1Itemsets();
        cleanData();
        if(!DB.isEmpty())
            setUpTree();
    }

    FPTree(int min,DataBase DB){
        min_sup=min;
        FPTreeRoot=new FPTreeNode();
        this.DB=DB.getItems();
        set=new HashSet<String>();
        set.clear();
        frequent1=findFrequent1Itemsets();
        setUpTree();
    }
    private void cleanData(){//去掉非频繁一项集
        for(int i = 0; i < DB.size();i++){
            Itemset itemSet=DB.get(i);
            for(int j = 0;j<itemSet.getSize();j++){
                if(!set.contains(itemSet.getItem(j)))
                    itemSet.remove(j);
            }
        }
        for(int i = 0; i < DB.size();i++){
            if(DB.get(i).getSize()==0){
                DB.remove(i);
                i--;
            }
        }
    }
    FPTree(int min,DataBase DB, ArrayList<Itemset> setOf1){
        min_sup=min;
        FPTreeRoot=new FPTreeNode();
        this.DB=DB.getItems();
        set=new HashSet<String>();
        frequent1=setOf1;
        cleanData();
        setUpTree();
    }
    FPTree(int min,ArrayList<Itemset> DB, ArrayList<Itemset> setOf1){
        min_sup=min;
        FPTreeRoot=new FPTreeNode();
        this.DB=DB;
        set=new HashSet<String>();
        frequent1=setOf1;
        setUpTree();
    }
    public ArrayList<Itemset> getFrequent1Set(){
        return frequent1;
    }
    private void insert_tree(Itemset item, FPTreeNode root){
        int index;
        FPTreeNode newRoot;
        if((index=root.hasChild(item.getItem(0)))>-1){
            newRoot = root.getNode(index);
            newRoot.incCount(item.getCount());
        }
        else{
            newRoot = new FPTreeNode(item.getCount(),item.getItem(0),root);

            //建立头表到树的联系
            Itemset headItem=null;
            for(int i = 0; i <frequent1.size();i++){
                if(item.getItem(0).equals(frequent1.get(i).getItem(0))) {
                    headItem = frequent1.get(i);
                    break;
                }
            }
            if(headItem!=null){
                root.addNode(newRoot);
            if(headItem.getNextNode()==null)
                headItem.setNextNode(newRoot);
            else{
                FPTreeNode last=headItem.getNextNode();
                while(last.getNextNode()!=null){
                    last=last.getNextNode();
                }
                last.setNextNode(newRoot);
            }}
        }Itemset nextItem = new Itemset(item);
        nextItem.remove(0);
        if(nextItem.getSize()>0)
            insert_tree(nextItem,newRoot);
    }
    private void setUpTree(){

        for(int i = 0; i < DB.size();i++){
            //QuickSort.qsort(DB.get(i).getItems(),0,DB.get(i).getSize()-1,frequent1);
            Itemset item = DB.get(i);
            insert_tree(item,FPTreeRoot);
        }
    }
    private ArrayList<Itemset> findFrequent1Itemsets(){
        ArrayList<Itemset> frequent1Itemsets=new ArrayList<Itemset>();//记录1频繁项集
        for(int i = 0; i < DB.size();i++){

            Itemset itemset=DB.get(i);//从数据库中取出一条数据
            for(int j = 0; j < itemset.getSize();j++){
                String item= itemset.getItem(j);//对数据库中每条数据的每个项
                if(!set.contains(item)){//如果项之前没遇到过
                    set.add(item);
                    Itemset frequentItem=new Itemset(item);
                    frequent1Itemsets.add(frequentItem);
                    frequentItem.setCount(itemset.getCount());
                }

                else{//如果之前遇到过
                    for(int k = 0; k < frequent1Itemsets.size();k++){
                        if(frequent1Itemsets.get(k).getItem(0).equals(item)) {
                            frequent1Itemsets.get(k).incCount(itemset.getCount());
                            break;
                        }
                    }
                }
            }
        }
        for(int i=0;i<frequent1Itemsets.size();i++){
            if(frequent1Itemsets.get(i).getCount()<min_sup){
                frequent1Itemsets.remove(i);
                i--;
            }
        }
        set.clear();
        for(int i = 0; i < frequent1Itemsets.size();i++){
            set.add(frequent1Itemsets.get(i).getItem(0));
        }
        Collections.sort(frequent1Itemsets, new Comparator<Itemset>() {
            @Override
            public int compare(Itemset o1, Itemset o2) {
                if((o1.getCount()<o2.getCount())||((o1.getCount()==o2.getCount())&&(o1.getItem(0).compareTo(o2.getItem(0))>0)))
                    return 1;
                else
                    return -1;
            }
        });
        return frequent1Itemsets;
    }
    private void show(FPTreeNode root){
        System.out.println(root.getCount()+"\""+root.getItem()+"\"");
        pw.write(root.getCount()+"\""+root.getItem()+"\""+"\r\n");
        for(int i = 0; i < root.getChildSize();i++){
            show(root.getNode(i));
        }

    }
    public void show()throws IOException {
        BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
        String PathName = br.readLine();
        pw=new PrintWriter(PathName);
        //show(FPTreeRoot);
        for(int i = 0; i < frequent1.size();i++){
            pw.write(frequent1.get(i).getCount()+"\""+frequent1.get(i).getItem(0)+"\""+"\r\n");
            System.out.print(frequent1.get(i).getCount()+" "+frequent1.get(i).getItem(0)+" ");
            FPTreeNode it=frequent1.get(i).getNextNode();
            while(it!=null){
                System.out.print(it.getCount()+"\""+it.getItem()+"\""+" ");
                it=it.getNextNode();
            }
            System.out.println(" ");
        }
        pw.close();
    }

    public static void main(String[]args)throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int min_sup=Integer.valueOf(br.readLine());
        String PathName = br.readLine();
        //FPTree miner = new FPTree(min_sup,PathName);
    }
}
