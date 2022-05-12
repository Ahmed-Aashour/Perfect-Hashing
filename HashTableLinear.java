import java.util.*;

public class HashTableLinear {
    final int MAXBITS = Integer.SIZE; //32
    int size;
    int collisions = 0;
    Bucket Dictionary[];
    int matrix[][];
    //constructor
    public HashTableLinear(int size){
        this.size = size;
        this.setSize(size);
        this.matrix =  new int [(int)Math.pow(2, this.size)][MAXBITS];
        this.setMatrix();
        this.Dictionary = new Bucket[(int)Math.pow(2, this.size)];
        this.constructBins(); //construct all Bins
    }

    //Bucket sub-class
    class Bucket extends HashTable{

        List<Integer> elements;     /* Holds the elemets to be hashed */
        boolean hasCollisions;      /* True: if there are more than 1 element 
                                       False: otherwise. */
        //constructor
        Bucket(int size) {
            super(size);
            this.elements = new ArrayList<Integer>();
            this.hasCollisions = false;
        }
        /* Adds the elements to the Bucket. */
        void put(int n){ 
            this.elements.add(n);
            if(this.elements.size() == 2)
                this.hasCollisions = true;
        }
        /* Returns the size of the elements collided in the same Bucket. */
        int size() { return this.elements.size(); }
        /* Checks if the Bucket is empty or not. */
        boolean isNotEmpty() { return this.elements.size() != 0; }
        /* Hashes each element inside this Bucket 
           using Method 1 of O(N^2). */
        void hashBucket() {
            this.size = this.size();
            this.setSize(size);
            this.matrix =  new int [(int)Math.pow(2, this.size)][MAXBITS];
            this.setMatrix();
            this.Dictionary = new int[(int)Math.pow(2, this.size)];
            Arrays.fill(this.Dictionary, Integer.MIN_VALUE);
            for(int i = 0; i < elements.size(); i++){
                this.hash(this.elements.get(i));
            }
        }
    }

    private void constructBins() {
        for(int i = 0; i < this.Dictionary.length; i++){
            this.Dictionary[i] = new Bucket(0);
        }
    }

    // n : number to be hashed
    public void hash(int n)
    {
        int answer[] = new int[this.size];
        String bits = Integer.toBinaryString(n);
        for(int i = 0; i < bits.length(); i++)
        {
            if(bits.charAt(i) == '0'){continue;}
            int column [] = this.getColumn(i);
            answer = this.addColumns(answer, column);
        }
        int index = 0;
        for(int i = 0; i < answer.length; i++)
        {
            if(answer[i] == 0){continue;}
            index = index + (1<<i);
        }

        if(this.Dictionary[index].isNotEmpty()) //Bucket is not empty
            this.collisions++;

        this.Dictionary[index].put(n);
    }

     // find the smallest number power of 2 that covers the desired dictionary size
     private void setSize(int size)
     {
         // O(N)
         int x = 1;
         int count = 0;
         while(x < size){
             x <<= 1; // multiply by 2
             count++;
         }
         this.size = count; 
     }

    // create random matrix
    private void setMatrix()
    {
        for(int i = 0; i < matrix.length; i++)
        {
            for(int j = 0; j < matrix[0].length; j++)
            {
                this.matrix[i][j] = (int)Math.round(Math.random());
            }
        }
    }

    private int[] getColumn(int index) {
        int[] column = new int[(int)Math.pow(2, this.size)];
        for(int i=0; i<column.length; i++){
           column[i] = this.matrix[i][index];
        }
        return column;
    }

    private int[] addColumns(int x[], int y[])
    {
        for(int i = 0; i < x.length; i++)
        {
            x[i] = (x[i] + y[i])%2;
        }
        return x;
    }

    public void hashBucketes() {
        for(int i = 0; i < this.Dictionary.length; i++){
            Bucket bucket = this.Dictionary[i];
            if(bucket.hasCollisions){ //hash the collided buckets
                bucket.hashBucket();
                System.out.println("Bucket " + (i+1) + " collisions: " + bucket.collisions);
            }
        }
    }

    public boolean lookup(int n){
        int answer[] = new int[this.size];
        String bits = Integer.toBinaryString(n);
        for(int i = 0; i < bits.length(); i++)
        {
            if(bits.charAt(i) == '0'){continue;}
            int column [] = this.getColumn(i);
            answer = this.addColumns(answer, column);
        }
        int index = 0;
        for(int i = 0; i < answer.length; i++)
        {
            if(answer[i] == 0){continue;}
            index = index + (1<<i);
        }

        if(this.Dictionary[index].isNotEmpty()) //Bucket is not empty
            this.collisions++;
        
        return true;
    }

}
