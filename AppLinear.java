import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

public class AppLinear {
    /* O(N)-Space Application */
    public static void main(String[] args) throws Exception {
        File f = new File("O(N) test cases\\testcase8.txt");
        f.createNewFile();
        FileWriter writer = new FileWriter(f);
        System.out.print("enter size : ");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        HashTableLinear hashTable = new HashTableLinear(n);
        writer.write("entered size " + n + "\n");
        writer.write("built hashtable with " + hashTable.Dictionary.length + " Buckets \n");
        int max = (int)Math.pow(2, 32);
        Random rand = new Random();
        writer.write("inserting variable length samples 20 times\n");
        for(int k = 0; k < 20; k++)
        {
            int size = rand.nextInt(hashTable.Dictionary.length);
            hashTable.setSampleSize(size);
            writer.write(k+1 + "-" + " inserting " + size + " elements\n");
            for(int i = 0; i < size; i++)
            {
                try{
                    int num  = rand.nextInt(max);
                    hashTable.hash(num);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            writer.write("   the table is O(" + (hashTable.actualSize/(n*1.00)) + " n)-Space\n");
            writer.write("   level {1,2} collisions : {" + hashTable.collisions_lvl_1 + ", " + hashTable.collisions_lvl_2 +  "}\n");
            writer.write("   number of rebuilds : " + hashTable.rebuilds + "\n");
            hashTable = new HashTableLinear(n);
        }
        sc.close();
        writer.close();
    }
}
