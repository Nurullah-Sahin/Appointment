import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppointmentCategoryDetailComponent } from './appointment-category-detail.component';

describe('Component Tests', () => {
  describe('AppointmentCategory Management Detail Component', () => {
    let comp: AppointmentCategoryDetailComponent;
    let fixture: ComponentFixture<AppointmentCategoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AppointmentCategoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ appointmentCategory: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(AppointmentCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AppointmentCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load appointmentCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.appointmentCategory).toEqual(expect.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
