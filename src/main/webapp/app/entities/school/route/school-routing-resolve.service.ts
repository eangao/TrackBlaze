import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchool } from '../school.model';
import { SchoolService } from '../service/school.service';

@Injectable({ providedIn: 'root' })
export class SchoolRoutingResolveService implements Resolve<ISchool | null> {
  constructor(protected service: SchoolService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchool | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((school: HttpResponse<ISchool>) => {
          if (school.body) {
            return of(school.body);
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
