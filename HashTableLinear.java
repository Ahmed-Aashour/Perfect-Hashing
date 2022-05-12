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
        this.constructBuckets(); //construct all Buckets
    }

    //Bucket sub-class
    class Bucket extends HashTable{

        List<Integer> elements;     /* Holds the elemets to be hashed */
        boolean hasCollisions;      /* True: if there are more than 1 element 
                                       False: otherwise. */
        int id;
        //constructor
        Bucket(int size, int id) {
            super(size);
            this.id = id;
            this.elements = new ArrayList<Integer>();
            this.hasCollisions = false;
            this.Dictionary = new int[0];
        }
        /* Adds the elements to the Bucket & performs hashing to the whole bucket again. */
        void put(int n){ 
            this.elements.add(n);
            this.hashBucket();
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
            //System.out.println("collisions = " + this.collisions + " of Bucket " + this.id);
        }
    }

    private void constructBuckets() {
        for(int i = 0; i < this.Dictionary.length; i++){
            this.Dictionary[i] = new Bucket(0, i+1);
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

    /* Checks if the hash function chosen is acceptable or not. */
    public void checkBuckets()
    {
        int sum_squared = 0;
        for(Bucket bucket: this.Dictionary){
            sum_squared += bucket.size() * bucket.size();
        }
        /* Testing the condition. */
        if(sum_squared > 4*this.Dictionary.length){
            //System.out.println("NOt Cool :((((((((((((((((((((");
            for(Bucket bucket: this.Dictionary){ //Rehashing the entire table. <----------------------
                bucket.hashBucket();
            }
        }
        else{
            //System.out.println("So Coool :))))))))))))))))))))");
        }
    }
    
    // find the smallest number power of 2 that covers the desired dictionary size
    private void setSize(int size){
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
    private void setMatrix(){
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

    private int[] addColumns(int x[], int y[]){
        for(int i = 0; i < x.length; i++)
        {
            x[i] = (x[i] + y[i])%2;
        }
        return x;
    }
    
    void printRealSize(){
        int size = 0;
        for(Bucket bucket: this.Dictionary){
            size += bucket.Dictionary.length;
        }
        System.out.println(">>>>> Real size = " + size);
    }
}
