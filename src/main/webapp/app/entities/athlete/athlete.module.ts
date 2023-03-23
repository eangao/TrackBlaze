import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AthleteComponent } from './list/athlete.component';
import { AthleteDetailComponent } from './detail/athlete-detail.component';
import { AthleteUpdateComponent } from './update/athlete-update.component';
import { AthleteDeleteDialogComponent } from './delete/athlete-delete-dialog.component';
import { AthleteRoutingModule } from './route/athlete-routing.module';

@NgModule({
  imports: [SharedModule, AthleteRoutingModule],
  declarations: [AthleteComponent, AthleteDetailComponent, AthleteUpdateComponent, AthleteDeleteDialogComponent],
})
export class AthleteModule {}
