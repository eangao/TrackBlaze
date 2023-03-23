import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISchool, NewSchool } from '../school.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISchool for edit and NewSchoolFormGroupInput for create.
 */
type SchoolFormGroupInput = ISchool | PartialWithRequiredKeyOf<NewSchool>;

type SchoolFormDefaults = Pick<NewSchool, 'id'>;

type SchoolFormGroupContent = {
  id: FormControl<ISchool['id'] | NewSchool['id']>;
  name: FormControl<ISchool['name']>;
};

export type SchoolFormGroup = FormGroup<SchoolFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SchoolFormService {
  createSchoolFormGroup(school: SchoolFormGroupInput = { id: null }): SchoolFormGroup {
    const schoolRawValue = {
      ...this.getFormDefaults(),
      ...school,
    };
    return new FormGroup<SchoolFormGroupContent>({
      id: new FormControl(
        { value: schoolRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(schoolRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getSchool(form: SchoolFormGroup): ISchool | NewSchool {
    return form.getRawValue() as ISchool | NewSchool;
  }

  resetForm(form: SchoolFormGroup, school: SchoolFormGroupInput): void {
    const schoolRawValue = { ...this.getFormDefaults(), ...school };
    form.reset(
      {
        ...schoolRawValue,
        id: { value: schoolRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SchoolFormDefaults {
    return {
      id: null,
    };
  }
}
