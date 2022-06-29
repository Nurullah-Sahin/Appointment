jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AppointmentDefinitionService } from '../service/appointment-definition.service';
import { IAppointmentDefinition, AppointmentDefinition } from '../appointment-definition.model';
import { IAppointmentCategory } from 'app/entities/appointment-category/appointment-category.model';
import { AppointmentCategoryService } from 'app/entities/appointment-category/service/appointment-category.service';

import { AppointmentDefinitionUpdateComponent } from './appointment-definition-update.component';

describe('Component Tests', () => {
  describe('AppointmentDefinition Management Update Component', () => {
    let comp: AppointmentDefinitionUpdateComponent;
    let fixture: ComponentFixture<AppointmentDefinitionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let appointmentDefinitionService: AppointmentDefinitionService;
    let appointmentCategoryService: AppointmentCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppointmentDefinitionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AppointmentDefinitionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppointmentDefinitionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      appointmentDefinitionService = TestBed.inject(AppointmentDefinitionService);
      appointmentCategoryService = TestBed.inject(AppointmentCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call categoryName query and add missing value', () => {
        const appointmentDefinition: IAppointmentDefinition = { id: 'CBA' };
        const categoryName: IAppointmentCategory = { id: '01bcc3f6-a7ce-485b-93e6-6887d95c5be7' };
        appointmentDefinition.categoryName = categoryName;

        const categoryNameCollection: IAppointmentCategory[] = [{ id: '75e7c2b5-9334-47bc-83e7-7d817e2e3605' }];
        jest.spyOn(appointmentCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryNameCollection })));
        const expectedCollection: IAppointmentCategory[] = [categoryName, ...categoryNameCollection];
        jest.spyOn(appointmentCategoryService, 'addAppointmentCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ appointmentDefinition });
        comp.ngOnInit();

        expect(appointmentCategoryService.query).toHaveBeenCalled();
        expect(appointmentCategoryService.addAppointmentCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          categoryNameCollection,
          categoryName
        );
        expect(comp.categoryNamesCollection).toEqual(expectedCollection);
      });

      it('Should call categoryId query and add missing value', () => {
        const appointmentDefinition: IAppointmentDefinition = { id: 'CBA' };
        const categoryId: IAppointmentCategory = { id: 'cc6ea303-7bda-46d6-bf09-22d663eeba4f' };
        appointmentDefinition.categoryId = categoryId;

        const categoryIdCollection: IAppointmentCategory[] = [{ id: '07c329d4-5fff-487f-b687-42a679ff0ea2' }];
        jest.spyOn(appointmentCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryIdCollection })));
        const expectedCollection: IAppointmentCategory[] = [categoryId, ...categoryIdCollection];
        jest.spyOn(appointmentCategoryService, 'addAppointmentCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ appointmentDefinition });
        comp.ngOnInit();

        expect(appointmentCategoryService.query).toHaveBeenCalled();
        expect(appointmentCategoryService.addAppointmentCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          categoryIdCollection,
          categoryId
        );
        expect(comp.categoryIdsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const appointmentDefinition: IAppointmentDefinition = { id: 'CBA' };
        const categoryName: IAppointmentCategory = { id: 'fa6cd0ef-1696-4771-b3e6-75ba2cca0885' };
        appointmentDefinition.categoryName = categoryName;
        const categoryId: IAppointmentCategory = { id: '1a4c0398-151c-4b02-85e4-b4989b8d6086' };
        appointmentDefinition.categoryId = categoryId;

        activatedRoute.data = of({ appointmentDefinition });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(appointmentDefinition));
        expect(comp.categoryNamesCollection).toContain(categoryName);
        expect(comp.categoryIdsCollection).toContain(categoryId);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AppointmentDefinition>>();
        const appointmentDefinition = { id: 'ABC' };
        jest.spyOn(appointmentDefinitionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentDefinition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointmentDefinition }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(appointmentDefinitionService.update).toHaveBeenCalledWith(appointmentDefinition);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AppointmentDefinition>>();
        const appointmentDefinition = new AppointmentDefinition();
        jest.spyOn(appointmentDefinitionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentDefinition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointmentDefinition }));
        saveSubject.complete();

        // THEN
        expect(appointmentDefinitionService.create).toHaveBeenCalledWith(appointmentDefinition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AppointmentDefinition>>();
        const appointmentDefinition = { id: 'ABC' };
        jest.spyOn(appointmentDefinitionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentDefinition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(appointmentDefinitionService.update).toHaveBeenCalledWith(appointmentDefinition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAppointmentCategoryById', () => {
        it('Should return tracked AppointmentCategory primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackAppointmentCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
