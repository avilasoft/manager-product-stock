package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitEquivalenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitEquivalence.class);
        UnitEquivalence unitEquivalence1 = new UnitEquivalence();
        unitEquivalence1.setId(1L);
        UnitEquivalence unitEquivalence2 = new UnitEquivalence();
        unitEquivalence2.setId(unitEquivalence1.getId());
        assertThat(unitEquivalence1).isEqualTo(unitEquivalence2);
        unitEquivalence2.setId(2L);
        assertThat(unitEquivalence1).isNotEqualTo(unitEquivalence2);
        unitEquivalence1.setId(null);
        assertThat(unitEquivalence1).isNotEqualTo(unitEquivalence2);
    }
}
