import { ISchool, NewSchool } from './school.model';

export const sampleWithRequiredData: ISchool = {
  id: 82208,
  name: 'Botswana productivity connecting',
};

export const sampleWithPartialData: ISchool = {
  id: 50901,
  name: 'regional Bike Cambridgeshire',
};

export const sampleWithFullData: ISchool = {
  id: 50340,
  name: 'transmitting',
};

export const sampleWithNewData: NewSchool = {
  name: 'invoice Fish',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
