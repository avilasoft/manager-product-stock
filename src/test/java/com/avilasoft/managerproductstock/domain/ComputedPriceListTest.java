package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComputedPriceListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComputedPriceList.class);
        ComputedPriceList computedPriceList1 = new ComputedPriceList();
        computedPriceList1.setId(1L);
        ComputedPriceList computedPriceList2 = new ComputedPriceList();
        computedPriceList2.setId(computedPriceList1.getId());
        assertThat(computedPriceList1).isEqualTo(computedPriceList2);
        computedPriceList2.setId(2L);
        assertThat(computedPriceList1).isNotEqualTo(computedPriceList2);
        computedPriceList1.setId(null);
        assertThat(computedPriceList1).isNotEqualTo(computedPriceList2);
    }
}
