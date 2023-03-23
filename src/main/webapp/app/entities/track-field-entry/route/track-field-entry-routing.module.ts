import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrackFieldEntryComponent } from '../list/track-field-entry.component';
import { TrackFieldEntryDetailComponent } from '../detail/track-field-entry-detail.component';
import { TrackFieldEntryUpdateComponent } from '../update/track-field-entry-update.component';
import { TrackFieldEntryRoutingResolveService } from './track-field-entry-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const trackFieldEntryRoute: Routes = [
  {
    path: '',
    component: TrackFieldEntryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrackFieldEntryDetailComponent,
    resolve: {
      trackFieldEntry: TrackFieldEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrackFieldEntryUpdateComponent,
    resolve: {
      trackFieldEntry: TrackFieldEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrackFieldEntryUpdateComponent,
    resolve: {
      trackFieldEntry: TrackFieldEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trackFieldEntryRoute)],
  exports: [RouterModule],
})
export class TrackFieldEntryRoutingModule {}
