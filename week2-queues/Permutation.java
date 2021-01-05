import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
   public static void main(String[] args) {
      if (args.length == 1) {
         RandomizedQueue<String> rq = new RandomizedQueue<>();
         int k = Integer.parseInt(args[0]);
         boolean full = false;

         while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
            if (k > 0) {
               k--;
            } else {
               rq.dequeue();
            }
         }

         for (String s : rq) {
            StdOut.println(s);
         }

      } if (args.length == 2) {
         RandomizedQueue<String> rq = new RandomizedQueue<>();
         int k = Integer.parseInt(args[0]);
         // In in = new In(args[0]);
         // StdIn in = new StdIn.readString();

         while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            // StdOut.println(s);
            rq.enqueue(s);   
         }

         int count = k > rq.size() ? rq.size() : k;

         if (k > rq.size()/4) {
            for (String s : rq) {
               StdOut.println(s);
               count--;
               if (count <= 0) {
                  break;
               }
            }
         } else {
            while (count > 0) {
               String s = rq.dequeue();
               StdOut.println(s);
               count--;
            }
         }
      }
   }
}