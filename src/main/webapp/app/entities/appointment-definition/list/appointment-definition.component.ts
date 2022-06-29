import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppointmentDefinition } from '../appointment-definition.model';
import { AppointmentDefinitionService } from '../service/appointment-definition.service';
import { AppointmentDefinitionDeleteDialogComponent } from '../delete/appointment-definition-delete-dialog.component';

@Component({
  selector: 'jhi-appointment-definition',
  templateUrl: './appointment-definition.component.html',
})
export class AppointmentDefinitionComponent implements OnInit {
  appointmentDefinitions?: IAppointmentDefinition[];
  isLoading = false;

  constructor(protected appointmentDefinitionService: AppointmentDefinitionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.appointmentDefinitionService.query().subscribe(
      (res: HttpResponse<IAppointmentDefinition[]>) => {
        this.isLoading = false;
        this.appointmentDefinitions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAppointmentDefinition): string {
    return item.id!;
  }

  delete(appointmentDefinition: IAppointmentDefinition): void {
    const modalRef = this.modalService.open(AppointmentDefinitionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appointmentDefinition = appointmentDefinition;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
