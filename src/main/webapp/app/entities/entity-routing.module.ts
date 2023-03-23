import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'athlete',
        data: { pageTitle: 'trackBlazeApp.athlete.home.title' },
        loadChildren: () => import('./athlete/athlete.module').then(m => m.AthleteModule),
      },
      {
        path: 'track-field-entry',
        data: { pageTitle: 'trackBlazeApp.trackFieldEntry.home.title' },
        loadChildren: () => import('./track-field-entry/track-field-entry.module').then(m => m.TrackFieldEntryModule),
      },
      {
        path: 'track-field-entry-athlete-details',
        data: { pageTitle: 'trackBlazeApp.trackFieldEntryAthleteDetails.home.title' },
        loadChildren: () =>
          import('./track-field-entry-athlete-details/track-field-entry-athlete-details.module').then(
            m => m.TrackFieldEntryAthleteDetailsModule
          ),
      },
      {
        path: 'event',
        data: { pageTitle: 'trackBlazeApp.event.home.title' },
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      {
        path: 'school',
        data: { pageTitle: 'trackBlazeApp.school.home.title' },
        loadChildren: () => import('./school/school.module').then(m => m.SchoolModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
