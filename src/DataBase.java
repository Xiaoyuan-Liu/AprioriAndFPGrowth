import java.io.*;
import java.util.ArrayList;

public class DataBase {
    ArrayList<Itemset> database;
    DataBase(){

    }
    DataBase(DataBase DB){
        database=new ArrayList<Itemset>();
        for(int i = 0; i < DB.getSize();i++)
        database.add(new Itemset(DB.getItemset(i)));
    }
    DataBase(String PathName,int DSNo)throws IOException {
        database=new ArrayList<Itemset>();
        if(DSNo==1)
            setUpDBForGrocery(PathName);
        else if(DSNo==2)
            setUpDBForUNIX(PathName);
        /*
        BufferedReader reader=new BufferedReader(new FileReader(new File(PathName)));
        String itemsetString = reader.readLine();
        while((itemsetString=reader.readLine())!=null) {
            itemsetString = itemsetString.replace("\"", "");
            itemsetString = itemsetString.substring(itemsetString.indexOf('{') + 1, itemsetString.indexOf('}'));
            String[] str = itemsetString.split(",");
            Itemset itemset = new Itemset();
            for (int i = 0; i < str.length; i++) {

                itemset.addItem(str[i]);
            }
            itemset.incCount(1);
            database.add(itemset);
        }*/
    }
    private void setUpDBForGrocery(String PathName)throws IOException{
        BufferedReader reader=new BufferedReader(new FileReader(new File(PathName)));
        String itemsetString = reader.readLine();
        while((itemsetString=reader.readLine())!=null) {
            itemsetString = itemsetString.replace("\"", "");
            itemsetString = itemsetString.substring(itemsetString.indexOf('{') + 1, itemsetString.indexOf('}'));
            String[] str = itemsetString.split(",");
            Itemset itemset = new Itemset();
            for (int i = 0; i < str.length; i++) {

                itemset.addItem(str[i]);
            }
            itemset.incCount(1);
            database.add(itemset);
        }
    }
    private void setUpDBForUNIX(String PathName)throws IOException{
        BufferedReader reader=new BufferedReader(new FileReader(new File(PathName)));
        String item;
        while((item=reader.readLine())!=null) {
            if(item.contains("SOF")) {
                Itemset itemset = new Itemset();
                item = reader.readLine();
                while (!item.contains("EOF")) {
                    if(!itemset.hasItem(item))
                        itemset.addItem(item);
                    item = reader.readLine();
                }
                itemset.incCount(1);
                database.add(itemset);
            }
        }
    }
    public void setDB(ArrayList<Itemset>db){
        database=db;
    }
    public int getSize(){
        return database.size();
    }
    public Itemset getItemset(int index){
        return database.get(index);
    }
    public void show(){
        for(int i = 0;i <database.size();i++){
            Itemset set=database.get(i);
            for(int j = 0; j < set.getSize();j++){
                System.out.print(set.getItem(j)+",");
            }
            System.out.println("");
        }
    }
    public ArrayList<Itemset> getItems(){
        return database;
    }
    public static void main(String[] args)throws Exception{
        String PathName=null;
        BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
        PathName = br.readLine();
        DataBase DB=new DataBase(PathName,1);
        DB.show();

    }
}
