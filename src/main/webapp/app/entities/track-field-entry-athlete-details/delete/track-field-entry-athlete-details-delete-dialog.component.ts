import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrackFieldEntryAthleteDetails } from '../track-field-entry-athlete-details.model';
import { TrackFieldEntryAthleteDetailsService } from '../service/track-field-entry-athlete-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './track-field-entry-athlete-details-delete-dialog.component.html',
})
export class TrackFieldEntryAthleteDetailsDeleteDialogComponent {
  trackFieldEntryAthleteDetails?: ITrackFieldEntryAthleteDetails;

  constructor(
    protected trackFieldEntryAthleteDetailsService: TrackFieldEntryAthleteDetailsService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trackFieldEntryAthleteDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
