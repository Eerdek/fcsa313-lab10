package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * gcd() — all-DU-pairs. Line numbers = DataFlowTargets.java file lines:
 * 13 def x,y | 15 while (y != 0) p-use y | 16 tmp = x % y | 17 x = y | 18 y = tmp | 20 return x.
 * Pair notation: var(def -> use), p-use edges as 15T (to 16) / 15F (to 20).
 */
class GcdTest {

    // G1: loop executes 0 times. The ONLY way the parameter x (def 13) reaches return (20).
    @Test
    @DisplayName("G1 gcd(7,0)=7 | x(13->20), y(13->15F)")
    void gcd_7_0_zeroIterations_returns7() {
        assertEquals(7, DataFlowTargets.gcd(7, 0));
    }

    // G2: loop executes exactly 1 time: 0%5=0 -> x=5, y=0.
    @Test
    @DisplayName("G2 gcd(0,5)=5 | x(13->16), x(17->20), y(13->15T), y(13->16), y(13->17), y(18->15F), tmp(16->18)")
    void gcd_0_5_oneIteration_returns5() {
        assertEquals(5, DataFlowTargets.gcd(0, 5));
    }

    // G3: loop executes 2 times: (12,8) -> (8,4) -> (4,0).
    // Pairs that need the back edge 18 -> 15 -> 16: x(17->16), y(18->15T), y(18->16), y(18->17).
    @Test
    @DisplayName("G3 gcd(12,8)=4 | x(17->16), y(18->15T), y(18->16), y(18->17) + all G2 pairs")
    void gcd_12_8_twoIterations_returns4() {
        assertEquals(4, DataFlowTargets.gcd(12, 8));
    }
}
