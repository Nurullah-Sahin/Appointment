import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppointmentCategory, AppointmentCategory } from '../appointment-category.model';
import { AppointmentCategoryService } from '../service/appointment-category.service';

@Injectable({ providedIn: 'root' })
export class AppointmentCategoryRoutingResolveService implements Resolve<IAppointmentCategory> {
  constructor(protected service: AppointmentCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppointmentCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((appointmentCategory: HttpResponse<AppointmentCategory>) => {
          if (appointmentCategory.body) {
            return of(appointmentCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AppointmentCategory());
  }
}
