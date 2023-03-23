import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AthleteFormService } from './athlete-form.service';
import { AthleteService } from '../service/athlete.service';
import { IAthlete } from '../athlete.model';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { ISchool } from 'app/entities/school/school.model';
import { SchoolService } from 'app/entities/school/service/school.service';

import { AthleteUpdateComponent } from './athlete-update.component';

describe('Athlete Management Update Component', () => {
  let comp: AthleteUpdateComponent;
  let fixture: ComponentFixture<AthleteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let athleteFormService: AthleteFormService;
  let athleteService: AthleteService;
  let eventService: EventService;
  let schoolService: SchoolService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AthleteUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AthleteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AthleteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    athleteFormService = TestBed.inject(AthleteFormService);
    athleteService = TestBed.inject(AthleteService);
    eventService = TestBed.inject(EventService);
    schoolService = TestBed.inject(SchoolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Event query and add missing value', () => {
      const athlete: IAthlete = { id: 456 };
      const events: IEvent[] = [{ id: 29391 }];
      athlete.events = events;

      const eventCollection: IEvent[] = [{ id: 12654 }];
      jest.spyOn(eventService, 'query').mockReturnValue(of(new HttpResponse({ body: eventCollection })));
      const additionalEvents = [...events];
      const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
      jest.spyOn(eventService, 'addEventToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ athlete });
      comp.ngOnInit();

      expect(eventService.query).toHaveBeenCalled();
      expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(
        eventCollection,
        ...additionalEvents.map(expect.objectContaining)
      );
      expect(comp.eventsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call School query and add missing value', () => {
      const athlete: IAthlete = { id: 456 };
      const school: ISchool = { id: 54765 };
      athlete.school = school;

      const schoolCollection: ISchool[] = [{ id: 94375 }];
      jest.spyOn(schoolService, 'query').mockReturnValue(of(new HttpResponse({ body: schoolCollection })));
      const additionalSchools = [school];
      const expectedCollection: ISchool[] = [...additionalSchools, ...schoolCollection];
      jest.spyOn(schoolService, 'addSchoolToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ athlete });
      comp.ngOnInit();

      expect(schoolService.query).toHaveBeenCalled();
      expect(schoolService.addSchoolToCollectionIfMissing).toHaveBeenCalledWith(
        schoolCollection,
        ...additionalSchools.map(expect.objectContaining)
      );
      expect(comp.schoolsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const athlete: IAthlete = { id: 456 };
      const event: IEvent = { id: 83043 };
      athlete.events = [event];
      const school: ISchool = { id: 58904 };
      athlete.school = school;

      activatedRoute.data = of({ athlete });
      comp.ngOnInit();

      expect(comp.eventsSharedCollection).toContain(event);
      expect(comp.schoolsSharedCollection).toContain(school);
      expect(comp.athlete).toEqual(athlete);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAthlete>>();
      const athlete = { id: 123 };
      jest.spyOn(athleteFormService, 'getAthlete').mockReturnValue(athlete);
      jest.spyOn(athleteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ athlete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: athlete }));
      saveSubject.complete();

      // THEN
      expect(athleteFormService.getAthlete).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(athleteService.update).toHaveBeenCalledWith(expect.objectContaining(athlete));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAthlete>>();
      const athlete = { id: 123 };
      jest.spyOn(athleteFormService, 'getAthlete').mockReturnValue({ id: null });
      jest.spyOn(athleteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ athlete: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: athlete }));
      saveSubject.complete();

      // THEN
      expect(athleteFormService.getAthlete).toHaveBeenCalled();
      expect(athleteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAthlete>>();
      const athlete = { id: 123 };
      jest.spyOn(athleteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ athlete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(athleteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEvent', () => {
      it('Should forward to eventService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(eventService, 'compareEvent');
        comp.compareEvent(entity, entity2);
        expect(eventService.compareEvent).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSchool', () => {
      it('Should forward to schoolService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(schoolService, 'compareSchool');
        comp.compareSchool(entity, entity2);
        expect(schoolService.compareSchool).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
