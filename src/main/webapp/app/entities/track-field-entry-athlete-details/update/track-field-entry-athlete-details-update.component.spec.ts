import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TrackFieldEntryAthleteDetailsFormService } from './track-field-entry-athlete-details-form.service';
import { TrackFieldEntryAthleteDetailsService } from '../service/track-field-entry-athlete-details.service';
import { ITrackFieldEntryAthleteDetails } from '../track-field-entry-athlete-details.model';
import { IAthlete } from 'app/entities/athlete/athlete.model';
import { AthleteService } from 'app/entities/athlete/service/athlete.service';

import { TrackFieldEntryAthleteDetailsUpdateComponent } from './track-field-entry-athlete-details-update.component';

describe('TrackFieldEntryAthleteDetails Management Update Component', () => {
  let comp: TrackFieldEntryAthleteDetailsUpdateComponent;
  let fixture: ComponentFixture<TrackFieldEntryAthleteDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trackFieldEntryAthleteDetailsFormService: TrackFieldEntryAthleteDetailsFormService;
  let trackFieldEntryAthleteDetailsService: TrackFieldEntryAthleteDetailsService;
  let athleteService: AthleteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TrackFieldEntryAthleteDetailsUpdateComponent],
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
      .overrideTemplate(TrackFieldEntryAthleteDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrackFieldEntryAthleteDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trackFieldEntryAthleteDetailsFormService = TestBed.inject(TrackFieldEntryAthleteDetailsFormService);
    trackFieldEntryAthleteDetailsService = TestBed.inject(TrackFieldEntryAthleteDetailsService);
    athleteService = TestBed.inject(AthleteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Athlete query and add missing value', () => {
      const trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails = { id: 456 };
      const athlete: IAthlete = { id: 16428 };
      trackFieldEntryAthleteDetails.athlete = athlete;

      const athleteCollection: IAthlete[] = [{ id: 45095 }];
      jest.spyOn(athleteService, 'query').mockReturnValue(of(new HttpResponse({ body: athleteCollection })));
      const additionalAthletes = [athlete];
      const expectedCollection: IAthlete[] = [...additionalAthletes, ...athleteCollection];
      jest.spyOn(athleteService, 'addAthleteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trackFieldEntryAthleteDetails });
      comp.ngOnInit();

      expect(athleteService.query).toHaveBeenCalled();
      expect(athleteService.addAthleteToCollectionIfMissing).toHaveBeenCalledWith(
        athleteCollection,
        ...additionalAthletes.map(expect.objectContaining)
      );
      expect(comp.athletesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails = { id: 456 };
      const athlete: IAthlete = { id: 56854 };
      trackFieldEntryAthleteDetails.athlete = athlete;

      activatedRoute.data = of({ trackFieldEntryAthleteDetails });
      comp.ngOnInit();

      expect(comp.athletesSharedCollection).toContain(athlete);
      expect(comp.trackFieldEntryAthleteDetails).toEqual(trackFieldEntryAthleteDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrackFieldEntryAthleteDetails>>();
      const trackFieldEntryAthleteDetails = { id: 123 };
      jest
        .spyOn(trackFieldEntryAthleteDetailsFormService, 'getTrackFieldEntryAthleteDetails')
        .mockReturnValue(trackFieldEntryAthleteDetails);
      jest.spyOn(trackFieldEntryAthleteDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackFieldEntryAthleteDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trackFieldEntryAthleteDetails }));
      saveSubject.complete();

      // THEN
      expect(trackFieldEntryAthleteDetailsFormService.getTrackFieldEntryAthleteDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trackFieldEntryAthleteDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(trackFieldEntryAthleteDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrackFieldEntryAthleteDetails>>();
      const trackFieldEntryAthleteDetails = { id: 123 };
      jest.spyOn(trackFieldEntryAthleteDetailsFormService, 'getTrackFieldEntryAthleteDetails').mockReturnValue({ id: null });
      jest.spyOn(trackFieldEntryAthleteDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackFieldEntryAthleteDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trackFieldEntryAthleteDetails }));
      saveSubject.complete();

      // THEN
      expect(trackFieldEntryAthleteDetailsFormService.getTrackFieldEntryAthleteDetails).toHaveBeenCalled();
      expect(trackFieldEntryAthleteDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrackFieldEntryAthleteDetails>>();
      const trackFieldEntryAthleteDetails = { id: 123 };
      jest.spyOn(trackFieldEntryAthleteDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackFieldEntryAthleteDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trackFieldEntryAthleteDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAthlete', () => {
      it('Should forward to athleteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(athleteService, 'compareAthlete');
        comp.compareAthlete(entity, entity2);
        expect(athleteService.compareAthlete).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
