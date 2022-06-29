jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AppointmentCategoryService } from '../service/appointment-category.service';
import { IAppointmentCategory, AppointmentCategory } from '../appointment-category.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { AppointmentCategoryUpdateComponent } from './appointment-category-update.component';

describe('Component Tests', () => {
  describe('AppointmentCategory Management Update Component', () => {
    let comp: AppointmentCategoryUpdateComponent;
    let fixture: ComponentFixture<AppointmentCategoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let appointmentCategoryService: AppointmentCategoryService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppointmentCategoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AppointmentCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppointmentCategoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      appointmentCategoryService = TestBed.inject(AppointmentCategoryService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const appointmentCategory: IAppointmentCategory = { id: 'CBA' };
        const userId: IUser = { id: '56a5f7d1-c896-46d6-babd-676dc80c9814' };
        appointmentCategory.userId = userId;

        const userCollection: IUser[] = [{ id: '30544472-e831-4476-916a-c2441de1759d' }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [userId];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ appointmentCategory });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const appointmentCategory: IAppointmentCategory = { id: 'CBA' };
        const userId: IUser = { id: 'da32cd8e-397b-4c73-92e6-e8d0dcd13a8f' };
        appointmentCategory.userId = userId;

        activatedRoute.data = of({ appointmentCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(appointmentCategory));
        expect(comp.usersSharedCollection).toContain(userId);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AppointmentCategory>>();
        const appointmentCategory = { id: 'ABC' };
        jest.spyOn(appointmentCategoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointmentCategory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(appointmentCategoryService.update).toHaveBeenCalledWith(appointmentCategory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AppointmentCategory>>();
        const appointmentCategory = new AppointmentCategory();
        jest.spyOn(appointmentCategoryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointmentCategory }));
        saveSubject.complete();

        // THEN
        expect(appointmentCategoryService.create).toHaveBeenCalledWith(appointmentCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AppointmentCategory>>();
        const appointmentCategory = { id: 'ABC' };
        jest.spyOn(appointmentCategoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(appointmentCategoryService.update).toHaveBeenCalledWith(appointmentCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
