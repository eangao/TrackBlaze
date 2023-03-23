import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchool, NewSchool } from '../school.model';

export type PartialUpdateSchool = Partial<ISchool> & Pick<ISchool, 'id'>;

export type EntityResponseType = HttpResponse<ISchool>;
export type EntityArrayResponseType = HttpResponse<ISchool[]>;

@Injectable({ providedIn: 'root' })
export class SchoolService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/schools');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(school: NewSchool): Observable<EntityResponseType> {
    return this.http.post<ISchool>(this.resourceUrl, school, { observe: 'response' });
  }

  update(school: ISchool): Observable<EntityResponseType> {
    return this.http.put<ISchool>(`${this.resourceUrl}/${this.getSchoolIdentifier(school)}`, school, { observe: 'response' });
  }

  partialUpdate(school: PartialUpdateSchool): Observable<EntityResponseType> {
    return this.http.patch<ISchool>(`${this.resourceUrl}/${this.getSchoolIdentifier(school)}`, school, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISchool>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISchool[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSchoolIdentifier(school: Pick<ISchool, 'id'>): number {
    return school.id;
  }

  compareSchool(o1: Pick<ISchool, 'id'> | null, o2: Pick<ISchool, 'id'> | null): boolean {
    return o1 && o2 ? this.getSchoolIdentifier(o1) === this.getSchoolIdentifier(o2) : o1 === o2;
  }

  addSchoolToCollectionIfMissing<Type extends Pick<ISchool, 'id'>>(
    schoolCollection: Type[],
    ...schoolsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const schools: Type[] = schoolsToCheck.filter(isPresent);
    if (schools.length > 0) {
      const schoolCollectionIdentifiers = schoolCollection.map(schoolItem => this.getSchoolIdentifier(schoolItem)!);
      const schoolsToAdd = schools.filter(schoolItem => {
        const schoolIdentifier = this.getSchoolIdentifier(schoolItem);
        if (schoolCollectionIdentifiers.includes(schoolIdentifier)) {
          return false;
        }
        schoolCollectionIdentifiers.push(schoolIdentifier);
        return true;
      });
      return [...schoolsToAdd, ...schoolCollection];
    }
    return schoolCollection;
  }
}
