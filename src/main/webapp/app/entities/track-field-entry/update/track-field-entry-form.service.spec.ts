import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../track-field-entry.test-samples';

import { TrackFieldEntryFormService } from './track-field-entry-form.service';

describe('TrackFieldEntry Form Service', () => {
  let service: TrackFieldEntryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrackFieldEntryFormService);
  });

  describe('Service methods', () => {
    describe('createTrackFieldEntryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrackFieldEntryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            category: expect.any(Object),
            details: expect.any(Object),
            event: expect.any(Object),
          })
        );
      });

      it('passing ITrackFieldEntry should create a new form with FormGroup', () => {
        const formGroup = service.createTrackFieldEntryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            category: expect.any(Object),
            details: expect.any(Object),
            event: expect.any(Object),
          })
        );
      });
    });

    describe('getTrackFieldEntry', () => {
      it('should return NewTrackFieldEntry for default TrackFieldEntry initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTrackFieldEntryFormGroup(sampleWithNewData);

        const trackFieldEntry = service.getTrackFieldEntry(formGroup) as any;

        expect(trackFieldEntry).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrackFieldEntry for empty TrackFieldEntry initial value', () => {
        const formGroup = service.createTrackFieldEntryFormGroup();

        const trackFieldEntry = service.getTrackFieldEntry(formGroup) as any;

        expect(trackFieldEntry).toMatchObject({});
      });

      it('should return ITrackFieldEntry', () => {
        const formGroup = service.createTrackFieldEntryFormGroup(sampleWithRequiredData);

        const trackFieldEntry = service.getTrackFieldEntry(formGroup) as any;

        expect(trackFieldEntry).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrackFieldEntry should not enable id FormControl', () => {
        const formGroup = service.createTrackFieldEntryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrackFieldEntry should disable id FormControl', () => {
        const formGroup = service.createTrackFieldEntryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
