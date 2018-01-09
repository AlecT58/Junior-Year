import java.util.NoSuchElementException;

public class MinPQ
{
    private CarData[] pq;                    // store items at indices 1 to n
    private int n;                       // number of items on priority queue
    private boolean priceOrMiles;
    /**
     * Initializes an empty priority queue with the given initial capacity.
     *
     * @param  initCapacity the initial capacity of this priority queue
       * @param priceOrMiles
     */
    public MinPQ(int initCapacity, boolean priceOrMiles) 
    {
        pq = new CarData[initCapacity + 1];
        n = 0;
        this.priceOrMiles = priceOrMiles;
    }
    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty()
    {
        return n == 0;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int size()
    {
        return n;
    }

    /**
     * Returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public CarData min() 
    {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) 
    {
        assert capacity > n;
        CarData[] temp = new CarData[capacity];
        for (int i = 1; i <= n; i++) 
        {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    /**
     * Adds a new key to this priority queue.
     *
     * @param  x the key to add to this priority queue
     */
    public void insert(CarData x) 
    {
        // double size of array if necessary
        if (n == pq.length - 1) resize(2 * pq.length);

        // add x, and percolate it up to maintain heap invariant
        pq[++n] = x;
        swim(n);
        assert isMinHeap();
    }

    /**
     * Removes and returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public CarData delMin() 
    {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        exch(1, n);
        CarData min = pq[n--];
        sink(1);
        pq[n+1] = null;         // avoid loitering and help with garbage collection
        if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length  / 2);
        assert isMinHeap();
        return min;
    }
    
    public void update(CarData newCar)
    {
          for (int i = 1; i < pq.length; i++) 
          {
                if(pq[i].getVin().equals(newCar.getVin()))
                {
                      pq[i].addColor(newCar.getColor());
                      pq[i].addPrice(newCar.getPrice());
                      pq[i].addMileage(newCar.getMileage());
                      break;
                }
          }
          assert isMinHeap();
    }

//    public CarData delete(String vin)
//    {
//          if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
//          CarData tempCar = null;
//          
//          int numElements = 0;
//          for(int i = 0; i < pq.length; i++)
//          {
//                  tempCar = (CarData) pq[i];
//                
//                if(tempCar.getVin().equals(vin))
//                {
//                      pq[i] = null;
//                }
//                else if(pq[i] != null)
//                {
//                      numElements++;
//                }
//          }
//          
//          CarData[] temp = new CarData[numElements];
//    }

   /***************************************************************************
    * Helper functions to restore the heap invariant.
    ***************************************************************************/

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

   /***************************************************************************
    * Helper functions for compares and swaps.
    ***************************************************************************/
    private boolean greater(int i, int j) 
    {
           if(this.priceOrMiles)
          {
                CarData car1 = (CarData) pq[i];
                CarData car2 = (CarData) pq[j];
                
                return car1.getPrice() == car2.getPrice() || car1.getPrice() > car2.getPrice();
          }
          else if(!this.priceOrMiles)
          {
                CarData car1 = (CarData) pq[i];
                CarData car2 = (CarData) pq[j];
                
                return car1.getMileage() == car2.getMileage() || car1.getMileage() > car2.getMileage();
          }
           
           return false;
    }

    private void exch(int i, int j) 
    {
        CarData swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    // is pq[1..N] a min heap?
    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    // is subtree of pq[1..n] rooted at k a min heap?
    private boolean isMinHeap(int k) {
        if (k > n) return true;
        int left = 2*k;
        int right = 2*k + 1;
        if (left  <= n && greater(k, left))  return false;
        if (right <= n && greater(k, right)) return false;
        return isMinHeap(left) && isMinHeap(right);
    }
}