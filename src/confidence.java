import java.util.ArrayList;

public class confidence {
    //由index0推出index1的confidence
    public static ArrayList<ArrayList<Itemset>> calculateConfidence(DataBase DB,ArrayList<Itemset>frequentSet,float con){

        ArrayList<ArrayList<Itemset>> res = new ArrayList<ArrayList<Itemset>>();

        for(int i = 0; i < frequentSet.size();i++){
            for(int j = 0; j < frequentSet.size();j++){
                if(i!=j){
                    Itemset A=frequentSet.get(i);
                    Itemset B=frequentSet.get(j);
                    if(!A.hasSameItem(B)){
                        Itemset AandB=new Itemset();
                        for(int k = 0; k < A.getSize();k++){
                           AandB.addItem(A.getItem(k));
                        }
                        for(int k = 0; k < B.getSize();k++){
                            AandB.addItem(B.getItem(k));
                        }
                        int index=-1;
                        for(int k = 0;k<frequentSet.size();k++){
                            if(frequentSet.get(k).contains(AandB)&&(AandB.contains(frequentSet.get(k)))){
                                index=k;
                                break;
                            }
                        }
                        if(index>-1) {
                            AandB=frequentSet.get(index);
                            float Con = (float) AandB.getCount() / A.getCount();
                            System.out.println(Con);
                            System.out.print("confidence="+A.getConfidence()+": ");
                            for(int k = 0; k < A.getSize();k++){
                                System.out.print(A.getItem(k)+",");
                            }
                            System.out.print("--->");
                            for(int k = 0; k < B.getSize();k++){
                                System.out.print(B.getItem(k)+",");
                            }
                            System.out.println(" ");
                            if (Con >= con) {
                                A.setConfidence(Con);
                                ArrayList<Itemset> tmp = new ArrayList<Itemset>();
                                tmp.add(new Itemset(A));
                                tmp.add(new Itemset(B));
                                res.add(tmp);
                            }
                        }
                    }
                }
            }
        }



        return res;
    }
    public static ArrayList<ArrayList<Itemset>> calculateConfidence(ArrayList<Itemset> DB,ArrayList<Itemset>frequentSet){
        ArrayList<ArrayList<Itemset>> res = new ArrayList<ArrayList<Itemset>>();
        return res;
    }
}
