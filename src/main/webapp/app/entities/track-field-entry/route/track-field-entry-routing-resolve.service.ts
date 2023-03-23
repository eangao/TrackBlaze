import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrackFieldEntry } from '../track-field-entry.model';
import { TrackFieldEntryService } from '../service/track-field-entry.service';

@Injectable({ providedIn: 'root' })
export class TrackFieldEntryRoutingResolveService implements Resolve<ITrackFieldEntry | null> {
  constructor(protected service: TrackFieldEntryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrackFieldEntry | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trackFieldEntry: HttpResponse<ITrackFieldEntry>) => {
          if (trackFieldEntry.body) {
            return of(trackFieldEntry.body);
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
