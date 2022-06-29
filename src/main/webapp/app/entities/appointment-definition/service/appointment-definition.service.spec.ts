import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAppointmentDefinition, AppointmentDefinition } from '../appointment-definition.model';

import { AppointmentDefinitionService } from './appointment-definition.service';

describe('Service Tests', () => {
  describe('AppointmentDefinition Service', () => {
    let service: AppointmentDefinitionService;
    let httpMock: HttpTestingController;
    let elemDefault: IAppointmentDefinition;
    let expectedResult: IAppointmentDefinition | IAppointmentDefinition[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AppointmentDefinitionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 'AAAAAAA',
        startTime: currentDate,
        endTime: currentDate,
        name: 'AAAAAAA',
        allowRescheduleNoSoonerThan: 0,
        allowRescheduleNoLaterThan: 0,
        allowScheduleNoSoonerThan: 0,
        allowScheduleNoLaterThan: 0,
        numberOfReschedule: 0,
        duration: 'PT1S',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startTime: currentDate.format(DATE_FORMAT),
            endTime: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find('ABC').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AppointmentDefinition', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            startTime: currentDate.format(DATE_FORMAT),
            endTime: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startTime: currentDate,
            endTime: currentDate,
          },
          returnedFromService
        );

        service.create(new AppointmentDefinition()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AppointmentDefinition', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            startTime: currentDate.format(DATE_FORMAT),
            endTime: currentDate.format(DATE_FORMAT),
            name: 'BBBBBB',
            allowRescheduleNoSoonerThan: 1,
            allowRescheduleNoLaterThan: 1,
            allowScheduleNoSoonerThan: 1,
            allowScheduleNoLaterThan: 1,
            numberOfReschedule: 1,
            duration: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startTime: currentDate,
            endTime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AppointmentDefinition', () => {
        const patchObject = Object.assign(
          {
            endTime: currentDate.format(DATE_FORMAT),
            name: 'BBBBBB',
            allowRescheduleNoSoonerThan: 1,
            allowScheduleNoSoonerThan: 1,
            duration: 'BBBBBB',
          },
          new AppointmentDefinition()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startTime: currentDate,
            endTime: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AppointmentDefinition', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            startTime: currentDate.format(DATE_FORMAT),
            endTime: currentDate.format(DATE_FORMAT),
            name: 'BBBBBB',
            allowRescheduleNoSoonerThan: 1,
            allowRescheduleNoLaterThan: 1,
            allowScheduleNoSoonerThan: 1,
            allowScheduleNoLaterThan: 1,
            numberOfReschedule: 1,
            duration: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startTime: currentDate,
            endTime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a AppointmentDefinition', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAppointmentDefinitionToCollectionIfMissing', () => {
        it('should add a AppointmentDefinition to an empty array', () => {
          const appointmentDefinition: IAppointmentDefinition = { id: 'ABC' };
          expectedResult = service.addAppointmentDefinitionToCollectionIfMissing([], appointmentDefinition);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appointmentDefinition);
        });

        it('should not add a AppointmentDefinition to an array that contains it', () => {
          const appointmentDefinition: IAppointmentDefinition = { id: 'ABC' };
          const appointmentDefinitionCollection: IAppointmentDefinition[] = [
            {
              ...appointmentDefinition,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addAppointmentDefinitionToCollectionIfMissing(appointmentDefinitionCollection, appointmentDefinition);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AppointmentDefinition to an array that doesn't contain it", () => {
          const appointmentDefinition: IAppointmentDefinition = { id: 'ABC' };
          const appointmentDefinitionCollection: IAppointmentDefinition[] = [{ id: 'CBA' }];
          expectedResult = service.addAppointmentDefinitionToCollectionIfMissing(appointmentDefinitionCollection, appointmentDefinition);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appointmentDefinition);
        });

        it('should add only unique AppointmentDefinition to an array', () => {
          const appointmentDefinitionArray: IAppointmentDefinition[] = [
            { id: 'ABC' },
            { id: 'CBA' },
            { id: '7df7cdf5-41ab-4f2a-9a3a-9152ac19237e' },
          ];
          const appointmentDefinitionCollection: IAppointmentDefinition[] = [{ id: 'ABC' }];
          expectedResult = service.addAppointmentDefinitionToCollectionIfMissing(
            appointmentDefinitionCollection,
            ...appointmentDefinitionArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const appointmentDefinition: IAppointmentDefinition = { id: 'ABC' };
          const appointmentDefinition2: IAppointmentDefinition = { id: 'CBA' };
          expectedResult = service.addAppointmentDefinitionToCollectionIfMissing([], appointmentDefinition, appointmentDefinition2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appointmentDefinition);
          expect(expectedResult).toContain(appointmentDefinition2);
        });

        it('should accept null and undefined values', () => {
          const appointmentDefinition: IAppointmentDefinition = { id: 'ABC' };
          expectedResult = service.addAppointmentDefinitionToCollectionIfMissing([], null, appointmentDefinition, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appointmentDefinition);
        });

        it('should return initial array if no AppointmentDefinition is added', () => {
          const appointmentDefinitionCollection: IAppointmentDefinition[] = [{ id: 'ABC' }];
          expectedResult = service.addAppointmentDefinitionToCollectionIfMissing(appointmentDefinitionCollection, undefined, null);
          expect(expectedResult).toEqual(appointmentDefinitionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
