import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrackFieldEntryAthleteDetails, NewTrackFieldEntryAthleteDetails } from '../track-field-entry-athlete-details.model';

export type PartialUpdateTrackFieldEntryAthleteDetails = Partial<ITrackFieldEntryAthleteDetails> &
  Pick<ITrackFieldEntryAthleteDetails, 'id'>;

export type EntityResponseType = HttpResponse<ITrackFieldEntryAthleteDetails>;
export type EntityArrayResponseType = HttpResponse<ITrackFieldEntryAthleteDetails[]>;

@Injectable({ providedIn: 'root' })
export class TrackFieldEntryAthleteDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/track-field-entry-athlete-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trackFieldEntryAthleteDetails: NewTrackFieldEntryAthleteDetails): Observable<EntityResponseType> {
    return this.http.post<ITrackFieldEntryAthleteDetails>(this.resourceUrl, trackFieldEntryAthleteDetails, { observe: 'response' });
  }

  update(trackFieldEntryAthleteDetails: ITrackFieldEntryAthleteDetails): Observable<EntityResponseType> {
    return this.http.put<ITrackFieldEntryAthleteDetails>(
      `${this.resourceUrl}/${this.getTrackFieldEntryAthleteDetailsIdentifier(trackFieldEntryAthleteDetails)}`,
      trackFieldEntryAthleteDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(trackFieldEntryAthleteDetails: PartialUpdateTrackFieldEntryAthleteDetails): Observable<EntityResponseType> {
    return this.http.patch<ITrackFieldEntryAthleteDetails>(
      `${this.resourceUrl}/${this.getTrackFieldEntryAthleteDetailsIdentifier(trackFieldEntryAthleteDetails)}`,
      trackFieldEntryAthleteDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrackFieldEntryAthleteDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrackFieldEntryAthleteDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrackFieldEntryAthleteDetailsIdentifier(trackFieldEntryAthleteDetails: Pick<ITrackFieldEntryAthleteDetails, 'id'>): number {
    return trackFieldEntryAthleteDetails.id;
  }

  compareTrackFieldEntryAthleteDetails(
    o1: Pick<ITrackFieldEntryAthleteDetails, 'id'> | null,
    o2: Pick<ITrackFieldEntryAthleteDetails, 'id'> | null
  ): boolean {
    return o1 && o2
      ? this.getTrackFieldEntryAthleteDetailsIdentifier(o1) === this.getTrackFieldEntryAthleteDetailsIdentifier(o2)
      : o1 === o2;
  }

  addTrackFieldEntryAthleteDetailsToCollectionIfMissing<Type extends Pick<ITrackFieldEntryAthleteDetails, 'id'>>(
    trackFieldEntryAthleteDetailsCollection: Type[],
    ...trackFieldEntryAthleteDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trackFieldEntryAthleteDetails: Type[] = trackFieldEntryAthleteDetailsToCheck.filter(isPresent);
    if (trackFieldEntryAthleteDetails.length > 0) {
      const trackFieldEntryAthleteDetailsCollectionIdentifiers = trackFieldEntryAthleteDetailsCollection.map(
        trackFieldEntryAthleteDetailsItem => this.getTrackFieldEntryAthleteDetailsIdentifier(trackFieldEntryAthleteDetailsItem)!
      );
      const trackFieldEntryAthleteDetailsToAdd = trackFieldEntryAthleteDetails.filter(trackFieldEntryAthleteDetailsItem => {
        const trackFieldEntryAthleteDetailsIdentifier = this.getTrackFieldEntryAthleteDetailsIdentifier(trackFieldEntryAthleteDetailsItem);
        if (trackFieldEntryAthleteDetailsCollectionIdentifiers.includes(trackFieldEntryAthleteDetailsIdentifier)) {
          return false;
        }
        trackFieldEntryAthleteDetailsCollectionIdentifiers.push(trackFieldEntryAthleteDetailsIdentifier);
        return true;
      });
      return [...trackFieldEntryAthleteDetailsToAdd, ...trackFieldEntryAthleteDetailsCollection];
    }
    return trackFieldEntryAthleteDetailsCollection;
  }
}
