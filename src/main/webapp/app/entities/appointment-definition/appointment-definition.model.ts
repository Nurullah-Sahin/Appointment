import * as dayjs from 'dayjs';
import { IAppointmentCategory } from 'app/entities/appointment-category/appointment-category.model';

export interface IAppointmentDefinition {
  id?: string;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  name?: string | null;
  allowRescheduleNoSoonerThan?: number | null;
  allowRescheduleNoLaterThan?: number | null;
  allowScheduleNoSoonerThan?: number | null;
  allowScheduleNoLaterThan?: number | null;
  numberOfReschedule?: number | null;
  duration?: string | null;
  categoryName?: IAppointmentCategory | null;
  categoryId?: IAppointmentCategory | null;
}

export class AppointmentDefinition implements IAppointmentDefinition {
  constructor(
    public id?: string,
    public startTime?: dayjs.Dayjs | null,
    public endTime?: dayjs.Dayjs | null,
    public name?: string | null,
    public allowRescheduleNoSoonerThan?: number | null,
    public allowRescheduleNoLaterThan?: number | null,
    public allowScheduleNoSoonerThan?: number | null,
    public allowScheduleNoLaterThan?: number | null,
    public numberOfReschedule?: number | null,
    public duration?: string | null,
    public categoryName?: IAppointmentCategory | null,
    public categoryId?: IAppointmentCategory | null
  ) {}
}

export function getAppointmentDefinitionIdentifier(appointmentDefinition: IAppointmentDefinition): string | undefined {
  return appointmentDefinition.id;
}
