package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BatchProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BatchProduct.class);
        BatchProduct batchProduct1 = new BatchProduct();
        batchProduct1.setId(1L);
        BatchProduct batchProduct2 = new BatchProduct();
        batchProduct2.setId(batchProduct1.getId());
        assertThat(batchProduct1).isEqualTo(batchProduct2);
        batchProduct2.setId(2L);
        assertThat(batchProduct1).isNotEqualTo(batchProduct2);
        batchProduct1.setId(null);
        assertThat(batchProduct1).isNotEqualTo(batchProduct2);
    }
}
