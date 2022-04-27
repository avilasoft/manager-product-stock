package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProviderProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProviderProduct.class);
        ProviderProduct providerProduct1 = new ProviderProduct();
        providerProduct1.setId(1L);
        ProviderProduct providerProduct2 = new ProviderProduct();
        providerProduct2.setId(providerProduct1.getId());
        assertThat(providerProduct1).isEqualTo(providerProduct2);
        providerProduct2.setId(2L);
        assertThat(providerProduct1).isNotEqualTo(providerProduct2);
        providerProduct1.setId(null);
        assertThat(providerProduct1).isNotEqualTo(providerProduct2);
    }
}
