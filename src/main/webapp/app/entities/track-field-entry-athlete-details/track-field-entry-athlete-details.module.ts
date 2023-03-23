import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrackFieldEntryAthleteDetailsComponent } from './list/track-field-entry-athlete-details.component';
import { TrackFieldEntryAthleteDetailsDetailComponent } from './detail/track-field-entry-athlete-details-detail.component';
import { TrackFieldEntryAthleteDetailsUpdateComponent } from './update/track-field-entry-athlete-details-update.component';
import { TrackFieldEntryAthleteDetailsDeleteDialogComponent } from './delete/track-field-entry-athlete-details-delete-dialog.component';
import { TrackFieldEntryAthleteDetailsRoutingModule } from './route/track-field-entry-athlete-details-routing.module';

@NgModule({
  imports: [SharedModule, TrackFieldEntryAthleteDetailsRoutingModule],
  declarations: [
    TrackFieldEntryAthleteDetailsComponent,
    TrackFieldEntryAthleteDetailsDetailComponent,
    TrackFieldEntryAthleteDetailsUpdateComponent,
    TrackFieldEntryAthleteDetailsDeleteDialogComponent,
  ],
})
export class TrackFieldEntryAthleteDetailsModule {}
