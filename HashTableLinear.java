import java.util.*;

public class HashTableLinear {
    final int MAXBITS = Integer.SIZE; //32 bits
    int size;
    int sampleSize;
    int elementsInserted = 0;
    int actualSize = 0;
    int collisions_lvl_1 = 0;
    int collisions_lvl_2 = 0;
    int rebuilds = 0;
    Bucket Dictionary[];
    int matrix[][];
    //constructor
    public HashTableLinear(int size){
        this.sampleSize = this.size = size;
        this.setSize(size);
        this.matrix =  new int [(int)Math.pow(2, this.size)][MAXBITS];
        this.setMatrix();
        this.Dictionary = new Bucket[(int)Math.pow(2, this.size)];
        this.constructBuckets(); //construct all Buckets
    }

    public void setSampleSize(int sampleSize){
        this.sampleSize = sampleSize;
    }

    //Bucket sub-class
    class Bucket extends HashTable{

        List<Integer> elements;     /* Holds the elemets to be hashed */
        /* boolean hasCollisions; */      /* True: if there are more than 1 element 
                                       False: otherwise. */
        int id;
        //constructor
        Bucket(int size, int id) {
            super(size);
            this.id = id;
            this.elements = new ArrayList<Integer>();
            // this.hasCollisions = false;
            this.Dictionary = new int[0];
        }

        /* Adds the elements to the Bucket & performs hashing to the whole bucket again. */
        void put(int n){ 
            this.elements.add(n);
            // this.hashBucket();
            // if(this.elements.size() == 2)
            //     this.hasCollisions = true;
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
    public void hash(int n){
        // Getting the key
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
        if(this.Dictionary[index].isNotEmpty()) // Collision Condition
            this.collisions_lvl_1++;

        this.Dictionary[index].put(n);
        this.elementsInserted++;
        // When all elements were inserted, check if the hash function is acceptable or not.
        if(this.elementsInserted == this.sampleSize)
            checkBuckets();
    }

    /* Checks if the hash function chosen is acceptable or not.
       if acceptable:   then Performs the 2nd level Hashing,
       if unacceptable: then chooses another hash function & rebuilds the 1st level. */
    public void checkBuckets(){
        // Getting the summation of Sigma(ni^2)
        int sum_squared = 0;
        for(Bucket bucket: this.Dictionary){
            sum_squared += bucket.size() * bucket.size();
        }
        // Testing the condition.
        if(sum_squared < 4*this.Dictionary.length){ // Cool :)
            this.hashBuckets();
            //System.out.println("Hash 2nd level !! as Eni^2 " + sum_squared + " < 4*n " + 4*this.Dictionary.length);
        }
        else{ // Not cool :(
            this.rebuilds++;
            this.rehash();
            //System.out.println("Rebuilding !!");
        }
    }

    /* Rehashes the Hash Table using a new universal hash function */
    private void rehash(){
        // Reset collisions & elements inserted counters
        this.collisions_lvl_1 = 0;
        this.elementsInserted = 0;
        // Store the hashed elements to be rehashed
        int elements[] = new int[sampleSize];
        int i = -1;
        for(Bucket bucket: this.Dictionary)
            if(bucket.isNotEmpty()){
                for(Integer element: bucket.elements){
                    elements[++i] = element;
                }
            }
        // Get a new hash faunction & reset the Dictionary and all Buckets
        this.matrix =  new int [(int)Math.pow(2, this.size)][MAXBITS];
        this.setMatrix();
        this.Dictionary = new Bucket[(int)Math.pow(2, this.size)];
        this.constructBuckets();
        // Rehashing
        for(int element: elements)
            this.hash(element);
    }

    /* looks up for a specific number
       if its found returns it & if not found throws an Exception. */
    public int lookUp(int n){
        // lookUp in the 1st level
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
        if(this.Dictionary[index].isNotEmpty()){
            // lookUp in the 2nd level
            return this.Dictionary[index].lookUp(n);
        }
        throw new NoSuchElementException();
    }
    
    /* find the smallest number power of 2 that covers the desired dictionary size */
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

    /* Creates random matrix */
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

    /* Hashes each bucket that filled with elemets
       & computes the sum of the Buckets collisions (2nd level collisions)
       & computes the actual size occupied by the hash table.  */
    private void hashBuckets(){
        for(Bucket bucket: this.Dictionary){
            if(bucket.isNotEmpty()){ // hash only the filled ones
                bucket.hashBucket();
            }
        }
        compute_collisions_and_actualSize();
    }

    private void compute_collisions_and_actualSize(){
        for(Bucket bucket: this.Dictionary)
            if(bucket.isNotEmpty()){ // hash only the filled ones
                this.collisions_lvl_2 += bucket.collisions;
                this.actualSize += bucket.Dictionary.length;
            }
    }
}
