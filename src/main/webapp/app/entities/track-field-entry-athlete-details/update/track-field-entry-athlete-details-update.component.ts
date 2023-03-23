import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import {
  TrackFieldEntryAthleteDetailsFormService,
  TrackFieldEntryAthleteDetailsFormGroup,
} from './track-field-entry-athlete-details-form.service';
import { ITrackFieldEntryAthleteDetails } from '../track-field-entry-athlete-details.model';
import { TrackFieldEntryAthleteDetailsService } from '../service/track-field-entry-athlete-details.service';
import { IAthlete } from 'app/entities/athlete/athlete.model';
import { AthleteService } from 'app/entities/athlete/service/athlete.service';

@Component({
  selector: 'jhi-track-field-entry-athlete-details-update',
  templateUrl: './track-field-entry-athlete-details-update.component.html',
})
export class TrackFieldEntryAthleteDetailsUpdateComponent implements OnInit {
  isSaving = false;
  trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails | null = null;

  athletesSharedCollection: IAthlete[] = [];

  editForm: TrackFieldEntryAthleteDetailsFormGroup =
    this.trackFieldEntryAthleteDetailsFormService.createTrackFieldEntryAthleteDetailsFormGroup();

  constructor(
    protected trackFieldEntryAthleteDetailsService: TrackFieldEntryAthleteDetailsService,
    protected trackFieldEntryAthleteDetailsFormService: TrackFieldEntryAthleteDetailsFormService,
    protected athleteService: AthleteService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAthlete = (o1: IAthlete | null, o2: IAthlete | null): boolean => this.athleteService.compareAthlete(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trackFieldEntryAthleteDetails }) => {
      this.trackFieldEntryAthleteDetails = trackFieldEntryAthleteDetails;
      if (trackFieldEntryAthleteDetails) {
        this.updateForm(trackFieldEntryAthleteDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trackFieldEntryAthleteDetails = this.trackFieldEntryAthleteDetailsFormService.getTrackFieldEntryAthleteDetails(this.editForm);
    if (trackFieldEntryAthleteDetails.id !== null) {
      this.subscribeToSaveResponse(this.trackFieldEntryAthleteDetailsService.update(trackFieldEntryAthleteDetails));
    } else {
      this.subscribeToSaveResponse(this.trackFieldEntryAthleteDetailsService.create(trackFieldEntryAthleteDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrackFieldEntryAthleteDetails>>): void {
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

  protected updateForm(trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails): void {
    this.trackFieldEntryAthleteDetails = trackFieldEntryAthleteDetails;
    this.trackFieldEntryAthleteDetailsFormService.resetForm(this.editForm, trackFieldEntryAthleteDetails);

    this.athletesSharedCollection = this.athleteService.addAthleteToCollectionIfMissing<IAthlete>(
      this.athletesSharedCollection,
      trackFieldEntryAthleteDetails.athlete
    );
  }

  protected loadRelationshipsOptions(): void {
    this.athleteService
      .query()
      .pipe(map((res: HttpResponse<IAthlete[]>) => res.body ?? []))
      .pipe(
        map((athletes: IAthlete[]) =>
          this.athleteService.addAthleteToCollectionIfMissing<IAthlete>(athletes, this.trackFieldEntryAthleteDetails?.athlete)
        )
      )
      .subscribe((athletes: IAthlete[]) => (this.athletesSharedCollection = athletes));
  }
}
