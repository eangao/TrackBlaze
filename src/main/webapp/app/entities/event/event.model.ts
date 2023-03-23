import { IAthlete } from 'app/entities/athlete/athlete.model';

export interface IEvent {
  id: number;
  name?: string | null;
  athletes?: Pick<IAthlete, 'id'>[] | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
