import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
   public static void main(String[] args) {
      if (args.length == 1) {
         RandomizedQueue<String> rq = new RandomizedQueue<>();
         int k = Integer.parseInt(args[0]);

         if (k == 0) return;
         // init first k elements
         for (int i = 0; i < k && !StdIn.isEmpty(); i++) {
            String s = StdIn.readString();
            rq.enqueue(s);
         }

         // resevoir sampling
         /**
         The n-th item is to be added to the collection of probability: k / n
         
         Method 1:
         StdRandom.uniform(a, b) returns a value in the range [a, b). Thus in 
         resevoir sampling we need to call with such arguments
         StdRandom.uniform(0, n+1) to get a value rand in [0, n], 
         and compare rand with k. 

         Method 2:
         Generate a value rand in the range [0, 1), and compare rand with
         (double) k / n (beware that k / n is an int). 
         This is technically the same as Method 1, especially when we look at 
         the implementations of StdRandom.uniform(a, b) or StdRandom.uniform(n).
         */
         int n = k;
         while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            int rand = StdRandom.uniform(++n);
            // int rand = StdRandom.uniform(0, ++n);
            if (rand < k) {
            // if (StdRandom.uniform() < (double) k / n) {
               rq.dequeue();
               rq.enqueue(s);
            }
         }

         for (String s : rq) {
            StdOut.println(s);
         }
      }
   }
}
