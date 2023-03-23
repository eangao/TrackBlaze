import dayjs from 'dayjs/esm';
import { IEvent } from 'app/entities/event/event.model';
import { ISchool } from 'app/entities/school/school.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IAthlete {
  id: number;
  firstName?: string | null;
  middleName?: string | null;
  lastName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  gender?: Gender | null;
  picture?: string | null;
  pictureContentType?: string | null;
  userId?: number | null;
  events?: Pick<IEvent, 'id' | 'name'>[] | null;
  school?: Pick<ISchool, 'id' | 'name'> | null;
}

export type NewAthlete = Omit<IAthlete, 'id'> & { id: null };
