import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'appointment-category',
        data: { pageTitle: 'onlineAppointmentApp.appointmentCategory.home.title' },
        loadChildren: () => import('./appointment-category/appointment-category.module').then(m => m.AppointmentCategoryModule),
      },
      {
        path: 'appointment-definition',
        data: { pageTitle: 'onlineAppointmentApp.appointmentDefinition.home.title' },
        loadChildren: () => import('./appointment-definition/appointment-definition.module').then(m => m.AppointmentDefinitionModule),
      },
      {
        path: 'appointment-slot',
        data: { pageTitle: 'onlineAppointmentApp.appointmentSlot.home.title' },
        loadChildren: () => import('./appointment-slot/appointment-slot.module').then(m => m.AppointmentSlotModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
