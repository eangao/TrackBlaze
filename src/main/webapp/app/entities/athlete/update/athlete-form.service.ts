import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAthlete, NewAthlete } from '../athlete.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAthlete for edit and NewAthleteFormGroupInput for create.
 */
type AthleteFormGroupInput = IAthlete | PartialWithRequiredKeyOf<NewAthlete>;

type AthleteFormDefaults = Pick<NewAthlete, 'id' | 'events'>;

type AthleteFormGroupContent = {
  id: FormControl<IAthlete['id'] | NewAthlete['id']>;
  firstName: FormControl<IAthlete['firstName']>;
  middleName: FormControl<IAthlete['middleName']>;
  lastName: FormControl<IAthlete['lastName']>;
  birthDate: FormControl<IAthlete['birthDate']>;
  gender: FormControl<IAthlete['gender']>;
  picture: FormControl<IAthlete['picture']>;
  pictureContentType: FormControl<IAthlete['pictureContentType']>;
  userId: FormControl<IAthlete['userId']>;
  events: FormControl<IAthlete['events']>;
  school: FormControl<IAthlete['school']>;
};

export type AthleteFormGroup = FormGroup<AthleteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AthleteFormService {
  createAthleteFormGroup(athlete: AthleteFormGroupInput = { id: null }): AthleteFormGroup {
    const athleteRawValue = {
      ...this.getFormDefaults(),
      ...athlete,
    };
    return new FormGroup<AthleteFormGroupContent>({
      id: new FormControl(
        { value: athleteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(athleteRawValue.firstName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      middleName: new FormControl(athleteRawValue.middleName, {
        validators: [Validators.maxLength(50)],
      }),
      lastName: new FormControl(athleteRawValue.lastName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      birthDate: new FormControl(athleteRawValue.birthDate, {
        validators: [Validators.required],
      }),
      gender: new FormControl(athleteRawValue.gender, {
        validators: [Validators.required],
      }),
      picture: new FormControl(athleteRawValue.picture),
      pictureContentType: new FormControl(athleteRawValue.pictureContentType),
      userId: new FormControl(athleteRawValue.userId),
      events: new FormControl(athleteRawValue.events ?? []),
      school: new FormControl(athleteRawValue.school, {
        validators: [Validators.required],
      }),
    });
  }

  getAthlete(form: AthleteFormGroup): IAthlete | NewAthlete {
    return form.getRawValue() as IAthlete | NewAthlete;
  }

  resetForm(form: AthleteFormGroup, athlete: AthleteFormGroupInput): void {
    const athleteRawValue = { ...this.getFormDefaults(), ...athlete };
    form.reset(
      {
        ...athleteRawValue,
        id: { value: athleteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AthleteFormDefaults {
    return {
      id: null,
      events: [],
    };
  }
}
