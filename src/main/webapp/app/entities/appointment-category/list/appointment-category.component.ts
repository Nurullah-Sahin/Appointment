import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppointmentCategory } from '../appointment-category.model';
import { AppointmentCategoryService } from '../service/appointment-category.service';
import { AppointmentCategoryDeleteDialogComponent } from '../delete/appointment-category-delete-dialog.component';

@Component({
  selector: 'jhi-appointment-category',
  templateUrl: './appointment-category.component.html',
})
export class AppointmentCategoryComponent implements OnInit {
  appointmentCategories?: IAppointmentCategory[];
  isLoading = false;

  constructor(protected appointmentCategoryService: AppointmentCategoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.appointmentCategoryService.query().subscribe(
      (res: HttpResponse<IAppointmentCategory[]>) => {
        this.isLoading = false;
        this.appointmentCategories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAppointmentCategory): string {
    return item.id!;
  }

  delete(appointmentCategory: IAppointmentCategory): void {
    const modalRef = this.modalService.open(AppointmentCategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appointmentCategory = appointmentCategory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
