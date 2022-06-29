import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppointmentSlot, getAppointmentSlotIdentifier } from '../appointment-slot.model';

export type EntityResponseType = HttpResponse<IAppointmentSlot>;
export type EntityArrayResponseType = HttpResponse<IAppointmentSlot[]>;

@Injectable({ providedIn: 'root' })
export class AppointmentSlotService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/appointment-slots');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(appointmentSlot: IAppointmentSlot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointmentSlot);
    return this.http
      .post<IAppointmentSlot>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appointmentSlot: IAppointmentSlot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointmentSlot);
    return this.http
      .put<IAppointmentSlot>(`${this.resourceUrl}/${getAppointmentSlotIdentifier(appointmentSlot) as string}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(appointmentSlot: IAppointmentSlot): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointmentSlot);
    return this.http
      .patch<IAppointmentSlot>(`${this.resourceUrl}/${getAppointmentSlotIdentifier(appointmentSlot) as string}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IAppointmentSlot>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppointmentSlot[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAppointmentSlotToCollectionIfMissing(
    appointmentSlotCollection: IAppointmentSlot[],
    ...appointmentSlotsToCheck: (IAppointmentSlot | null | undefined)[]
  ): IAppointmentSlot[] {
    const appointmentSlots: IAppointmentSlot[] = appointmentSlotsToCheck.filter(isPresent);
    if (appointmentSlots.length > 0) {
      const appointmentSlotCollectionIdentifiers = appointmentSlotCollection.map(
        appointmentSlotItem => getAppointmentSlotIdentifier(appointmentSlotItem)!
      );
      const appointmentSlotsToAdd = appointmentSlots.filter(appointmentSlotItem => {
        const appointmentSlotIdentifier = getAppointmentSlotIdentifier(appointmentSlotItem);
        if (appointmentSlotIdentifier == null || appointmentSlotCollectionIdentifiers.includes(appointmentSlotIdentifier)) {
          return false;
        }
        appointmentSlotCollectionIdentifiers.push(appointmentSlotIdentifier);
        return true;
      });
      return [...appointmentSlotsToAdd, ...appointmentSlotCollection];
    }
    return appointmentSlotCollection;
  }

  protected convertDateFromClient(appointmentSlot: IAppointmentSlot): IAppointmentSlot {
    return Object.assign({}, appointmentSlot, {
      startTime: appointmentSlot.startTime?.isValid() ? appointmentSlot.startTime.toJSON() : undefined,
      endTime: appointmentSlot.endTime?.isValid() ? appointmentSlot.endTime.toJSON() : undefined,
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
      res.body.forEach((appointmentSlot: IAppointmentSlot) => {
        appointmentSlot.startTime = appointmentSlot.startTime ? dayjs(appointmentSlot.startTime) : undefined;
        appointmentSlot.endTime = appointmentSlot.endTime ? dayjs(appointmentSlot.endTime) : undefined;
      });
    }
    return res;
  }
}
