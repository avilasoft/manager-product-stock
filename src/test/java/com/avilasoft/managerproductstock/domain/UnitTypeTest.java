package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitType.class);
        UnitType unitType1 = new UnitType();
        unitType1.setId(1L);
        UnitType unitType2 = new UnitType();
        unitType2.setId(unitType1.getId());
        assertThat(unitType1).isEqualTo(unitType2);
        unitType2.setId(2L);
        assertThat(unitType1).isNotEqualTo(unitType2);
        unitType1.setId(null);
        assertThat(unitType1).isNotEqualTo(unitType2);
    }
}
