import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITrackFieldEntryAthleteDetails, NewTrackFieldEntryAthleteDetails } from '../track-field-entry-athlete-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrackFieldEntryAthleteDetails for edit and NewTrackFieldEntryAthleteDetailsFormGroupInput for create.
 */
type TrackFieldEntryAthleteDetailsFormGroupInput =
  | ITrackFieldEntryAthleteDetails
  | PartialWithRequiredKeyOf<NewTrackFieldEntryAthleteDetails>;

type TrackFieldEntryAthleteDetailsFormDefaults = Pick<NewTrackFieldEntryAthleteDetails, 'id' | 'trackFieldEntries'>;

type TrackFieldEntryAthleteDetailsFormGroupContent = {
  id: FormControl<ITrackFieldEntryAthleteDetails['id'] | NewTrackFieldEntryAthleteDetails['id']>;
  rank: FormControl<ITrackFieldEntryAthleteDetails['rank']>;
  timeHeightDistance: FormControl<ITrackFieldEntryAthleteDetails['timeHeightDistance']>;
  remarks: FormControl<ITrackFieldEntryAthleteDetails['remarks']>;
  athlete: FormControl<ITrackFieldEntryAthleteDetails['athlete']>;
  trackFieldEntries: FormControl<ITrackFieldEntryAthleteDetails['trackFieldEntries']>;
};

export type TrackFieldEntryAthleteDetailsFormGroup = FormGroup<TrackFieldEntryAthleteDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrackFieldEntryAthleteDetailsFormService {
  createTrackFieldEntryAthleteDetailsFormGroup(
    trackFieldEntryAthleteDetails: TrackFieldEntryAthleteDetailsFormGroupInput = { id: null }
  ): TrackFieldEntryAthleteDetailsFormGroup {
    const trackFieldEntryAthleteDetailsRawValue = {
      ...this.getFormDefaults(),
      ...trackFieldEntryAthleteDetails,
    };
    return new FormGroup<TrackFieldEntryAthleteDetailsFormGroupContent>({
      id: new FormControl(
        { value: trackFieldEntryAthleteDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      rank: new FormControl(trackFieldEntryAthleteDetailsRawValue.rank),
      timeHeightDistance: new FormControl(trackFieldEntryAthleteDetailsRawValue.timeHeightDistance),
      remarks: new FormControl(trackFieldEntryAthleteDetailsRawValue.remarks),
      athlete: new FormControl(trackFieldEntryAthleteDetailsRawValue.athlete),
      trackFieldEntries: new FormControl(trackFieldEntryAthleteDetailsRawValue.trackFieldEntries ?? []),
    });
  }

  getTrackFieldEntryAthleteDetails(
    form: TrackFieldEntryAthleteDetailsFormGroup
  ): ITrackFieldEntryAthleteDetails | NewTrackFieldEntryAthleteDetails {
    return form.getRawValue() as ITrackFieldEntryAthleteDetails | NewTrackFieldEntryAthleteDetails;
  }

  resetForm(
    form: TrackFieldEntryAthleteDetailsFormGroup,
    trackFieldEntryAthleteDetails: TrackFieldEntryAthleteDetailsFormGroupInput
  ): void {
    const trackFieldEntryAthleteDetailsRawValue = { ...this.getFormDefaults(), ...trackFieldEntryAthleteDetails };
    form.reset(
      {
        ...trackFieldEntryAthleteDetailsRawValue,
        id: { value: trackFieldEntryAthleteDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TrackFieldEntryAthleteDetailsFormDefaults {
    return {
      id: null,
      trackFieldEntries: [],
    };
  }
}
