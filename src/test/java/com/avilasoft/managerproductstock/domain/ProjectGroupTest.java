package com.avilasoft.managerproductstock.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avilasoft.managerproductstock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectGroup.class);
        ProjectGroup projectGroup1 = new ProjectGroup();
        projectGroup1.setId(1L);
        ProjectGroup projectGroup2 = new ProjectGroup();
        projectGroup2.setId(projectGroup1.getId());
        assertThat(projectGroup1).isEqualTo(projectGroup2);
        projectGroup2.setId(2L);
        assertThat(projectGroup1).isNotEqualTo(projectGroup2);
        projectGroup1.setId(null);
        assertThat(projectGroup1).isNotEqualTo(projectGroup2);
    }
}
