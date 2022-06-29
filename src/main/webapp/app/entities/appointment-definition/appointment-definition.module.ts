import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AppointmentDefinitionComponent } from './list/appointment-definition.component';
import { AppointmentDefinitionDetailComponent } from './detail/appointment-definition-detail.component';
import { AppointmentDefinitionUpdateComponent } from './update/appointment-definition-update.component';
import { AppointmentDefinitionDeleteDialogComponent } from './delete/appointment-definition-delete-dialog.component';
import { AppointmentDefinitionRoutingModule } from './route/appointment-definition-routing.module';

@NgModule({
  imports: [SharedModule, AppointmentDefinitionRoutingModule],
  declarations: [
    AppointmentDefinitionComponent,
    AppointmentDefinitionDetailComponent,
    AppointmentDefinitionUpdateComponent,
    AppointmentDefinitionDeleteDialogComponent,
  ],
  entryComponents: [AppointmentDefinitionDeleteDialogComponent],
})
export class AppointmentDefinitionModule {}
