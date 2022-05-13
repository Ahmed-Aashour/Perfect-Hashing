import java.util.Arrays;
import java.util.NoSuchElementException;

public class HashTable {
    final int MAXBITS = 32;
    int size;
    int collisions = 0;
    int Dictionary[]; // the table
    int matrix[][]; // the matrix represents the hash function
    // size: size of the dictionary
    public HashTable(int size)
    {
        this.size = size;
        this.setSize(size);
        this.matrix =  new int [(int)Math.pow(2, this.size)][MAXBITS];
        this.setMatrix();
        Dictionary = new int[(int)Math.pow(2, this.size)];
        // intialize the dictionary with minimum integer value
        // assume that it won't be inserted
        Arrays.fill(this.Dictionary, Integer.MIN_VALUE);
    }
    // n : number to be hashed
    public void hash(int n)
    {
        int answer[] = new int[this.size];
        String bits = Integer.toBinaryString(n); // convert the number to bit representation
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
        if(this.Dictionary[index] == Integer.MIN_VALUE)
        {
            // no collision state
            this.Dictionary[index] = n;
        }
        else
        {
            collisions++;
            this.reHash();
            this.hash(n);
        }
    }
    // rehash in case of a collision rebuild the table and choose new hash function
    public void reHash()
    {
        // choose another hash funstion
        this.setMatrix();
        for(int i = 0; i < this.Dictionary.length; i++)
        {
            if(this.Dictionary[i] != Integer.MIN_VALUE)
            {
                int temp = this.Dictionary[i];
                this.Dictionary[i] = Integer.MIN_VALUE;
                this.hash(temp);
            }
        }
    }
    // find the smallest number power of 2 that covers the desired dictionary size
    protected void setSize(int size)
    {
        size = size*size; // O(N^2)
        int x = 1;
        int count = 0;
        while(x < size){
            x <<= 1; // multiply by 2
            count++;
        }
        this.size = count; 
    }
    // return the number at the following index if not found throws exception
    public int lookUp(int n)
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
        if(this.Dictionary[index] ==  Integer.MIN_VALUE)
        {
            throw new NoSuchElementException();
        }
        return this.Dictionary[index];
    }
    // create random matrix (hash function)
    protected void setMatrix()
    {
        for(int i = 0; i < matrix.length; i++)
        {
            for(int j = 0; j < matrix[0].length; j++)
            {
                this.matrix[i][j] = (int)Math.round(Math.random());
            }
        }
    }
    // get the column with the given index
    protected int[] getColumn(int index) {
        int[] column = new int[(int)Math.pow(2, this.size)]; 
        for(int i=0; i<column.length; i++){
           column[i] = this.matrix[i][index];
        }
        return column;
    }
    // add first and second column and put thresult in the first
    protected int[] addColumns(int x[], int y[])
    {
        for(int i = 0; i < x.length; i++)
        {
            x[i] = (x[i] + y[i])%2;
        }
        return x;
    }
}
