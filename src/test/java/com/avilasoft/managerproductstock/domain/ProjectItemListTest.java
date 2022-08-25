package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectItemListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectItemList.class);
        ProjectItemList projectItemList1 = new ProjectItemList();
        projectItemList1.setId(1L);
        ProjectItemList projectItemList2 = new ProjectItemList();
        projectItemList2.setId(projectItemList1.getId());
        assertThat(projectItemList1).isEqualTo(projectItemList2);
        projectItemList2.setId(2L);
        assertThat(projectItemList1).isNotEqualTo(projectItemList2);
        projectItemList1.setId(null);
        assertThat(projectItemList1).isNotEqualTo(projectItemList2);
    }
}
