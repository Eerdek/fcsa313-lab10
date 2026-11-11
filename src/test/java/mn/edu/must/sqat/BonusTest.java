package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    // ---------- Stage 2: ALL-DU-PAIRS (pairs AD1 + AD2 did not reach) ----------

    @Test
    @DisplayName("DU1 bonus(null,1) throws | attendance(31->32a T)")
    void bonus_nullAttendance_throws() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(null, 1));
    }

    @Test
    @DisplayName("DU2 bonus([1],-1) throws | threshold(31->32b T)")
    void bonus_negativeThreshold_throws() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(new int[] {1}, -1));
    }

    // Empty array: loop body never runs, present@35 and best@37 reach 50/53 directly.
    @Test
    @DisplayName("DU3 bonus([],0)=5 | present(35->50T), best(37->53F), attendance(31->38F first eval), score(51->56)")
    void bonus_emptyArrayThresholdZero_returns5() {
        assertEquals(5, DataFlowTargets.bonus(new int[] {}, 0));
    }

    // All absent: present stays at its def@35 and threshold is not met.
    @Test
    @DisplayName("DU4 bonus([0,0],1)=0 | present(35->50F), best(37->53F), score(49->56)")
    void bonus_allAbsentThresholdOne_returns0() {
        assertEquals(0, DataFlowTargets.bonus(new int[] {0, 0}, 1));
    }

    // Exactly threshold present days AND a streak of exactly 5.
    @Test
    @DisplayName("DU5 bonus([1,1,1,1,1],5)=8 | score(51->54), present(40->50T), best(43->53T), score(54->56)")
    void bonus_streakFiveExactlyThreshold_returns8() {
        assertEquals(8, DataFlowTargets.bonus(new int[] {1, 1, 1, 1, 1}, 5));
    }

    // Extra idea from the lab: streak longer than 5 broken in the middle; best must keep 6, not the last streak 2.
    @Test
    @DisplayName("DU6 bonus([1,1,1,1,1,1,0,1,1],8)=8 | best(43->42F) after reset, best(43->53T) with best=6")
    void bonus_longStreakBrokenInMiddle_keepsBest_returns8() {
        assertEquals(8, DataFlowTargets.bonus(new int[] {1, 1, 1, 1, 1, 1, 0, 1, 1}, 8));
    }
}
