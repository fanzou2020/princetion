import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
public class Test {

    public static void main(String[] args) {
        // Differences between String "==" and "equals()"
        String a = "abc";
        String b = new String("abc");
        String c = "abc";
        StdOut.println("Compare using '==' ");
        StdOut.println(a == b);
        StdOut.println(a == c);
        StdOut.println("Compare using 'equals()' ");
        StdOut.println(a.equals(b));
        StdOut.println(a.equals(c));


        Integer integer = 1;
        StdOut.println(integer.getClass());
        int[] aa;
        Object bb = new Object();
        StdOut.println(bb.hashCode());
        StdOut.println(bb);
        Double dou ;
        Boolean bool;

        byte b1 = 1;
        StdOut.println(b1);

        StdOut.println(a.charAt(1)+1);

        int i = 1 << 8;
        StdOut.println(i);

        Queue<String> q = new Queue<>();
        StdOut.println(q == null);


    }
}
