package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitPriceListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitPriceList.class);
        UnitPriceList unitPriceList1 = new UnitPriceList();
        unitPriceList1.setId(1L);
        UnitPriceList unitPriceList2 = new UnitPriceList();
        unitPriceList2.setId(unitPriceList1.getId());
        assertThat(unitPriceList1).isEqualTo(unitPriceList2);
        unitPriceList2.setId(2L);
        assertThat(unitPriceList1).isNotEqualTo(unitPriceList2);
        unitPriceList1.setId(null);
        assertThat(unitPriceList1).isNotEqualTo(unitPriceList2);
    }
}
