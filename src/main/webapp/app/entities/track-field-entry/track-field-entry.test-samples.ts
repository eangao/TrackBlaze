import { Category } from 'app/entities/enumerations/category.model';

import { ITrackFieldEntry, NewTrackFieldEntry } from './track-field-entry.model';

export const sampleWithRequiredData: ITrackFieldEntry = {
  id: 15015,
  description: 'invoice',
  category: Category['SECONDARY_BOYS'],
};

export const sampleWithPartialData: ITrackFieldEntry = {
  id: 93140,
  description: 'Industrial',
  category: Category['ELEMENTARY_GIRLS'],
};

export const sampleWithFullData: ITrackFieldEntry = {
  id: 82736,
  description: 'extranet invoice',
  category: Category['SECONDARY_BOYS'],
};

export const sampleWithNewData: NewTrackFieldEntry = {
  description: 'Avon metrics synthesizing',
  category: Category['SECONDARY_GIRLS'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
