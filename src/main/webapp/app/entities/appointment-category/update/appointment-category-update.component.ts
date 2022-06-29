import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAppointmentCategory, AppointmentCategory } from '../appointment-category.model';
import { AppointmentCategoryService } from '../service/appointment-category.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-appointment-category-update',
  templateUrl: './appointment-category-update.component.html',
})
export class AppointmentCategoryUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    categoryName: [],
    hosts: [],
    userId: [],
  });

  constructor(
    protected appointmentCategoryService: AppointmentCategoryService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointmentCategory }) => {
      this.updateForm(appointmentCategory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointmentCategory = this.createFromForm();
    if (appointmentCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.appointmentCategoryService.update(appointmentCategory));
    } else {
      this.subscribeToSaveResponse(this.appointmentCategoryService.create(appointmentCategory));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointmentCategory>>): void {
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

  protected updateForm(appointmentCategory: IAppointmentCategory): void {
    this.editForm.patchValue({
      id: appointmentCategory.id,
      categoryName: appointmentCategory.categoryName,
      hosts: appointmentCategory.hosts,
      userId: appointmentCategory.userId,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, appointmentCategory.userId);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('userId')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IAppointmentCategory {
    return {
      ...new AppointmentCategory(),
      id: this.editForm.get(['id'])!.value,
      categoryName: this.editForm.get(['categoryName'])!.value,
      hosts: this.editForm.get(['hosts'])!.value,
      userId: this.editForm.get(['userId'])!.value,
    };
  }
}
