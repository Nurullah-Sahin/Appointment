import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAppointmentDefinition, AppointmentDefinition } from '../appointment-definition.model';
import { AppointmentDefinitionService } from '../service/appointment-definition.service';
import { IAppointmentCategory } from 'app/entities/appointment-category/appointment-category.model';
import { AppointmentCategoryService } from 'app/entities/appointment-category/service/appointment-category.service';

@Component({
  selector: 'jhi-appointment-definition-update',
  templateUrl: './appointment-definition-update.component.html',
})
export class AppointmentDefinitionUpdateComponent implements OnInit {
  isSaving = false;

  categoryNamesCollection: IAppointmentCategory[] = [];
  categoryIdsCollection: IAppointmentCategory[] = [];

  editForm = this.fb.group({
    id: [],
    startTime: [],
    endTime: [],
    name: [],
    allowRescheduleNoSoonerThan: [],
    allowRescheduleNoLaterThan: [],
    allowScheduleNoSoonerThan: [],
    allowScheduleNoLaterThan: [],
    numberOfReschedule: [],
    duration: [],
    categoryName: [],
    categoryId: [],
  });

  constructor(
    protected appointmentDefinitionService: AppointmentDefinitionService,
    protected appointmentCategoryService: AppointmentCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointmentDefinition }) => {
      this.updateForm(appointmentDefinition);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointmentDefinition = this.createFromForm();
    if (appointmentDefinition.id !== undefined) {
      this.subscribeToSaveResponse(this.appointmentDefinitionService.update(appointmentDefinition));
    } else {
      this.subscribeToSaveResponse(this.appointmentDefinitionService.create(appointmentDefinition));
    }
  }

  trackAppointmentCategoryById(index: number, item: IAppointmentCategory): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointmentDefinition>>): void {
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

  protected updateForm(appointmentDefinition: IAppointmentDefinition): void {
    this.editForm.patchValue({
      id: appointmentDefinition.id,
      startTime: appointmentDefinition.startTime,
      endTime: appointmentDefinition.endTime,
      name: appointmentDefinition.name,
      allowRescheduleNoSoonerThan: appointmentDefinition.allowRescheduleNoSoonerThan,
      allowRescheduleNoLaterThan: appointmentDefinition.allowRescheduleNoLaterThan,
      allowScheduleNoSoonerThan: appointmentDefinition.allowScheduleNoSoonerThan,
      allowScheduleNoLaterThan: appointmentDefinition.allowScheduleNoLaterThan,
      numberOfReschedule: appointmentDefinition.numberOfReschedule,
      duration: appointmentDefinition.duration,
      categoryName: appointmentDefinition.categoryName,
      categoryId: appointmentDefinition.categoryId,
    });

    this.categoryNamesCollection = this.appointmentCategoryService.addAppointmentCategoryToCollectionIfMissing(
      this.categoryNamesCollection,
      appointmentDefinition.categoryName
    );
    this.categoryIdsCollection = this.appointmentCategoryService.addAppointmentCategoryToCollectionIfMissing(
      this.categoryIdsCollection,
      appointmentDefinition.categoryId
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appointmentCategoryService
      .query({ filter: 'categoryname-is-null' })
      .pipe(map((res: HttpResponse<IAppointmentCategory[]>) => res.body ?? []))
      .pipe(
        map((appointmentCategories: IAppointmentCategory[]) =>
          this.appointmentCategoryService.addAppointmentCategoryToCollectionIfMissing(
            appointmentCategories,
            this.editForm.get('categoryName')!.value
          )
        )
      )
      .subscribe((appointmentCategories: IAppointmentCategory[]) => (this.categoryNamesCollection = appointmentCategories));

    this.appointmentCategoryService
      .query({ filter: 'id-is-null' })
      .pipe(map((res: HttpResponse<IAppointmentCategory[]>) => res.body ?? []))
      .pipe(
        map((appointmentCategories: IAppointmentCategory[]) =>
          this.appointmentCategoryService.addAppointmentCategoryToCollectionIfMissing(
            appointmentCategories,
            this.editForm.get('categoryId')!.value
          )
        )
      )
      .subscribe((appointmentCategories: IAppointmentCategory[]) => (this.categoryIdsCollection = appointmentCategories));
  }

  protected createFromForm(): IAppointmentDefinition {
    return {
      ...new AppointmentDefinition(),
      id: this.editForm.get(['id'])!.value,
      startTime: this.editForm.get(['startTime'])!.value,
      endTime: this.editForm.get(['endTime'])!.value,
      name: this.editForm.get(['name'])!.value,
      allowRescheduleNoSoonerThan: this.editForm.get(['allowRescheduleNoSoonerThan'])!.value,
      allowRescheduleNoLaterThan: this.editForm.get(['allowRescheduleNoLaterThan'])!.value,
      allowScheduleNoSoonerThan: this.editForm.get(['allowScheduleNoSoonerThan'])!.value,
      allowScheduleNoLaterThan: this.editForm.get(['allowScheduleNoLaterThan'])!.value,
      numberOfReschedule: this.editForm.get(['numberOfReschedule'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      categoryName: this.editForm.get(['categoryName'])!.value,
      categoryId: this.editForm.get(['categoryId'])!.value,
    };
  }
}
