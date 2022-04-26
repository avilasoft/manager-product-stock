package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BatchStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BatchStatus.class);
        BatchStatus batchStatus1 = new BatchStatus();
        batchStatus1.setId(1L);
        BatchStatus batchStatus2 = new BatchStatus();
        batchStatus2.setId(batchStatus1.getId());
        assertThat(batchStatus1).isEqualTo(batchStatus2);
        batchStatus2.setId(2L);
        assertThat(batchStatus1).isNotEqualTo(batchStatus2);
        batchStatus1.setId(null);
        assertThat(batchStatus1).isNotEqualTo(batchStatus2);
    }
}
