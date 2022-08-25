package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProviderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProviderItem.class);
        ProviderItem providerItem1 = new ProviderItem();
        providerItem1.setId(1L);
        ProviderItem providerItem2 = new ProviderItem();
        providerItem2.setId(providerItem1.getId());
        assertThat(providerItem1).isEqualTo(providerItem2);
        providerItem2.setId(2L);
        assertThat(providerItem1).isNotEqualTo(providerItem2);
        providerItem1.setId(null);
        assertThat(providerItem1).isNotEqualTo(providerItem2);
    }
}
