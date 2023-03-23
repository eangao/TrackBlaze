import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SchoolFormService, SchoolFormGroup } from './school-form.service';
import { ISchool } from '../school.model';
import { SchoolService } from '../service/school.service';

@Component({
  selector: 'jhi-school-update',
  templateUrl: './school-update.component.html',
})
export class SchoolUpdateComponent implements OnInit {
  isSaving = false;
  school: ISchool | null = null;

  editForm: SchoolFormGroup = this.schoolFormService.createSchoolFormGroup();

  constructor(
    protected schoolService: SchoolService,
    protected schoolFormService: SchoolFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ school }) => {
      this.school = school;
      if (school) {
        this.updateForm(school);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const school = this.schoolFormService.getSchool(this.editForm);
    if (school.id !== null) {
      this.subscribeToSaveResponse(this.schoolService.update(school));
    } else {
      this.subscribeToSaveResponse(this.schoolService.create(school));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchool>>): void {
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

  protected updateForm(school: ISchool): void {
    this.school = school;
    this.schoolFormService.resetForm(this.editForm, school);
  }
}
