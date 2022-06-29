import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAppointmentCategory, AppointmentCategory } from '../appointment-category.model';

import { AppointmentCategoryService } from './appointment-category.service';

describe('Service Tests', () => {
  describe('AppointmentCategory Service', () => {
    let service: AppointmentCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IAppointmentCategory;
    let expectedResult: IAppointmentCategory | IAppointmentCategory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AppointmentCategoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        categoryName: 'AAAAAAA',
        hosts: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find('ABC').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AppointmentCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AppointmentCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AppointmentCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            categoryName: 'BBBBBB',
            hosts: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AppointmentCategory', () => {
        const patchObject = Object.assign(
          {
            categoryName: 'BBBBBB',
          },
          new AppointmentCategory()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AppointmentCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            categoryName: 'BBBBBB',
            hosts: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a AppointmentCategory', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAppointmentCategoryToCollectionIfMissing', () => {
        it('should add a AppointmentCategory to an empty array', () => {
          const appointmentCategory: IAppointmentCategory = { id: 'ABC' };
          expectedResult = service.addAppointmentCategoryToCollectionIfMissing([], appointmentCategory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appointmentCategory);
        });

        it('should not add a AppointmentCategory to an array that contains it', () => {
          const appointmentCategory: IAppointmentCategory = { id: 'ABC' };
          const appointmentCategoryCollection: IAppointmentCategory[] = [
            {
              ...appointmentCategory,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addAppointmentCategoryToCollectionIfMissing(appointmentCategoryCollection, appointmentCategory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AppointmentCategory to an array that doesn't contain it", () => {
          const appointmentCategory: IAppointmentCategory = { id: 'ABC' };
          const appointmentCategoryCollection: IAppointmentCategory[] = [{ id: 'CBA' }];
          expectedResult = service.addAppointmentCategoryToCollectionIfMissing(appointmentCategoryCollection, appointmentCategory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appointmentCategory);
        });

        it('should add only unique AppointmentCategory to an array', () => {
          const appointmentCategoryArray: IAppointmentCategory[] = [
            { id: 'ABC' },
            { id: 'CBA' },
            { id: '096371bb-88e5-495f-9ad6-b8c2134a7b46' },
          ];
          const appointmentCategoryCollection: IAppointmentCategory[] = [{ id: 'ABC' }];
          expectedResult = service.addAppointmentCategoryToCollectionIfMissing(appointmentCategoryCollection, ...appointmentCategoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const appointmentCategory: IAppointmentCategory = { id: 'ABC' };
          const appointmentCategory2: IAppointmentCategory = { id: 'CBA' };
          expectedResult = service.addAppointmentCategoryToCollectionIfMissing([], appointmentCategory, appointmentCategory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appointmentCategory);
          expect(expectedResult).toContain(appointmentCategory2);
        });

        it('should accept null and undefined values', () => {
          const appointmentCategory: IAppointmentCategory = { id: 'ABC' };
          expectedResult = service.addAppointmentCategoryToCollectionIfMissing([], null, appointmentCategory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appointmentCategory);
        });

        it('should return initial array if no AppointmentCategory is added', () => {
          const appointmentCategoryCollection: IAppointmentCategory[] = [{ id: 'ABC' }];
          expectedResult = service.addAppointmentCategoryToCollectionIfMissing(appointmentCategoryCollection, undefined, null);
          expect(expectedResult).toEqual(appointmentCategoryCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
