import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppointmentCategory, getAppointmentCategoryIdentifier } from '../appointment-category.model';

export type EntityResponseType = HttpResponse<IAppointmentCategory>;
export type EntityArrayResponseType = HttpResponse<IAppointmentCategory[]>;

@Injectable({ providedIn: 'root' })
export class AppointmentCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/appointment-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(appointmentCategory: IAppointmentCategory): Observable<EntityResponseType> {
    return this.http.post<IAppointmentCategory>(this.resourceUrl, appointmentCategory, { observe: 'response' });
  }

  update(appointmentCategory: IAppointmentCategory): Observable<EntityResponseType> {
    return this.http.put<IAppointmentCategory>(
      `${this.resourceUrl}/${getAppointmentCategoryIdentifier(appointmentCategory) as string}`,
      appointmentCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(appointmentCategory: IAppointmentCategory): Observable<EntityResponseType> {
    return this.http.patch<IAppointmentCategory>(
      `${this.resourceUrl}/${getAppointmentCategoryIdentifier(appointmentCategory) as string}`,
      appointmentCategory,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAppointmentCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppointmentCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAppointmentCategoryToCollectionIfMissing(
    appointmentCategoryCollection: IAppointmentCategory[],
    ...appointmentCategoriesToCheck: (IAppointmentCategory | null | undefined)[]
  ): IAppointmentCategory[] {
    const appointmentCategories: IAppointmentCategory[] = appointmentCategoriesToCheck.filter(isPresent);
    if (appointmentCategories.length > 0) {
      const appointmentCategoryCollectionIdentifiers = appointmentCategoryCollection.map(
        appointmentCategoryItem => getAppointmentCategoryIdentifier(appointmentCategoryItem)!
      );
      const appointmentCategoriesToAdd = appointmentCategories.filter(appointmentCategoryItem => {
        const appointmentCategoryIdentifier = getAppointmentCategoryIdentifier(appointmentCategoryItem);
        if (appointmentCategoryIdentifier == null || appointmentCategoryCollectionIdentifiers.includes(appointmentCategoryIdentifier)) {
          return false;
        }
        appointmentCategoryCollectionIdentifiers.push(appointmentCategoryIdentifier);
        return true;
      });
      return [...appointmentCategoriesToAdd, ...appointmentCategoryCollection];
    }
    return appointmentCategoryCollection;
  }
}
