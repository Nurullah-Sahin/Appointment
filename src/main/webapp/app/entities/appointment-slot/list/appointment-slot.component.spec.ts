import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AppointmentSlotService } from '../service/appointment-slot.service';

import { AppointmentSlotComponent } from './appointment-slot.component';

describe('Component Tests', () => {
  describe('AppointmentSlot Management Component', () => {
    let comp: AppointmentSlotComponent;
    let fixture: ComponentFixture<AppointmentSlotComponent>;
    let service: AppointmentSlotService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppointmentSlotComponent],
      })
        .overrideTemplate(AppointmentSlotComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppointmentSlotComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AppointmentSlotService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 'ABC' }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.appointmentSlots?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
