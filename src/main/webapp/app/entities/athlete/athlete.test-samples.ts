import dayjs from 'dayjs/esm';

import { Gender } from 'app/entities/enumerations/gender.model';

import { IAthlete, NewAthlete } from './athlete.model';

export const sampleWithRequiredData: IAthlete = {
  id: 38108,
  firstName: 'Kobe',
  lastName: 'Sanford',
  birthDate: dayjs('2023-03-22'),
  gender: Gender['MALE'],
};

export const sampleWithPartialData: IAthlete = {
  id: 30782,
  firstName: 'Beulah',
  middleName: 'Loan Texas e-services',
  lastName: 'Williamson',
  birthDate: dayjs('2023-03-22'),
  gender: Gender['FEMALE'],
};

export const sampleWithFullData: IAthlete = {
  id: 20870,
  firstName: 'Brent',
  middleName: 'payment compressing 1080p',
  lastName: 'Jakubowski',
  birthDate: dayjs('2023-03-22'),
  gender: Gender['FEMALE'],
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  userId: 15764,
};

export const sampleWithNewData: NewAthlete = {
  firstName: 'Ignatius',
  lastName: 'Emmerich',
  birthDate: dayjs('2023-03-22'),
  gender: Gender['MALE'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
