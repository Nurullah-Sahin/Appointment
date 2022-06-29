import * as dayjs from 'dayjs';
import { IAppointmentDefinition } from 'app/entities/appointment-definition/appointment-definition.model';

export interface IAppointmentSlot {
  id?: string;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  definitionId?: IAppointmentDefinition | null;
}

export class AppointmentSlot implements IAppointmentSlot {
  constructor(
    public id?: string,
    public startTime?: dayjs.Dayjs | null,
    public endTime?: dayjs.Dayjs | null,
    public definitionId?: IAppointmentDefinition | null
  ) {}
}

export function getAppointmentSlotIdentifier(appointmentSlot: IAppointmentSlot): string | undefined {
  return appointmentSlot.id;
}
