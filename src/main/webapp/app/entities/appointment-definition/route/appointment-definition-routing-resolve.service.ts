import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppointmentDefinition, AppointmentDefinition } from '../appointment-definition.model';
import { AppointmentDefinitionService } from '../service/appointment-definition.service';

@Injectable({ providedIn: 'root' })
export class AppointmentDefinitionRoutingResolveService implements Resolve<IAppointmentDefinition> {
  constructor(protected service: AppointmentDefinitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppointmentDefinition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((appointmentDefinition: HttpResponse<AppointmentDefinition>) => {
          if (appointmentDefinition.body) {
            return of(appointmentDefinition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AppointmentDefinition());
  }
}
