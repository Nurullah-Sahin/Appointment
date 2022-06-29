import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAppointmentSlot, AppointmentSlot } from '../appointment-slot.model';
import { AppointmentSlotService } from '../service/appointment-slot.service';
import { IAppointmentDefinition } from 'app/entities/appointment-definition/appointment-definition.model';
import { AppointmentDefinitionService } from 'app/entities/appointment-definition/service/appointment-definition.service';

@Component({
  selector: 'jhi-appointment-slot-update',
  templateUrl: './appointment-slot-update.component.html',
})
export class AppointmentSlotUpdateComponent implements OnInit {
  isSaving = false;

  definitionIdsCollection: IAppointmentDefinition[] = [];

  editForm = this.fb.group({
    id: [],
    startTime: [],
    endTime: [],
    definitionId: [],
  });

  constructor(
    protected appointmentSlotService: AppointmentSlotService,
    protected appointmentDefinitionService: AppointmentDefinitionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointmentSlot }) => {
      if (appointmentSlot.id === undefined) {
        const today = dayjs().startOf('day');
        appointmentSlot.startTime = today;
        appointmentSlot.endTime = today;
      }

      this.updateForm(appointmentSlot);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointmentSlot = this.createFromForm();
    if (appointmentSlot.id !== undefined) {
      this.subscribeToSaveResponse(this.appointmentSlotService.update(appointmentSlot));
    } else {
      this.subscribeToSaveResponse(this.appointmentSlotService.create(appointmentSlot));
    }
  }

  trackAppointmentDefinitionById(index: number, item: IAppointmentDefinition): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointmentSlot>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(appointmentSlot: IAppointmentSlot): void {
    this.editForm.patchValue({
      id: appointmentSlot.id,
      startTime: appointmentSlot.startTime ? appointmentSlot.startTime.format(DATE_TIME_FORMAT) : null,
      endTime: appointmentSlot.endTime ? appointmentSlot.endTime.format(DATE_TIME_FORMAT) : null,
      definitionId: appointmentSlot.definitionId,
    });

    this.definitionIdsCollection = this.appointmentDefinitionService.addAppointmentDefinitionToCollectionIfMissing(
      this.definitionIdsCollection,
      appointmentSlot.definitionId
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appointmentDefinitionService
      .query({ filter: 'id-is-null' })
      .pipe(map((res: HttpResponse<IAppointmentDefinition[]>) => res.body ?? []))
      .pipe(
        map((appointmentDefinitions: IAppointmentDefinition[]) =>
          this.appointmentDefinitionService.addAppointmentDefinitionToCollectionIfMissing(
            appointmentDefinitions,
            this.editForm.get('definitionId')!.value
          )
        )
      )
      .subscribe((appointmentDefinitions: IAppointmentDefinition[]) => (this.definitionIdsCollection = appointmentDefinitions));
  }

  protected createFromForm(): IAppointmentSlot {
    return {
      ...new AppointmentSlot(),
      id: this.editForm.get(['id'])!.value,
      startTime: this.editForm.get(['startTime'])!.value ? dayjs(this.editForm.get(['startTime'])!.value, DATE_TIME_FORMAT) : undefined,
      endTime: this.editForm.get(['endTime'])!.value ? dayjs(this.editForm.get(['endTime'])!.value, DATE_TIME_FORMAT) : undefined,
      definitionId: this.editForm.get(['definitionId'])!.value,
    };
  }
}
