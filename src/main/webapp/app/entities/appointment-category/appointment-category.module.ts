import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AppointmentCategoryComponent } from './list/appointment-category.component';
import { AppointmentCategoryDetailComponent } from './detail/appointment-category-detail.component';
import { AppointmentCategoryUpdateComponent } from './update/appointment-category-update.component';
import { AppointmentCategoryDeleteDialogComponent } from './delete/appointment-category-delete-dialog.component';
import { AppointmentCategoryRoutingModule } from './route/appointment-category-routing.module';

@NgModule({
  imports: [SharedModule, AppointmentCategoryRoutingModule],
  declarations: [
    AppointmentCategoryComponent,
    AppointmentCategoryDetailComponent,
    AppointmentCategoryUpdateComponent,
    AppointmentCategoryDeleteDialogComponent,
  ],
  entryComponents: [AppointmentCategoryDeleteDialogComponent],
})
export class AppointmentCategoryModule {}
