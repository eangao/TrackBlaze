import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AthleteComponent } from '../list/athlete.component';
import { AthleteDetailComponent } from '../detail/athlete-detail.component';
import { AthleteUpdateComponent } from '../update/athlete-update.component';
import { AthleteRoutingResolveService } from './athlete-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const athleteRoute: Routes = [
  {
    path: '',
    component: AthleteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AthleteDetailComponent,
    resolve: {
      athlete: AthleteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AthleteUpdateComponent,
    resolve: {
      athlete: AthleteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AthleteUpdateComponent,
    resolve: {
      athlete: AthleteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(athleteRoute)],
  exports: [RouterModule],
})
export class AthleteRoutingModule {}
