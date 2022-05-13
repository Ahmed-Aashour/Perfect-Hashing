import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

public class App {
    /* O(N^2)-Space Application */
    public static void main(String[] args) throws Exception {
        File f = new File("O(N2) test cases\\testcase8.txt");
        f.createNewFile();
        FileWriter writer = new FileWriter(f);
        System.out.print("enter size : ");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        HashTable h = new HashTable(n);
        writer.write("entered size " + n + "\n");
        writer.write("built hashtable with size " + h.Dictionary.length + "\n");
        writer.write("the table is " + (h.Dictionary.length/(n*1.0))/(n*1.0) + " n^2\n");
        int max = (int)Math.pow(2, 32);
        Random rand = new Random();
        writer.write("inserting variable length samples 20 times\n");
        for(int k = 0; k < 20; k++)
        {
            int size = rand.nextInt(n);
            writer.write(k+1 + "-" + " inserting " + size + " elements\n");
            for(int i = 0; i < size; i++)
            {
                try{
                int num  = rand.nextInt(max);
                h.hash(num);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            writer.write("number of rebuilds (collsions): " + h.collisions + "\n");
            h = new HashTable(n);
        }
        sc.close();
        writer.close();
    }
}
