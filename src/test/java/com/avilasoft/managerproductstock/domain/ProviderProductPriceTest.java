package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProviderProductPriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProviderProductPrice.class);
        ProviderProductPrice providerProductPrice1 = new ProviderProductPrice();
        providerProductPrice1.setId(1L);
        ProviderProductPrice providerProductPrice2 = new ProviderProductPrice();
        providerProductPrice2.setId(providerProductPrice1.getId());
        assertThat(providerProductPrice1).isEqualTo(providerProductPrice2);
        providerProductPrice2.setId(2L);
        assertThat(providerProductPrice1).isNotEqualTo(providerProductPrice2);
        providerProductPrice1.setId(null);
        assertThat(providerProductPrice1).isNotEqualTo(providerProductPrice2);
    }
}
