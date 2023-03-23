package com.elmarangao.trackblaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.elmarangao.trackblaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrackFieldEntryAthleteDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrackFieldEntryAthleteDetails.class);
        TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails1 = new TrackFieldEntryAthleteDetails();
        trackFieldEntryAthleteDetails1.setId(1L);
        TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails2 = new TrackFieldEntryAthleteDetails();
        trackFieldEntryAthleteDetails2.setId(trackFieldEntryAthleteDetails1.getId());
        assertThat(trackFieldEntryAthleteDetails1).isEqualTo(trackFieldEntryAthleteDetails2);
        trackFieldEntryAthleteDetails2.setId(2L);
        assertThat(trackFieldEntryAthleteDetails1).isNotEqualTo(trackFieldEntryAthleteDetails2);
        trackFieldEntryAthleteDetails1.setId(null);
        assertThat(trackFieldEntryAthleteDetails1).isNotEqualTo(trackFieldEntryAthleteDetails2);
    }
}
