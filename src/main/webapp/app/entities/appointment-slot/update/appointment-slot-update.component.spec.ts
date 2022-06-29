jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AppointmentSlotService } from '../service/appointment-slot.service';
import { IAppointmentSlot, AppointmentSlot } from '../appointment-slot.model';
import { IAppointmentDefinition } from 'app/entities/appointment-definition/appointment-definition.model';
import { AppointmentDefinitionService } from 'app/entities/appointment-definition/service/appointment-definition.service';

import { AppointmentSlotUpdateComponent } from './appointment-slot-update.component';

describe('Component Tests', () => {
  describe('AppointmentSlot Management Update Component', () => {
    let comp: AppointmentSlotUpdateComponent;
    let fixture: ComponentFixture<AppointmentSlotUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let appointmentSlotService: AppointmentSlotService;
    let appointmentDefinitionService: AppointmentDefinitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppointmentSlotUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AppointmentSlotUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppointmentSlotUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      appointmentSlotService = TestBed.inject(AppointmentSlotService);
      appointmentDefinitionService = TestBed.inject(AppointmentDefinitionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call definitionId query and add missing value', () => {
        const appointmentSlot: IAppointmentSlot = { id: 'CBA' };
        const definitionId: IAppointmentDefinition = { id: '1b724ea6-0d5e-4376-9715-32a62b06ce77' };
        appointmentSlot.definitionId = definitionId;

        const definitionIdCollection: IAppointmentDefinition[] = [{ id: 'b2614ed5-fdcb-46e0-b4a4-0d36dda2ce5e' }];
        jest.spyOn(appointmentDefinitionService, 'query').mockReturnValue(of(new HttpResponse({ body: definitionIdCollection })));
        const expectedCollection: IAppointmentDefinition[] = [definitionId, ...definitionIdCollection];
        jest.spyOn(appointmentDefinitionService, 'addAppointmentDefinitionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ appointmentSlot });
        comp.ngOnInit();

        expect(appointmentDefinitionService.query).toHaveBeenCalled();
        expect(appointmentDefinitionService.addAppointmentDefinitionToCollectionIfMissing).toHaveBeenCalledWith(
          definitionIdCollection,
          definitionId
        );
        expect(comp.definitionIdsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const appointmentSlot: IAppointmentSlot = { id: 'CBA' };
        const definitionId: IAppointmentDefinition = { id: 'f0fdc855-eff0-436f-8905-1fde782a5e36' };
        appointmentSlot.definitionId = definitionId;

        activatedRoute.data = of({ appointmentSlot });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(appointmentSlot));
        expect(comp.definitionIdsCollection).toContain(definitionId);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AppointmentSlot>>();
        const appointmentSlot = { id: 'ABC' };
        jest.spyOn(appointmentSlotService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentSlot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointmentSlot }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(appointmentSlotService.update).toHaveBeenCalledWith(appointmentSlot);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AppointmentSlot>>();
        const appointmentSlot = new AppointmentSlot();
        jest.spyOn(appointmentSlotService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentSlot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: appointmentSlot }));
        saveSubject.complete();

        // THEN
        expect(appointmentSlotService.create).toHaveBeenCalledWith(appointmentSlot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AppointmentSlot>>();
        const appointmentSlot = { id: 'ABC' };
        jest.spyOn(appointmentSlotService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ appointmentSlot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(appointmentSlotService.update).toHaveBeenCalledWith(appointmentSlot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAppointmentDefinitionById', () => {
        it('Should return tracked AppointmentDefinition primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackAppointmentDefinitionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
