import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrackFieldEntryAthleteDetailsComponent } from '../list/track-field-entry-athlete-details.component';
import { TrackFieldEntryAthleteDetailsDetailComponent } from '../detail/track-field-entry-athlete-details-detail.component';
import { TrackFieldEntryAthleteDetailsUpdateComponent } from '../update/track-field-entry-athlete-details-update.component';
import { TrackFieldEntryAthleteDetailsRoutingResolveService } from './track-field-entry-athlete-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const trackFieldEntryAthleteDetailsRoute: Routes = [
  {
    path: '',
    component: TrackFieldEntryAthleteDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrackFieldEntryAthleteDetailsDetailComponent,
    resolve: {
      trackFieldEntryAthleteDetails: TrackFieldEntryAthleteDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrackFieldEntryAthleteDetailsUpdateComponent,
    resolve: {
      trackFieldEntryAthleteDetails: TrackFieldEntryAthleteDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrackFieldEntryAthleteDetailsUpdateComponent,
    resolve: {
      trackFieldEntryAthleteDetails: TrackFieldEntryAthleteDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trackFieldEntryAthleteDetailsRoute)],
  exports: [RouterModule],
})
export class TrackFieldEntryAthleteDetailsRoutingModule {}
