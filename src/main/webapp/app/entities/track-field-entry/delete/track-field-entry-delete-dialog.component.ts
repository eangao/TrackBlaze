import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrackFieldEntry } from '../track-field-entry.model';
import { TrackFieldEntryService } from '../service/track-field-entry.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './track-field-entry-delete-dialog.component.html',
})
export class TrackFieldEntryDeleteDialogComponent {
  trackFieldEntry?: ITrackFieldEntry;

  constructor(protected trackFieldEntryService: TrackFieldEntryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trackFieldEntryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
