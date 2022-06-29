import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppointmentCategoryComponent } from '../list/appointment-category.component';
import { AppointmentCategoryDetailComponent } from '../detail/appointment-category-detail.component';
import { AppointmentCategoryUpdateComponent } from '../update/appointment-category-update.component';
import { AppointmentCategoryRoutingResolveService } from './appointment-category-routing-resolve.service';

const appointmentCategoryRoute: Routes = [
  {
    path: '',
    component: AppointmentCategoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppointmentCategoryDetailComponent,
    resolve: {
      appointmentCategory: AppointmentCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppointmentCategoryUpdateComponent,
    resolve: {
      appointmentCategory: AppointmentCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppointmentCategoryUpdateComponent,
    resolve: {
      appointmentCategory: AppointmentCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appointmentCategoryRoute)],
  exports: [RouterModule],
})
export class AppointmentCategoryRoutingModule {}
