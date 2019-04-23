import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[]args)throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please Enter the Support, like: 100");
        int min_sup = Integer.valueOf(br.readLine());
        System.out.println("Please Enter the Confidence, like: 0.4");
        float Con = Float.valueOf(br.readLine());
        System.out.println("Which DadaSet do you want to mine? Enter \"1\" for GroceryStore or \"2\" for UNIX_usage");
        int DSNo = Integer.valueOf(br.readLine());
        System.out.println("Please Enter the Path of the file you want to mine, like: \"C:\\Users\\13668\\OneDrive\\文档\\数据挖掘导论\\dataset\\GroceryStore\\Groceries.csv\" or " +
                "\"C:\\Users\\13668\\OneDrive\\文档\\数据挖掘导论\\dataset\\UNIX_usage\\USER0\\sanitized_all.981115184025\"");
        String PathName = br.readLine();
        System.out.println("Which Method do you want to use? Enter  \"0\" for naiveMethod \"1\" for Apriori or \"2\" for FPGrowth");
        int method = Integer.valueOf(br.readLine());
        long startTime=System.nanoTime();   //获取开始时间;

        if(method==0){
            naiveMethod miner = new naiveMethod(min_sup,Con,DSNo,PathName);
            miner.naiveMining();
        }
        else if (method == 1) {
            Apriori miner = new Apriori(min_sup,Con,DSNo,PathName);
            miner.AprioriMining();
        } else if (method == 2) {
            FPGrowth miner = new FPGrowth(min_sup, Con, DSNo, PathName);
            miner.FPGrowthMining();
        }

        long endTime=System.nanoTime(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ns");

        System.out.println("The rules have been saved in file \"rules.txt\"");
    }
}
