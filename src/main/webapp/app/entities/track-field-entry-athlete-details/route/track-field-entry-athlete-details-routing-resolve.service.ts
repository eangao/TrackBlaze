import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrackFieldEntryAthleteDetails } from '../track-field-entry-athlete-details.model';
import { TrackFieldEntryAthleteDetailsService } from '../service/track-field-entry-athlete-details.service';

@Injectable({ providedIn: 'root' })
export class TrackFieldEntryAthleteDetailsRoutingResolveService implements Resolve<ITrackFieldEntryAthleteDetails | null> {
  constructor(protected service: TrackFieldEntryAthleteDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrackFieldEntryAthleteDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trackFieldEntryAthleteDetails: HttpResponse<ITrackFieldEntryAthleteDetails>) => {
          if (trackFieldEntryAthleteDetails.body) {
            return of(trackFieldEntryAthleteDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
