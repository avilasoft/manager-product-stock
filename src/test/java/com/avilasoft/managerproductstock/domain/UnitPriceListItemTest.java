package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitPriceListItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitPriceListItem.class);
        UnitPriceListItem unitPriceListItem1 = new UnitPriceListItem();
        unitPriceListItem1.setId(1L);
        UnitPriceListItem unitPriceListItem2 = new UnitPriceListItem();
        unitPriceListItem2.setId(unitPriceListItem1.getId());
        assertThat(unitPriceListItem1).isEqualTo(unitPriceListItem2);
        unitPriceListItem2.setId(2L);
        assertThat(unitPriceListItem1).isNotEqualTo(unitPriceListItem2);
        unitPriceListItem1.setId(null);
        assertThat(unitPriceListItem1).isNotEqualTo(unitPriceListItem2);
    }
}
