import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppointmentSlot } from '../appointment-slot.model';
import { AppointmentSlotService } from '../service/appointment-slot.service';
import { AppointmentSlotDeleteDialogComponent } from '../delete/appointment-slot-delete-dialog.component';

@Component({
  selector: 'jhi-appointment-slot',
  templateUrl: './appointment-slot.component.html',
})
export class AppointmentSlotComponent implements OnInit {
  appointmentSlots?: IAppointmentSlot[];
  isLoading = false;

  constructor(protected appointmentSlotService: AppointmentSlotService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.appointmentSlotService.query().subscribe(
      (res: HttpResponse<IAppointmentSlot[]>) => {
        this.isLoading = false;
        this.appointmentSlots = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAppointmentSlot): string {
    return item.id!;
  }

  delete(appointmentSlot: IAppointmentSlot): void {
    const modalRef = this.modalService.open(AppointmentSlotDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appointmentSlot = appointmentSlot;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
