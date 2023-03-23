import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrackFieldEntryComponent } from './list/track-field-entry.component';
import { TrackFieldEntryDetailComponent } from './detail/track-field-entry-detail.component';
import { TrackFieldEntryUpdateComponent } from './update/track-field-entry-update.component';
import { TrackFieldEntryDeleteDialogComponent } from './delete/track-field-entry-delete-dialog.component';
import { TrackFieldEntryRoutingModule } from './route/track-field-entry-routing.module';

@NgModule({
  imports: [SharedModule, TrackFieldEntryRoutingModule],
  declarations: [
    TrackFieldEntryComponent,
    TrackFieldEntryDetailComponent,
    TrackFieldEntryUpdateComponent,
    TrackFieldEntryDeleteDialogComponent,
  ],
})
export class TrackFieldEntryModule {}
