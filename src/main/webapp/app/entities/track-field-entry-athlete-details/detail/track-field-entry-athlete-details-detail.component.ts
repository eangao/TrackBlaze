import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrackFieldEntryAthleteDetails } from '../track-field-entry-athlete-details.model';

@Component({
  selector: 'jhi-track-field-entry-athlete-details-detail',
  templateUrl: './track-field-entry-athlete-details-detail.component.html',
})
export class TrackFieldEntryAthleteDetailsDetailComponent implements OnInit {
  trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trackFieldEntryAthleteDetails }) => {
      this.trackFieldEntryAthleteDetails = trackFieldEntryAthleteDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
