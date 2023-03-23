import { ITrackFieldEntryAthleteDetails } from 'app/entities/track-field-entry-athlete-details/track-field-entry-athlete-details.model';
import { IEvent } from 'app/entities/event/event.model';
import { Category } from 'app/entities/enumerations/category.model';

export interface ITrackFieldEntry {
  id: number;
  description?: string | null;
  category?: Category | null;
  details?: Pick<ITrackFieldEntryAthleteDetails, 'id'>[] | null;
  event?: Pick<IEvent, 'id' | 'name'> | null;
}

export type NewTrackFieldEntry = Omit<ITrackFieldEntry, 'id'> & { id: null };
