import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrackFieldEntry, NewTrackFieldEntry } from '../track-field-entry.model';

export type PartialUpdateTrackFieldEntry = Partial<ITrackFieldEntry> & Pick<ITrackFieldEntry, 'id'>;

export type EntityResponseType = HttpResponse<ITrackFieldEntry>;
export type EntityArrayResponseType = HttpResponse<ITrackFieldEntry[]>;

@Injectable({ providedIn: 'root' })
export class TrackFieldEntryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/track-field-entries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trackFieldEntry: NewTrackFieldEntry): Observable<EntityResponseType> {
    return this.http.post<ITrackFieldEntry>(this.resourceUrl, trackFieldEntry, { observe: 'response' });
  }

  update(trackFieldEntry: ITrackFieldEntry): Observable<EntityResponseType> {
    return this.http.put<ITrackFieldEntry>(`${this.resourceUrl}/${this.getTrackFieldEntryIdentifier(trackFieldEntry)}`, trackFieldEntry, {
      observe: 'response',
    });
  }

  partialUpdate(trackFieldEntry: PartialUpdateTrackFieldEntry): Observable<EntityResponseType> {
    return this.http.patch<ITrackFieldEntry>(`${this.resourceUrl}/${this.getTrackFieldEntryIdentifier(trackFieldEntry)}`, trackFieldEntry, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrackFieldEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrackFieldEntry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrackFieldEntryIdentifier(trackFieldEntry: Pick<ITrackFieldEntry, 'id'>): number {
    return trackFieldEntry.id;
  }

  compareTrackFieldEntry(o1: Pick<ITrackFieldEntry, 'id'> | null, o2: Pick<ITrackFieldEntry, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrackFieldEntryIdentifier(o1) === this.getTrackFieldEntryIdentifier(o2) : o1 === o2;
  }

  addTrackFieldEntryToCollectionIfMissing<Type extends Pick<ITrackFieldEntry, 'id'>>(
    trackFieldEntryCollection: Type[],
    ...trackFieldEntriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trackFieldEntries: Type[] = trackFieldEntriesToCheck.filter(isPresent);
    if (trackFieldEntries.length > 0) {
      const trackFieldEntryCollectionIdentifiers = trackFieldEntryCollection.map(
        trackFieldEntryItem => this.getTrackFieldEntryIdentifier(trackFieldEntryItem)!
      );
      const trackFieldEntriesToAdd = trackFieldEntries.filter(trackFieldEntryItem => {
        const trackFieldEntryIdentifier = this.getTrackFieldEntryIdentifier(trackFieldEntryItem);
        if (trackFieldEntryCollectionIdentifiers.includes(trackFieldEntryIdentifier)) {
          return false;
        }
        trackFieldEntryCollectionIdentifiers.push(trackFieldEntryIdentifier);
        return true;
      });
      return [...trackFieldEntriesToAdd, ...trackFieldEntryCollection];
    }
    return trackFieldEntryCollection;
  }
}
