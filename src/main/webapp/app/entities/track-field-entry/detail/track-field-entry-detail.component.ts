import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrackFieldEntry } from '../track-field-entry.model';

@Component({
  selector: 'jhi-track-field-entry-detail',
  templateUrl: './track-field-entry-detail.component.html',
})
export class TrackFieldEntryDetailComponent implements OnInit {
  trackFieldEntry: ITrackFieldEntry | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trackFieldEntry }) => {
      this.trackFieldEntry = trackFieldEntry;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
