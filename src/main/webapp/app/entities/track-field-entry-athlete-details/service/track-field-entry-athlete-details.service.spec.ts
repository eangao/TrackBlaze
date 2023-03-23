import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITrackFieldEntryAthleteDetails } from '../track-field-entry-athlete-details.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../track-field-entry-athlete-details.test-samples';

import { TrackFieldEntryAthleteDetailsService } from './track-field-entry-athlete-details.service';

const requireRestSample: ITrackFieldEntryAthleteDetails = {
  ...sampleWithRequiredData,
};

describe('TrackFieldEntryAthleteDetails Service', () => {
  let service: TrackFieldEntryAthleteDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrackFieldEntryAthleteDetails | ITrackFieldEntryAthleteDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrackFieldEntryAthleteDetailsService);
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

    it('should create a TrackFieldEntryAthleteDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const trackFieldEntryAthleteDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trackFieldEntryAthleteDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrackFieldEntryAthleteDetails', () => {
      const trackFieldEntryAthleteDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trackFieldEntryAthleteDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrackFieldEntryAthleteDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrackFieldEntryAthleteDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrackFieldEntryAthleteDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrackFieldEntryAthleteDetailsToCollectionIfMissing', () => {
      it('should add a TrackFieldEntryAthleteDetails to an empty array', () => {
        const trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails = sampleWithRequiredData;
        expectedResult = service.addTrackFieldEntryAthleteDetailsToCollectionIfMissing([], trackFieldEntryAthleteDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trackFieldEntryAthleteDetails);
      });

      it('should not add a TrackFieldEntryAthleteDetails to an array that contains it', () => {
        const trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails = sampleWithRequiredData;
        const trackFieldEntryAthleteDetailsCollection: ITrackFieldEntryAthleteDetails[] = [
          {
            ...trackFieldEntryAthleteDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrackFieldEntryAthleteDetailsToCollectionIfMissing(
          trackFieldEntryAthleteDetailsCollection,
          trackFieldEntryAthleteDetails
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrackFieldEntryAthleteDetails to an array that doesn't contain it", () => {
        const trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails = sampleWithRequiredData;
        const trackFieldEntryAthleteDetailsCollection: ITrackFieldEntryAthleteDetails[] = [sampleWithPartialData];
        expectedResult = service.addTrackFieldEntryAthleteDetailsToCollectionIfMissing(
          trackFieldEntryAthleteDetailsCollection,
          trackFieldEntryAthleteDetails
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trackFieldEntryAthleteDetails);
      });

      it('should add only unique TrackFieldEntryAthleteDetails to an array', () => {
        const trackFieldEntryAthleteDetailsArray: ITrackFieldEntryAthleteDetails[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const trackFieldEntryAthleteDetailsCollection: ITrackFieldEntryAthleteDetails[] = [sampleWithRequiredData];
        expectedResult = service.addTrackFieldEntryAthleteDetailsToCollectionIfMissing(
          trackFieldEntryAthleteDetailsCollection,
          ...trackFieldEntryAthleteDetailsArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails = sampleWithRequiredData;
        const trackFieldEntryAthleteDetails2: ITrackFieldEntryAthleteDetails = sampleWithPartialData;
        expectedResult = service.addTrackFieldEntryAthleteDetailsToCollectionIfMissing(
          [],
          trackFieldEntryAthleteDetails,
          trackFieldEntryAthleteDetails2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trackFieldEntryAthleteDetails);
        expect(expectedResult).toContain(trackFieldEntryAthleteDetails2);
      });

      it('should accept null and undefined values', () => {
        const trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails = sampleWithRequiredData;
        expectedResult = service.addTrackFieldEntryAthleteDetailsToCollectionIfMissing([], null, trackFieldEntryAthleteDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trackFieldEntryAthleteDetails);
      });

      it('should return initial array if no TrackFieldEntryAthleteDetails is added', () => {
        const trackFieldEntryAthleteDetailsCollection: ITrackFieldEntryAthleteDetails[] = [sampleWithRequiredData];
        expectedResult = service.addTrackFieldEntryAthleteDetailsToCollectionIfMissing(
          trackFieldEntryAthleteDetailsCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(trackFieldEntryAthleteDetailsCollection);
      });
    });

    describe('compareTrackFieldEntryAthleteDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrackFieldEntryAthleteDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTrackFieldEntryAthleteDetails(entity1, entity2);
        const compareResult2 = service.compareTrackFieldEntryAthleteDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTrackFieldEntryAthleteDetails(entity1, entity2);
        const compareResult2 = service.compareTrackFieldEntryAthleteDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTrackFieldEntryAthleteDetails(entity1, entity2);
        const compareResult2 = service.compareTrackFieldEntryAthleteDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
