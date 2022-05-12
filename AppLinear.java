import java.util.Random;

public class AppLinear {

    /* Stores the random samples in the "samples" 2D-Array. */
    static void build_samples(int[][] samples){
        int max = (int)Math.pow(2, 32);
        Random rand = new Random();
        for(int s = 0; s < samples.length; s++) //for each sample 
            for(int e = 0; e < samples[0].length; e++) //for each element
                samples[s][e] = rand.nextInt(max);
    }

    public static void main(String[] args) {
        HashTableLinear HashTableLinear = new HashTableLinear(100);
        /* 1st dimension: [number of samples]
           2nd dimension: [number of elements, to be hashed, in samples]. */
        int samples[][] = new int[20][100];
        build_samples(samples);
        //Test Loop
        for(int k = 0; k < samples.length; k++){
            for(int i = 0; i < samples[0].length; i++){
                try{ HashTableLinear.hash(samples[k][i]); }
                catch(Exception e){ e.printStackTrace(); }
            }
            HashTableLinear.hashBucketes(); //end of insertions
            if(k < 9) System.out.print(" ");
            System.out.println(k+1 +")  "+ HashTableLinear.collisions);
            HashTableLinear = new HashTableLinear(100);
        }
    }
}
