import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TrackFieldEntryFormService } from './track-field-entry-form.service';
import { TrackFieldEntryService } from '../service/track-field-entry.service';
import { ITrackFieldEntry } from '../track-field-entry.model';
import { ITrackFieldEntryAthleteDetails } from 'app/entities/track-field-entry-athlete-details/track-field-entry-athlete-details.model';
import { TrackFieldEntryAthleteDetailsService } from 'app/entities/track-field-entry-athlete-details/service/track-field-entry-athlete-details.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

import { TrackFieldEntryUpdateComponent } from './track-field-entry-update.component';

describe('TrackFieldEntry Management Update Component', () => {
  let comp: TrackFieldEntryUpdateComponent;
  let fixture: ComponentFixture<TrackFieldEntryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trackFieldEntryFormService: TrackFieldEntryFormService;
  let trackFieldEntryService: TrackFieldEntryService;
  let trackFieldEntryAthleteDetailsService: TrackFieldEntryAthleteDetailsService;
  let eventService: EventService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TrackFieldEntryUpdateComponent],
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
      .overrideTemplate(TrackFieldEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrackFieldEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trackFieldEntryFormService = TestBed.inject(TrackFieldEntryFormService);
    trackFieldEntryService = TestBed.inject(TrackFieldEntryService);
    trackFieldEntryAthleteDetailsService = TestBed.inject(TrackFieldEntryAthleteDetailsService);
    eventService = TestBed.inject(EventService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TrackFieldEntryAthleteDetails query and add missing value', () => {
      const trackFieldEntry: ITrackFieldEntry = { id: 456 };
      const details: ITrackFieldEntryAthleteDetails[] = [{ id: 37027 }];
      trackFieldEntry.details = details;

      const trackFieldEntryAthleteDetailsCollection: ITrackFieldEntryAthleteDetails[] = [{ id: 94028 }];
      jest
        .spyOn(trackFieldEntryAthleteDetailsService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: trackFieldEntryAthleteDetailsCollection })));
      const additionalTrackFieldEntryAthleteDetails = [...details];
      const expectedCollection: ITrackFieldEntryAthleteDetails[] = [
        ...additionalTrackFieldEntryAthleteDetails,
        ...trackFieldEntryAthleteDetailsCollection,
      ];
      jest
        .spyOn(trackFieldEntryAthleteDetailsService, 'addTrackFieldEntryAthleteDetailsToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trackFieldEntry });
      comp.ngOnInit();

      expect(trackFieldEntryAthleteDetailsService.query).toHaveBeenCalled();
      expect(trackFieldEntryAthleteDetailsService.addTrackFieldEntryAthleteDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        trackFieldEntryAthleteDetailsCollection,
        ...additionalTrackFieldEntryAthleteDetails.map(expect.objectContaining)
      );
      expect(comp.trackFieldEntryAthleteDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Event query and add missing value', () => {
      const trackFieldEntry: ITrackFieldEntry = { id: 456 };
      const event: IEvent = { id: 70449 };
      trackFieldEntry.event = event;

      const eventCollection: IEvent[] = [{ id: 20745 }];
      jest.spyOn(eventService, 'query').mockReturnValue(of(new HttpResponse({ body: eventCollection })));
      const additionalEvents = [event];
      const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
      jest.spyOn(eventService, 'addEventToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trackFieldEntry });
      comp.ngOnInit();

      expect(eventService.query).toHaveBeenCalled();
      expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(
        eventCollection,
        ...additionalEvents.map(expect.objectContaining)
      );
      expect(comp.eventsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const trackFieldEntry: ITrackFieldEntry = { id: 456 };
      const details: ITrackFieldEntryAthleteDetails = { id: 31826 };
      trackFieldEntry.details = [details];
      const event: IEvent = { id: 12591 };
      trackFieldEntry.event = event;

      activatedRoute.data = of({ trackFieldEntry });
      comp.ngOnInit();

      expect(comp.trackFieldEntryAthleteDetailsSharedCollection).toContain(details);
      expect(comp.eventsSharedCollection).toContain(event);
      expect(comp.trackFieldEntry).toEqual(trackFieldEntry);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrackFieldEntry>>();
      const trackFieldEntry = { id: 123 };
      jest.spyOn(trackFieldEntryFormService, 'getTrackFieldEntry').mockReturnValue(trackFieldEntry);
      jest.spyOn(trackFieldEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackFieldEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trackFieldEntry }));
      saveSubject.complete();

      // THEN
      expect(trackFieldEntryFormService.getTrackFieldEntry).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trackFieldEntryService.update).toHaveBeenCalledWith(expect.objectContaining(trackFieldEntry));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrackFieldEntry>>();
      const trackFieldEntry = { id: 123 };
      jest.spyOn(trackFieldEntryFormService, 'getTrackFieldEntry').mockReturnValue({ id: null });
      jest.spyOn(trackFieldEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackFieldEntry: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trackFieldEntry }));
      saveSubject.complete();

      // THEN
      expect(trackFieldEntryFormService.getTrackFieldEntry).toHaveBeenCalled();
      expect(trackFieldEntryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrackFieldEntry>>();
      const trackFieldEntry = { id: 123 };
      jest.spyOn(trackFieldEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackFieldEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trackFieldEntryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTrackFieldEntryAthleteDetails', () => {
      it('Should forward to trackFieldEntryAthleteDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(trackFieldEntryAthleteDetailsService, 'compareTrackFieldEntryAthleteDetails');
        comp.compareTrackFieldEntryAthleteDetails(entity, entity2);
        expect(trackFieldEntryAthleteDetailsService.compareTrackFieldEntryAthleteDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEvent', () => {
      it('Should forward to eventService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(eventService, 'compareEvent');
        comp.compareEvent(entity, entity2);
        expect(eventService.compareEvent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
