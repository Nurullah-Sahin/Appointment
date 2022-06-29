import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppointmentDefinitionComponent } from '../list/appointment-definition.component';
import { AppointmentDefinitionDetailComponent } from '../detail/appointment-definition-detail.component';
import { AppointmentDefinitionUpdateComponent } from '../update/appointment-definition-update.component';
import { AppointmentDefinitionRoutingResolveService } from './appointment-definition-routing-resolve.service';

const appointmentDefinitionRoute: Routes = [
  {
    path: '',
    component: AppointmentDefinitionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppointmentDefinitionDetailComponent,
    resolve: {
      appointmentDefinition: AppointmentDefinitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppointmentDefinitionUpdateComponent,
    resolve: {
      appointmentDefinition: AppointmentDefinitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppointmentDefinitionUpdateComponent,
    resolve: {
      appointmentDefinition: AppointmentDefinitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appointmentDefinitionRoute)],
  exports: [RouterModule],
})
export class AppointmentDefinitionRoutingModule {}
