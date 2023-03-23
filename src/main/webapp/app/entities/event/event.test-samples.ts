import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 63022,
  name: 'Cotton',
};

export const sampleWithPartialData: IEvent = {
  id: 50740,
  name: 'Games',
};

export const sampleWithFullData: IEvent = {
  id: 852,
  name: 'pixel',
};

export const sampleWithNewData: NewEvent = {
  name: 'withdrawal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
