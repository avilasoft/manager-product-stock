package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComputedPriceListItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComputedPriceListItem.class);
        ComputedPriceListItem computedPriceListItem1 = new ComputedPriceListItem();
        computedPriceListItem1.setId(1L);
        ComputedPriceListItem computedPriceListItem2 = new ComputedPriceListItem();
        computedPriceListItem2.setId(computedPriceListItem1.getId());
        assertThat(computedPriceListItem1).isEqualTo(computedPriceListItem2);
        computedPriceListItem2.setId(2L);
        assertThat(computedPriceListItem1).isNotEqualTo(computedPriceListItem2);
        computedPriceListItem1.setId(null);
        assertThat(computedPriceListItem1).isNotEqualTo(computedPriceListItem2);
    }
}
