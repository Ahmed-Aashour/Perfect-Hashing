import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {
        HashTable h = new HashTable(100);
        //int min = 0;
        int max = (int)Math.pow(2, 32);
        Random rand = new Random();
        for(int k = 0; k < 50; k++)
        {
            for(int i = 0; i < 100; i++)
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
            System.out.println(h.collisions);
            h = new HashTable(100);
        }
    }
}
