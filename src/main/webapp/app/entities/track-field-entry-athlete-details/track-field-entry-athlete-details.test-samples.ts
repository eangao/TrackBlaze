import { ITrackFieldEntryAthleteDetails, NewTrackFieldEntryAthleteDetails } from './track-field-entry-athlete-details.model';

export const sampleWithRequiredData: ITrackFieldEntryAthleteDetails = {
  id: 5129,
};

export const sampleWithPartialData: ITrackFieldEntryAthleteDetails = {
  id: 44458,
  rank: 62211,
  remarks: 'transmitting payment',
};

export const sampleWithFullData: ITrackFieldEntryAthleteDetails = {
  id: 60693,
  rank: 14425,
  timeHeightDistance: 'Steel violet compress',
  remarks: 'Japan Flat',
};

export const sampleWithNewData: NewTrackFieldEntryAthleteDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
