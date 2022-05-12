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
        int samplesNum = 20 ;   /* Number of samples to run. */
        int sampleSize = 100;   /* The size of each sample (number of elements to be hashed). */
        HashTableLinear[] HashTables = new HashTableLinear[samplesNum]; /* The hash tables array (one table for each sample). */

        /* Constructing each Hash Table with the sample size. */
        for(int i = 0; i < HashTables.length; i++)
            HashTables[i] = new HashTableLinear(sampleSize);
        /* Building the samples with RANDOM values. */
        int samples[][] = new int[samplesNum][sampleSize];
        build_samples(samples);
        /* Testing Loop. */
        for(int k = 0; k < samples.length; k++){
            for(int l = 0; l < samples[0].length; l++)
                try{
                    HashTables[k].hash(samples[k][l]); // Hashing the element.
                }
                catch(Exception e){ e.printStackTrace(); }
            System.out.println("collisions = " + HashTables[k].collisions + " of HashTable " + (k+1) + " <------------------");
        }
    }
}
