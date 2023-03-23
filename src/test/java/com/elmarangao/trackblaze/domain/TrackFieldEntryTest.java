package com.elmarangao.trackblaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.elmarangao.trackblaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrackFieldEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrackFieldEntry.class);
        TrackFieldEntry trackFieldEntry1 = new TrackFieldEntry();
        trackFieldEntry1.setId(1L);
        TrackFieldEntry trackFieldEntry2 = new TrackFieldEntry();
        trackFieldEntry2.setId(trackFieldEntry1.getId());
        assertThat(trackFieldEntry1).isEqualTo(trackFieldEntry2);
        trackFieldEntry2.setId(2L);
        assertThat(trackFieldEntry1).isNotEqualTo(trackFieldEntry2);
        trackFieldEntry1.setId(null);
        assertThat(trackFieldEntry1).isNotEqualTo(trackFieldEntry2);
    }
}
