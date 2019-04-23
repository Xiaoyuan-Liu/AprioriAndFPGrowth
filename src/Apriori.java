//Apriori频繁项集挖掘

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class Apriori {
    private int min_sup;
    private float Con;
    private HashSet<String> set;
    private DataBase DB;
    long time;
    Apriori(int min_sup,float con, int DSNo,String PathName)throws IOException{
        this.min_sup=min_sup;
        Con=con;
        set=new HashSet<String>();
        DB=new DataBase(PathName,DSNo);
    }
    private void LoadData(String PathName) throws IOException{
        BufferedReader reader=new BufferedReader(new FileReader(new File(PathName)));

    }

    private ArrayList<Itemset> findFrequent1Itemsets(){
        ArrayList<Itemset> frequent1Itemsets=new ArrayList<Itemset>();//记录1频繁项集
        for(int i = 0; i < DB.getSize();i++){

            Itemset itemset=DB.getItemset(i);//从数据库中取出一条数据
            for(int j = 0; j < itemset.getSize();j++){
                String item= itemset.getItem(j);//对数据库中每条数据的每个项
                if(!set.contains(item)){//如果项之前没遇到过
                    set.add(item);
                    Itemset frequentItem=new Itemset(item);
                    frequent1Itemsets.add(frequentItem);
                }

                else{//如果之前遇到过
                    for(int k = 0; k < frequent1Itemsets.size();k++){
                        if(frequent1Itemsets.get(k).getItem(0).equals(item)) {
                            frequent1Itemsets.get(k).incCount(1);
                            break;
                        }
                    }
                }
            }
        }
        for(int i=0;i<frequent1Itemsets.size();i++){
            if(frequent1Itemsets.get(i).getCount()<min_sup)
                frequent1Itemsets.remove(i);
        }
        return frequent1Itemsets;
    }

    private ArrayList<Itemset> aprioriGen(ArrayList<Itemset> L){
        ArrayList<Itemset> C=new ArrayList<Itemset>();
        for(int i=0;i<L.size();i++){
            for(int j=i+1;j<L.size();j++){
                    boolean patternOK = true;
                    Itemset set1=L.get(i);
                    Itemset set2=L.get(j);
                    for (int k =0;k<set1.getSize()-1;k++){
                        if(!set1.getItem(k).equals(set2.getItem(k))){
                            patternOK=false;
                            break;
                        }
                    }
                    if(patternOK){
                        Itemset mergeSet=new Itemset();
                        for(int k=0;k<set1.getSize();k++){
                            mergeSet.addItem(set1.getItem(k));
                        }
                        mergeSet.addItem(set2.getItem(set2.getSize()-1));
                        if(!mergeSet.hasInfrequentSubset(L))
                            C.add(mergeSet);
                    }

            }
        }

        return C;
    }
    public ArrayList<Itemset> frequentSubset(){
        ArrayList<ArrayList<Itemset>> L=new ArrayList<ArrayList<Itemset>>();
        ArrayList<Itemset> L1=findFrequent1Itemsets();
        //排序
        Collections.sort(L1, new Comparator<Itemset>() {
            @Override
                public int compare(Itemset o1, Itemset o2) {
                int result=o1.getItem(0).compareTo(o2.getItem(0));
                if(result>0)
                    return 1;
                else return -1;
            }
        });
        //QuickSort.qsort(L1,0,L1.size()-1);
        L.add(L1);
        for(int i = 1;!L.get(i-1).isEmpty();i++){
            System.out.println(i);
            ArrayList<Itemset> C=aprioriGen(L.get(i-1));
            for(int j = 0;j<DB.getSize();j++){
                for(int k = 0;k < C.size();k++){
                    if(DB.getItemset(j).contains(C.get(k))){
                        C.get(k).incCount(1);
                    }
                }
            }
            for(int j = 0; j < C.size();j++){
                if(C.get(j).getCount()<min_sup) {
                    C.remove(j);
                    j--;
                }
            }
            L.add(C);
        }
        for(int i = 0; i <L.get(L.size()-2).size();i++){
            Itemset tmp = L.get(L.size()-2).get(i);
            for(int j = 0; j <tmp.getSize();j++){
                System.out.print(tmp.getItem(j)+",");
            }
            System.out.println(tmp.getCount());
        }
        L.remove(L.size()-1);
        ArrayList<Itemset>res = new ArrayList<Itemset>();
        for(int i = 0;i < L.size();i++){
            ArrayList<Itemset>Li=L.get(i);
            for(int j=0;j<Li.size();j++){
                res.add(Li.get(j));
            }
        }
        return res;
    }
    public void AprioriMining()throws IOException{
        long startTime = System.nanoTime();
        ArrayList<Itemset>frequentSet=frequentSubset();
        long endTime=System.nanoTime(); //获取结束时间
        System.out.println("Apriori频繁项集挖掘时间： "+(endTime-startTime)+"ns");
        saveFrequentSet(frequentSet);
        ArrayList<ArrayList<Itemset>> confidenceSet = confidence.calculateConfidence(DB,frequentSet,Con);
        saveCon(confidenceSet);
        System.out.println("程序运行时间： "+time+"ns");
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


        Apriori miner = new Apriori(min_sup,Con,DSNo,PathName);
        miner.AprioriMining();
        System.out.println("The rules have been saved in file \"rules.txt\"");

    }
}
