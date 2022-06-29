import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppointmentSlotDetailComponent } from './appointment-slot-detail.component';

describe('Component Tests', () => {
  describe('AppointmentSlot Management Detail Component', () => {
    let comp: AppointmentSlotDetailComponent;
    let fixture: ComponentFixture<AppointmentSlotDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AppointmentSlotDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ appointmentSlot: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(AppointmentSlotDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AppointmentSlotDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load appointmentSlot on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.appointmentSlot).toEqual(expect.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
