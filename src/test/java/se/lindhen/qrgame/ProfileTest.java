package se.lindhen.qrgame;

import org.junit.Ignore;
import org.junit.Test;
import se.lindhen.qrgame.program.GameLoop;
import se.lindhen.qrgame.program.Program;

import java.io.IOException;

public class ProfileTest {

    boolean[] sieveOfEratosthenes(int n)
    {
        // Create a boolean array "prime[0..n]" and initialize
        // all entries it as true. A value in prime[i] will
        // finally be false if i is Not a prime, else true.
        boolean[] prime = new boolean[n+1];
        for(int i=0;i<=n;i++)
            prime[i] = true;

        for(int p = 2; p*p <=n; p++)
        {
            // If prime[p] is not changed, then it is a prime
            if(prime[p])
            {
                // Update all multiples of p
                for(int i = p*p; i <= n; i += p)
                    prime[i] = false;
            }
        }
        return null;
    }

    @Test
    @Ignore
    public void compareSieveImplementations() throws IOException {
        int max = 100000000;

        long before = System.currentTimeMillis();
        boolean[] primes = sieveOfEratosthenes(max);
        long took = System.currentTimeMillis() - before;
        System.out.println("Java version took " + took + "ms");

        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/sieveOfEratosthenes.qg"));
        GameLoop iteration = program.initializeAndPrepareRun();
        program.setVariable(0, (double) max);
        before = System.currentTimeMillis();
        iteration.run(100);
        took = System.currentTimeMillis() - before;
        System.out.println("QG took " + took + "ms");
        /*List<Value> backingList = program.getVariable(1).getList().getBackingList();
        for (int i = 0; i < primes.length; i++) {
            if (primes[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
        for (int i = 0; i < backingList.size(); i++) {
            if (backingList.get(i).getBool()) {
                System.out.print(i + " ");
            }
        }*/
    }

}
