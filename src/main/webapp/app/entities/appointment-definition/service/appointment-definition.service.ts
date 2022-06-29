import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppointmentDefinition, getAppointmentDefinitionIdentifier } from '../appointment-definition.model';

export type EntityResponseType = HttpResponse<IAppointmentDefinition>;
export type EntityArrayResponseType = HttpResponse<IAppointmentDefinition[]>;

@Injectable({ providedIn: 'root' })
export class AppointmentDefinitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/appointment-definitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(appointmentDefinition: IAppointmentDefinition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointmentDefinition);
    return this.http
      .post<IAppointmentDefinition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appointmentDefinition: IAppointmentDefinition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointmentDefinition);
    return this.http
      .put<IAppointmentDefinition>(`${this.resourceUrl}/${getAppointmentDefinitionIdentifier(appointmentDefinition) as string}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(appointmentDefinition: IAppointmentDefinition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointmentDefinition);
    return this.http
      .patch<IAppointmentDefinition>(`${this.resourceUrl}/${getAppointmentDefinitionIdentifier(appointmentDefinition) as string}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IAppointmentDefinition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppointmentDefinition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAppointmentDefinitionToCollectionIfMissing(
    appointmentDefinitionCollection: IAppointmentDefinition[],
    ...appointmentDefinitionsToCheck: (IAppointmentDefinition | null | undefined)[]
  ): IAppointmentDefinition[] {
    const appointmentDefinitions: IAppointmentDefinition[] = appointmentDefinitionsToCheck.filter(isPresent);
    if (appointmentDefinitions.length > 0) {
      const appointmentDefinitionCollectionIdentifiers = appointmentDefinitionCollection.map(
        appointmentDefinitionItem => getAppointmentDefinitionIdentifier(appointmentDefinitionItem)!
      );
      const appointmentDefinitionsToAdd = appointmentDefinitions.filter(appointmentDefinitionItem => {
        const appointmentDefinitionIdentifier = getAppointmentDefinitionIdentifier(appointmentDefinitionItem);
        if (
          appointmentDefinitionIdentifier == null ||
          appointmentDefinitionCollectionIdentifiers.includes(appointmentDefinitionIdentifier)
        ) {
          return false;
        }
        appointmentDefinitionCollectionIdentifiers.push(appointmentDefinitionIdentifier);
        return true;
      });
      return [...appointmentDefinitionsToAdd, ...appointmentDefinitionCollection];
    }
    return appointmentDefinitionCollection;
  }

  protected convertDateFromClient(appointmentDefinition: IAppointmentDefinition): IAppointmentDefinition {
    return Object.assign({}, appointmentDefinition, {
      startTime: appointmentDefinition.startTime?.isValid() ? appointmentDefinition.startTime.format(DATE_FORMAT) : undefined,
      endTime: appointmentDefinition.endTime?.isValid() ? appointmentDefinition.endTime.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startTime = res.body.startTime ? dayjs(res.body.startTime) : undefined;
      res.body.endTime = res.body.endTime ? dayjs(res.body.endTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((appointmentDefinition: IAppointmentDefinition) => {
        appointmentDefinition.startTime = appointmentDefinition.startTime ? dayjs(appointmentDefinition.startTime) : undefined;
        appointmentDefinition.endTime = appointmentDefinition.endTime ? dayjs(appointmentDefinition.endTime) : undefined;
      });
    }
    return res;
  }
}
