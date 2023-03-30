import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AthleteFormService, AthleteFormGroup } from './athlete-form.service';
import { IAthlete } from '../athlete.model';
import { AthleteService } from '../service/athlete.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolService } from 'app/entities/school/service/school.service';
import { Gender } from 'app/entities/enumerations/gender.model';

@Component({
  selector: 'jhi-athlete-update',
  templateUrl: './athlete-update.component.html',
})
export class AthleteUpdateComponent implements OnInit {
  isSaving = false;
  athlete: IAthlete | null = null;
  genderValues = Object.keys(Gender);

  eventsSharedCollection: IEvent[] = [];
  schoolsSharedCollection: ISchool[] = [];

  editForm: AthleteFormGroup = this.athleteFormService.createAthleteFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected athleteService: AthleteService,
    protected athleteFormService: AthleteFormService,
    protected eventService: EventService,
    protected schoolService: SchoolService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEvent = (o1: IEvent | null, o2: IEvent | null): boolean => this.eventService.compareEvent(o1, o2);

  compareSchool = (o1: ISchool | null, o2: ISchool | null): boolean => this.schoolService.compareSchool(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ athlete }) => {
      this.athlete = athlete;
      if (athlete) {
        this.updateForm(athlete);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('trackBlazeApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const athlete = this.athleteFormService.getAthlete(this.editForm);
    if (athlete.id !== null) {
      this.subscribeToSaveResponse(this.athleteService.update(athlete));
    } else {
      this.subscribeToSaveResponse(this.athleteService.create(athlete));
    }
  }

  isSelected(eventOption: any): boolean {
    const selectedOptions = this.editForm.get('events')!.value ?? [];
    return selectedOptions.some((option: any) => option.id === eventOption.id);
  }

  toggleSelection(event: any, eventOption: any): void {
    const selectedOptions = this.editForm.get('events')!.value ?? [];
    const index = selectedOptions.findIndex((option: any) => option.id === eventOption.id);

    if (event.target.checked) {
      if (index === -1) {
        selectedOptions.push(eventOption);
      }
    } else {
      if (index !== -1) {
        selectedOptions.splice(index, 1);
      }
    }

    this.editForm.patchValue({ events: selectedOptions });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAthlete>>): void {
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

  protected updateForm(athlete: IAthlete): void {
    this.athlete = athlete;
    this.athleteFormService.resetForm(this.editForm, athlete);

    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing<IEvent>(
      this.eventsSharedCollection,
      ...(athlete.events ?? [])
    );
    this.schoolsSharedCollection = this.schoolService.addSchoolToCollectionIfMissing<ISchool>(this.schoolsSharedCollection, athlete.school);
  }

  protected loadRelationshipsOptions(): void {
    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing<IEvent>(events, ...(this.athlete?.events ?? []))))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));

    this.schoolService
      .query()
      .pipe(map((res: HttpResponse<ISchool[]>) => res.body ?? []))
      .pipe(map((schools: ISchool[]) => this.schoolService.addSchoolToCollectionIfMissing<ISchool>(schools, this.athlete?.school)))
      .subscribe((schools: ISchool[]) => (this.schoolsSharedCollection = schools));
  }
}
