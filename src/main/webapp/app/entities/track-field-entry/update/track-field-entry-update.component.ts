import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TrackFieldEntryFormService, TrackFieldEntryFormGroup } from './track-field-entry-form.service';
import { ITrackFieldEntry } from '../track-field-entry.model';
import { TrackFieldEntryService } from '../service/track-field-entry.service';
import { ITrackFieldEntryAthleteDetails } from 'app/entities/track-field-entry-athlete-details/track-field-entry-athlete-details.model';
import { TrackFieldEntryAthleteDetailsService } from 'app/entities/track-field-entry-athlete-details/service/track-field-entry-athlete-details.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { Category } from 'app/entities/enumerations/category.model';

@Component({
  selector: 'jhi-track-field-entry-update',
  templateUrl: './track-field-entry-update.component.html',
})
export class TrackFieldEntryUpdateComponent implements OnInit {
  isSaving = false;
  trackFieldEntry: ITrackFieldEntry | null = null;
  categoryValues = Object.keys(Category);

  trackFieldEntryAthleteDetailsSharedCollection: ITrackFieldEntryAthleteDetails[] = [];
  eventsSharedCollection: IEvent[] = [];

  editForm: TrackFieldEntryFormGroup = this.trackFieldEntryFormService.createTrackFieldEntryFormGroup();

  constructor(
    protected trackFieldEntryService: TrackFieldEntryService,
    protected trackFieldEntryFormService: TrackFieldEntryFormService,
    protected trackFieldEntryAthleteDetailsService: TrackFieldEntryAthleteDetailsService,
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTrackFieldEntryAthleteDetails = (o1: ITrackFieldEntryAthleteDetails | null, o2: ITrackFieldEntryAthleteDetails | null): boolean =>
    this.trackFieldEntryAthleteDetailsService.compareTrackFieldEntryAthleteDetails(o1, o2);

  compareEvent = (o1: IEvent | null, o2: IEvent | null): boolean => this.eventService.compareEvent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trackFieldEntry }) => {
      this.trackFieldEntry = trackFieldEntry;
      if (trackFieldEntry) {
        this.updateForm(trackFieldEntry);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trackFieldEntry = this.trackFieldEntryFormService.getTrackFieldEntry(this.editForm);
    if (trackFieldEntry.id !== null) {
      this.subscribeToSaveResponse(this.trackFieldEntryService.update(trackFieldEntry));
    } else {
      this.subscribeToSaveResponse(this.trackFieldEntryService.create(trackFieldEntry));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrackFieldEntry>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(trackFieldEntry: ITrackFieldEntry): void {
    this.trackFieldEntry = trackFieldEntry;
    this.trackFieldEntryFormService.resetForm(this.editForm, trackFieldEntry);

    this.trackFieldEntryAthleteDetailsSharedCollection =
      this.trackFieldEntryAthleteDetailsService.addTrackFieldEntryAthleteDetailsToCollectionIfMissing<ITrackFieldEntryAthleteDetails>(
        this.trackFieldEntryAthleteDetailsSharedCollection,
        ...(trackFieldEntry.details ?? [])
      );
    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing<IEvent>(
      this.eventsSharedCollection,
      trackFieldEntry.event
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trackFieldEntryAthleteDetailsService
      .query()
      .pipe(map((res: HttpResponse<ITrackFieldEntryAthleteDetails[]>) => res.body ?? []))
      .pipe(
        map((trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails[]) =>
          this.trackFieldEntryAthleteDetailsService.addTrackFieldEntryAthleteDetailsToCollectionIfMissing<ITrackFieldEntryAthleteDetails>(
            trackFieldEntryAthleteDetails,
            ...(this.trackFieldEntry?.details ?? [])
          )
        )
      )
      .subscribe(
        (trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails[]) =>
          (this.trackFieldEntryAthleteDetailsSharedCollection = trackFieldEntryAthleteDetails)
      );

    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing<IEvent>(events, this.trackFieldEntry?.event)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));
  }
}
