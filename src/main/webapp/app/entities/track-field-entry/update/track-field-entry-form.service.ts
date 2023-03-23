import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITrackFieldEntry, NewTrackFieldEntry } from '../track-field-entry.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrackFieldEntry for edit and NewTrackFieldEntryFormGroupInput for create.
 */
type TrackFieldEntryFormGroupInput = ITrackFieldEntry | PartialWithRequiredKeyOf<NewTrackFieldEntry>;

type TrackFieldEntryFormDefaults = Pick<NewTrackFieldEntry, 'id' | 'details'>;

type TrackFieldEntryFormGroupContent = {
  id: FormControl<ITrackFieldEntry['id'] | NewTrackFieldEntry['id']>;
  description: FormControl<ITrackFieldEntry['description']>;
  category: FormControl<ITrackFieldEntry['category']>;
  details: FormControl<ITrackFieldEntry['details']>;
  event: FormControl<ITrackFieldEntry['event']>;
};

export type TrackFieldEntryFormGroup = FormGroup<TrackFieldEntryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrackFieldEntryFormService {
  createTrackFieldEntryFormGroup(trackFieldEntry: TrackFieldEntryFormGroupInput = { id: null }): TrackFieldEntryFormGroup {
    const trackFieldEntryRawValue = {
      ...this.getFormDefaults(),
      ...trackFieldEntry,
    };
    return new FormGroup<TrackFieldEntryFormGroupContent>({
      id: new FormControl(
        { value: trackFieldEntryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(trackFieldEntryRawValue.description, {
        validators: [Validators.required],
      }),
      category: new FormControl(trackFieldEntryRawValue.category, {
        validators: [Validators.required],
      }),
      details: new FormControl(trackFieldEntryRawValue.details ?? []),
      event: new FormControl(trackFieldEntryRawValue.event, {
        validators: [Validators.required],
      }),
    });
  }

  getTrackFieldEntry(form: TrackFieldEntryFormGroup): ITrackFieldEntry | NewTrackFieldEntry {
    return form.getRawValue() as ITrackFieldEntry | NewTrackFieldEntry;
  }

  resetForm(form: TrackFieldEntryFormGroup, trackFieldEntry: TrackFieldEntryFormGroupInput): void {
    const trackFieldEntryRawValue = { ...this.getFormDefaults(), ...trackFieldEntry };
    form.reset(
      {
        ...trackFieldEntryRawValue,
        id: { value: trackFieldEntryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TrackFieldEntryFormDefaults {
    return {
      id: null,
      details: [],
    };
  }
}
