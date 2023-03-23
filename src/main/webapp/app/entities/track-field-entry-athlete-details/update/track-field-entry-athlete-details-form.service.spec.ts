import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../track-field-entry-athlete-details.test-samples';

import { TrackFieldEntryAthleteDetailsFormService } from './track-field-entry-athlete-details-form.service';

describe('TrackFieldEntryAthleteDetails Form Service', () => {
  let service: TrackFieldEntryAthleteDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrackFieldEntryAthleteDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createTrackFieldEntryAthleteDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrackFieldEntryAthleteDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rank: expect.any(Object),
            timeHeightDistance: expect.any(Object),
            remarks: expect.any(Object),
            athlete: expect.any(Object),
            trackFieldEntries: expect.any(Object),
          })
        );
      });

      it('passing ITrackFieldEntryAthleteDetails should create a new form with FormGroup', () => {
        const formGroup = service.createTrackFieldEntryAthleteDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rank: expect.any(Object),
            timeHeightDistance: expect.any(Object),
            remarks: expect.any(Object),
            athlete: expect.any(Object),
            trackFieldEntries: expect.any(Object),
          })
        );
      });
    });

    describe('getTrackFieldEntryAthleteDetails', () => {
      it('should return NewTrackFieldEntryAthleteDetails for default TrackFieldEntryAthleteDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTrackFieldEntryAthleteDetailsFormGroup(sampleWithNewData);

        const trackFieldEntryAthleteDetails = service.getTrackFieldEntryAthleteDetails(formGroup) as any;

        expect(trackFieldEntryAthleteDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrackFieldEntryAthleteDetails for empty TrackFieldEntryAthleteDetails initial value', () => {
        const formGroup = service.createTrackFieldEntryAthleteDetailsFormGroup();

        const trackFieldEntryAthleteDetails = service.getTrackFieldEntryAthleteDetails(formGroup) as any;

        expect(trackFieldEntryAthleteDetails).toMatchObject({});
      });

      it('should return ITrackFieldEntryAthleteDetails', () => {
        const formGroup = service.createTrackFieldEntryAthleteDetailsFormGroup(sampleWithRequiredData);

        const trackFieldEntryAthleteDetails = service.getTrackFieldEntryAthleteDetails(formGroup) as any;

        expect(trackFieldEntryAthleteDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrackFieldEntryAthleteDetails should not enable id FormControl', () => {
        const formGroup = service.createTrackFieldEntryAthleteDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrackFieldEntryAthleteDetails should disable id FormControl', () => {
        const formGroup = service.createTrackFieldEntryAthleteDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
