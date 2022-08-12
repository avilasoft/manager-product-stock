package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessAssociateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessAssociate.class);
        BusinessAssociate businessAssociate1 = new BusinessAssociate();
        businessAssociate1.setId(1L);
        BusinessAssociate businessAssociate2 = new BusinessAssociate();
        businessAssociate2.setId(businessAssociate1.getId());
        assertThat(businessAssociate1).isEqualTo(businessAssociate2);
        businessAssociate2.setId(2L);
        assertThat(businessAssociate1).isNotEqualTo(businessAssociate2);
        businessAssociate1.setId(null);
        assertThat(businessAssociate1).isNotEqualTo(businessAssociate2);
    }
}
