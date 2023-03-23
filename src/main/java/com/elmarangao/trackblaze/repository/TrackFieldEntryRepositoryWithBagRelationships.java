package com.elmarangao.trackblaze.repository;

import com.elmarangao.trackblaze.domain.TrackFieldEntry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TrackFieldEntryRepositoryWithBagRelationships {
    Optional<TrackFieldEntry> fetchBagRelationships(Optional<TrackFieldEntry> trackFieldEntry);

    List<TrackFieldEntry> fetchBagRelationships(List<TrackFieldEntry> trackFieldEntries);

    Page<TrackFieldEntry> fetchBagRelationships(Page<TrackFieldEntry> trackFieldEntries);
}
