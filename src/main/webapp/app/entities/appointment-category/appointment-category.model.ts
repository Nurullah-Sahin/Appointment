import { IUser } from 'app/entities/user/user.model';

export interface IAppointmentCategory {
  id?: string;
  categoryName?: string | null;
  hosts?: string | null;
  userId?: IUser | null;
}

export class AppointmentCategory implements IAppointmentCategory {
  constructor(public id?: string, public categoryName?: string | null, public hosts?: string | null, public userId?: IUser | null) {}
}

export function getAppointmentCategoryIdentifier(appointmentCategory: IAppointmentCategory): string | undefined {
  return appointmentCategory.id;
}
