/*
 * Hashtable implementation modified from our textbook, used for the symbol table in Project 1
 */
//package dlb_test;

public class Symbol_Table
{
      private static final int INIT_CAPACITY = 6;
      private int n;        // number of key-value pairs
      private int m;        // number of chains
      private Node[] st;    // array of linked-list symbol tables

       // a helper linked list data type
      private static class Node
      {
            private final String key;
            private double val;
            private Node next;

            public Node(String key, double val, Node next) 
            {
                  this.key  = key;
                  this.val  = val;
                  this.next = next;
            }
      }

    /**
     * Initializes an empty symbol table.
     */
      public Symbol_Table()
      {
            this(INIT_CAPACITY);
      } 

    /**
     * Initializes an empty symbol table with {@code m} chains.
     * @param m the initial number of chains
     */
      public Symbol_Table(int m) 
      {
            this.m = m;
            st = new Node[m];
      } 

    // resize the hash table to have the given number of chains,
    // rehashing all of the keys
    private void resize(int chains) 
    {
        Symbol_Table temp = new Symbol_Table(chains);
        for (int i = 0; i < m; i++) 
        {
            for (Node x = st[i]; x != null; x = x.next) 
            {
                temp.put(x.key,  x.val);
            }
        }

        this.m  = temp.m;
        this.n  = temp.n;
        this.st = temp.st;
    }

    // hash value between 0 and m-1
      private int hash(String key) 
      {
          return (key.hashCode() & 0x7fffffff) % m;
      } 

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() 
    {
        return n;
    } 

    /**
     * Returns true if this symbol table is empty.
     *
     * @return {@code true} if this symbol table is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() 
    {
        return size() == 0;
    }

    /**
     * Returns true if this symbol table contains the specified key.
     *
     * @param  key the key
     * @return {@code true} if this symbol table contains {@code key};
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
      public boolean contains(String key) 
      {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            return get(key) > 0;
    } 

    /**
     * Returns the value associated with the specified key in this symbol table.
     *
     * @param  key the key
     * @return the value associated with {@code key} in the symbol table;
     *         {@code null} if no such value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
      public double get(String key) 
      {
            if (key == null) throw new IllegalArgumentException("argument to get() is null");
            int i = hash(key);
            for (Node x = st[i]; x != null; x = x.next) 
            {
                if (key.equals(x.key)) return  x.val;
            }
            return 0;
      }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Removes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
      public void put(String key, double val)
      {
            if (key == null) throw new IllegalArgumentException("first argument to put() is null");
            if (val == 0) 
            {
                  remove(key);
                  return;
            }

        // double table size if average length of list >= 10
            if (n >= 10*m) resize(2*m);


            int i = hash(key);
            for (Node x = st[i]; x != null; x = x.next) 
            {
                  if (key.equals(x.key)) 
                  {
                        x.val = val;
                        return;
                  }
            }
            n++;
            st[i] = new Node(key, val, st[i]);
    }

    /**
     * Removes the specified key and its associated value from this symbol table     
     * (if the key is in this symbol table).    
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
      public void remove(String key)
      {
            if (key == null) throw new IllegalArgumentException("argument to remove() is null");

            int i = hash(key);
            st[i] = remove(st[i], key);

            // halve table size if average length of list <= 2
            if (m > INIT_CAPACITY && n <= 2*m) resize(m/2);
    }

    // remove key in linked list beginning at Node x
    // warning: function call stack too large if table is large
      private Node remove(Node x, String key) 
      {
            if (x == null) return null;
            if (key.equals(x.key)) 
            {
                  n--;
                  return x.next;
            }
            x.next = remove(x.next, key);
            return x;
      }
}