package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectItemListItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectItemListItem.class);
        ProjectItemListItem projectItemListItem1 = new ProjectItemListItem();
        projectItemListItem1.setId(1L);
        ProjectItemListItem projectItemListItem2 = new ProjectItemListItem();
        projectItemListItem2.setId(projectItemListItem1.getId());
        assertThat(projectItemListItem1).isEqualTo(projectItemListItem2);
        projectItemListItem2.setId(2L);
        assertThat(projectItemListItem1).isNotEqualTo(projectItemListItem2);
        projectItemListItem1.setId(null);
        assertThat(projectItemListItem1).isNotEqualTo(projectItemListItem2);
    }
}
