import { IAthlete } from 'app/entities/athlete/athlete.model';
import { ITrackFieldEntry } from 'app/entities/track-field-entry/track-field-entry.model';

export interface ITrackFieldEntryAthleteDetails {
  id: number;
  rank?: number | null;
  timeHeightDistance?: string | null;
  remarks?: string | null;
  athlete?: Pick<IAthlete, 'id'> | null;
  trackFieldEntries?: Pick<ITrackFieldEntry, 'id'>[] | null;
}

export type NewTrackFieldEntryAthleteDetails = Omit<ITrackFieldEntryAthleteDetails, 'id'> & { id: null };
