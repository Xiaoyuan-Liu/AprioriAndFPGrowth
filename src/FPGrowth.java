import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.spec.DSAGenParameterSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class FPGrowth {
    int min_sup;
    DataBase DB;
    ArrayList<Itemset> L;
    ArrayList<Itemset> frequentSet;
    float Con;
    //private HashSet<String> set;
    //FPTreeNode FPTreeRoot;
    FPGrowth(int min,float con,int DSNo,String PathName)throws IOException {
        min_sup=min;
        Con=con;
        //FPTreeRoot =new FPTreeNode();
        DB=new DataBase(PathName, DSNo);
        //set=new HashSet<String>();
        L=findFrequent1Itemsets();

        //cleanData();
        FPTree FPT=new FPTree(min_sup,new DataBase(DB),new ArrayList<Itemset>(L));
        //FPT.show();
        //setUpTree();
    }
    /*
    private void insert_tree(Itemset item, FPTreeNode root){
        int index;
        FPTreeNode newRoot;
        if((index=root.hasChild(item.getItem(0)))>-1){
            newRoot = root.getNode(index);
            newRoot.incCount();
        }
        else{
            newRoot = new FPTreeNode(item.getItem(0));
            root.addNode(newRoot);
        }
        item.remove(0);
        if(item.getSize()>0)
            insert_tree(item,newRoot);
    }*/
    private void cleanData() {//去掉非频繁一项集

        for (int i = 0; i < DB.getSize(); i++) {
            Itemset itemSet = DB.getItemset(i);
            for (int j = 0; j < itemSet.getSize(); j++) {
                if (!L.contains(itemSet.getItem(j)))
                    itemSet.remove(j);
            }
        }
    }
    /*
    private void setUpTree(){
        cleanData();
        for(int i = 0; i < DB.getSize();i++){
            QuickSort.qsort(DB.getItemset(i).getItems(),0,DB.getItemset(i).getSize()-1,L);
            Itemset item = DB.getItemset(i);
            insert_tree(item,FPTreeRoot);
        }
        return;
    }*/
    private ArrayList<Itemset> findFrequent1Itemsets(){
        HashSet<String>sset = new HashSet<String>();
        ArrayList<Itemset> frequent1Itemsets=new ArrayList<Itemset>();//记录1频繁项集
        for(int i = 0; i < DB.getSize();i++){
            Itemset itemset=DB.getItemset(i);//从数据库中取出一条数据
            for(int j = 0; j < itemset.getSize();j++){
                String item= itemset.getItem(j);//对数据库中每条数据的每个项
                if(!sset.contains(item)){//如果项之前没遇到过
                    sset.add(item);
                    Itemset frequentItem=new Itemset(item);
                    frequentItem.setCount(itemset.getCount());
                    frequent1Itemsets.add(frequentItem);
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
            if(frequent1Itemsets.get(i).getCount()<min_sup) {
                frequent1Itemsets.remove(i);
                i--;
            }
        }

        for(int i = 0; i < frequent1Itemsets.size();i++){
            sset.add(frequent1Itemsets.get(i).getItem(0));
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
        for(int i = 0; i < DB.getSize();i++) {
            QuickSort.qsort(DB.getItemset(i).getItems(), 0, DB.getItemset(i).getItems().size() - 1, frequent1Itemsets);
        }
        return frequent1Itemsets;
    }
    private ArrayList<Itemset> FPMining(ArrayList<Itemset> recorder){
        ArrayList<Itemset> res=new ArrayList<Itemset>();
        ArrayList<Itemset> copyRecorder=new ArrayList<Itemset>();
        for(int i = 0; i < recorder.size();i++)
            copyRecorder.add(new Itemset(recorder.get(i)));
        FPTree tree= new FPTree(min_sup,copyRecorder);
        ArrayList<Itemset> frequent1Set=tree.getFrequent1Set();

        for(int i = 0;i < frequent1Set.size();i++){
            res.add(frequent1Set.get(i));
        }

        for(int i = frequent1Set.size()-1;i>=0;i--){
            Itemset tmp = frequent1Set.get(i);
            FPTreeNode head=tmp.getNextNode();
            ArrayList<Itemset> subDB = new ArrayList<Itemset>();
            while(head!=null){
                Itemset subItemset = new Itemset(head.getCount());
                FPTreeNode father = head.getFatherNode();
                while(father.getItem()!=null) {
                    subItemset.addItem(father.getItem());
                    father=father.getFatherNode();
                }
                if(subItemset.getSize()>0) {
                    Itemset verseSubItemset = new Itemset(subItemset.getCount());
                    for(int j = 0; j < subItemset.getSize();j++){
                        verseSubItemset.addItem(subItemset.getItem(subItemset.getSize()-1-j));
                    }
                    subDB.add(verseSubItemset);
                }
                head = head.getNextNode();
            }
            ArrayList<Itemset>resFront = FPMining(subDB);
            for(int j = 0; j < resFront.size();j++){
                resFront.get(j).addItem(tmp.getItem(0));
                res.add(resFront.get(j));
            }
        }
        return res;
    }
    public ArrayList<Itemset> FPGrowthMining()throws IOException{
        frequentSet= FPMining(DB.getItems());
        for(int i = 0; i < frequentSet.size();i++){
            if(frequentSet.get(i).getSize()<2){
                frequentSet.remove(i);
                i--;
            }
        }
        saveFrequentSet(frequentSet);
        calculateConfidence();
        return frequentSet;
    }
    public void calculateConfidence()throws IOException{
        for(int i = 0;i<L.size();i++){
            frequentSet.add(L.get(i));
        }
        ArrayList<ArrayList<Itemset>> confidenceSet = confidence.calculateConfidence(DB,frequentSet,Con);
        /*
        for(int i = 0; i < frequentSet.size();i++){
            for(int j = 0; j < frequentSet.size();i++){
                if(i!=j){
                    Itemset AandB=new Itemset();
                    for(int k = 0; k < frequentSet.get(i).getSize();k++){
                        AandB.addItem(frequentSet.get(i).getItem(k));
                    }
                    for(int k = 0; k < frequentSet.get(j).getSize();k++){
                        AandB.addItem(frequentSet.get(j).getItem(k));
                    }
                    for(int k = 0;k<frequentSet.size();k++){

                    }
                }2

            }
        }*/
        saveCon(confidenceSet);
    }
    public void saveCon(ArrayList<ArrayList<Itemset>> confidenceSet)throws IOException{

        String PathName = "rules.txt";
        PrintWriter pw=new PrintWriter(PathName);
        for(int i = 0;i<confidenceSet.size();i++){
            ArrayList<Itemset> rule = confidenceSet.get(i);
            Itemset A=rule.get(0);
            Itemset B=rule.get(1);
            //pw.write(A.getConfidence());
            pw.print("confidence="+A.getConfidence()+":\t");
            for(int j = 0; j < A.getSize();j++){
                pw.write(A.getItem(j)+",");
            }
            pw.write("--->");
            for(int j = 0; j < B.getSize();j++){
                pw.write(B.getItem(j)+",");
            }
            pw.write("\r\n");
        }

            pw.close();
    }
    public void saveFrequentSet(ArrayList<Itemset> frequentSet)throws IOException{

        String PathName = "frequentSet.txt";
        PrintWriter pw=new PrintWriter(PathName);
        for(int i = 0; i < frequentSet.size();i++)
        {
            pw.write(frequentSet.get(i).getCount()+"\"");
            for(int j = 0; j < frequentSet.get(i).getSize();j++)
            {
                pw.write(frequentSet.get(i).getItem(j)+",");
            }
            pw.write("\"\r\n");
        }
        pw.close();
    }
    public static void main(String[] args)throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please Enter the Support, like: 100");
        int min_sup=Integer.valueOf(br.readLine());
        System.out.println("Please Enter the Confidence, like: 0.4");
        float Con=Float.valueOf(br.readLine());
        System.out.println("Which DadaSet do you want to mine? Enter \"1\" for GroceryStore or \"2\" for UNIX_usage");
        int DSNo = Integer.valueOf(br.readLine());
        System.out.println("Please Enter the Path of the file you want to mine, like: \"C:\\Users\\13668\\OneDrive\\文档\\数据挖掘导论\\dataset\\GroceryStore\\Groceries.csv\" or " +
                "\"C:\\Users\\13668\\OneDrive\\文档\\数据挖掘导论\\dataset\\UNIX_usage\\USER0\\sanitized_all.981115184025\"");
        String PathName = br.readLine();
        FPGrowth miner = new FPGrowth(min_sup,Con,DSNo,PathName);
        miner.FPGrowthMining();
        System.out.println("The rules have been saved in file \"rules.txt\"");
    }
}
