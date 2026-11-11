package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * bonus() — data-flow tests. Line numbers = DataFlowTargets.java file lines (31-56).
 * Stage 1: ALL-DEFS (minimum 2 tests). Every def reaches at least one use.
 */
class BonusTest {

    // ---------- Stage 1: ALL-DEFS ----------

    // AD1: covers every def except score@51.
    // present@35->40, present@40->40/50, streak@36->41, streak@41->42, streak@46->41 (absent then present),
    // best@37->42, best@43->53, a@38->39, score@49->54 (50F then 53T), score@54->56.
    @Test
    @DisplayName("AD1 bonus([1,0,1,1,1,1,1],7)=3 | defs 35,36,37,38,40,41,43,46,49,54")
    void bonus_allDefs_absentThenStreak5_presentBelowThreshold_returns3() {
        assertEquals(3, DataFlowTargets.bonus(new int[] {1, 0, 1, 1, 1, 1, 1}, 7));
    }

    // AD2: score@51 -> 56 (50T then 53F). score@49 and score@51 can never both reach a use
    // in one run (51 kills 49 before any use), so all-defs needs at least 2 tests.
    @Test
    @DisplayName("AD2 bonus([1],1)=5 | def 51 (score@51->56)")
    void bonus_allDefs_singlePresentMeetsThreshold_returns5() {
        assertEquals(5, DataFlowTargets.bonus(new int[] {1}, 1));
    }
}
