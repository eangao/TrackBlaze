import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITrackFieldEntry } from '../track-field-entry.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../track-field-entry.test-samples';

import { TrackFieldEntryService } from './track-field-entry.service';

const requireRestSample: ITrackFieldEntry = {
  ...sampleWithRequiredData,
};

describe('TrackFieldEntry Service', () => {
  let service: TrackFieldEntryService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrackFieldEntry | ITrackFieldEntry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrackFieldEntryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TrackFieldEntry', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const trackFieldEntry = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trackFieldEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrackFieldEntry', () => {
      const trackFieldEntry = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trackFieldEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrackFieldEntry', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrackFieldEntry', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrackFieldEntry', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrackFieldEntryToCollectionIfMissing', () => {
      it('should add a TrackFieldEntry to an empty array', () => {
        const trackFieldEntry: ITrackFieldEntry = sampleWithRequiredData;
        expectedResult = service.addTrackFieldEntryToCollectionIfMissing([], trackFieldEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trackFieldEntry);
      });

      it('should not add a TrackFieldEntry to an array that contains it', () => {
        const trackFieldEntry: ITrackFieldEntry = sampleWithRequiredData;
        const trackFieldEntryCollection: ITrackFieldEntry[] = [
          {
            ...trackFieldEntry,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrackFieldEntryToCollectionIfMissing(trackFieldEntryCollection, trackFieldEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrackFieldEntry to an array that doesn't contain it", () => {
        const trackFieldEntry: ITrackFieldEntry = sampleWithRequiredData;
        const trackFieldEntryCollection: ITrackFieldEntry[] = [sampleWithPartialData];
        expectedResult = service.addTrackFieldEntryToCollectionIfMissing(trackFieldEntryCollection, trackFieldEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trackFieldEntry);
      });

      it('should add only unique TrackFieldEntry to an array', () => {
        const trackFieldEntryArray: ITrackFieldEntry[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trackFieldEntryCollection: ITrackFieldEntry[] = [sampleWithRequiredData];
        expectedResult = service.addTrackFieldEntryToCollectionIfMissing(trackFieldEntryCollection, ...trackFieldEntryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trackFieldEntry: ITrackFieldEntry = sampleWithRequiredData;
        const trackFieldEntry2: ITrackFieldEntry = sampleWithPartialData;
        expectedResult = service.addTrackFieldEntryToCollectionIfMissing([], trackFieldEntry, trackFieldEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trackFieldEntry);
        expect(expectedResult).toContain(trackFieldEntry2);
      });

      it('should accept null and undefined values', () => {
        const trackFieldEntry: ITrackFieldEntry = sampleWithRequiredData;
        expectedResult = service.addTrackFieldEntryToCollectionIfMissing([], null, trackFieldEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trackFieldEntry);
      });

      it('should return initial array if no TrackFieldEntry is added', () => {
        const trackFieldEntryCollection: ITrackFieldEntry[] = [sampleWithRequiredData];
        expectedResult = service.addTrackFieldEntryToCollectionIfMissing(trackFieldEntryCollection, undefined, null);
        expect(expectedResult).toEqual(trackFieldEntryCollection);
      });
    });

    describe('compareTrackFieldEntry', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrackFieldEntry(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTrackFieldEntry(entity1, entity2);
        const compareResult2 = service.compareTrackFieldEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTrackFieldEntry(entity1, entity2);
        const compareResult2 = service.compareTrackFieldEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTrackFieldEntry(entity1, entity2);
        const compareResult2 = service.compareTrackFieldEntry(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
